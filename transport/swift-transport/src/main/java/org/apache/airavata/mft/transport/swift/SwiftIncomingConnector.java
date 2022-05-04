/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.airavata.mft.transport.swift;

import org.apache.airavata.mft.core.api.ConnectorConfig;
import org.apache.airavata.mft.core.api.IncomingChunkedConnector;
import org.apache.airavata.mft.credential.stubs.swift.SwiftSecret;
import org.apache.airavata.mft.credential.stubs.swift.SwiftSecretGetRequest;
import org.apache.airavata.mft.resource.client.ResourceServiceClient;
import org.apache.airavata.mft.resource.client.ResourceServiceClientBuilder;
import org.apache.airavata.mft.resource.stubs.common.GenericResource;
import org.apache.airavata.mft.resource.stubs.common.GenericResourceGetRequest;
import org.apache.airavata.mft.resource.stubs.s3.storage.S3Storage;
import org.apache.airavata.mft.resource.stubs.swift.storage.SwiftStorage;
import org.apache.airavata.mft.secret.client.SecretServiceClient;
import org.apache.airavata.mft.secret.client.SecretServiceClientBuilder;
import org.apache.commons.io.IOUtils;
import org.jclouds.ContextBuilder;
import org.jclouds.http.options.GetOptions;
import org.jclouds.openstack.keystone.auth.config.CredentialTypes;
import org.jclouds.openstack.keystone.config.KeystoneProperties;
import org.jclouds.openstack.swift.v1.SwiftApi;
import org.jclouds.openstack.swift.v1.domain.SwiftObject;
import org.jclouds.openstack.swift.v1.features.ObjectApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

public class SwiftIncomingConnector implements IncomingChunkedConnector {

    private static final Logger logger = LoggerFactory.getLogger(SwiftIncomingConnector.class);

    private GenericResource resource;
    private SwiftApi swiftApi;
    private ObjectApi objectApi;

    @Override
    public void init(ConnectorConfig cc) throws Exception {
        try (ResourceServiceClient resourceClient = ResourceServiceClientBuilder
                .buildClient(cc.getResourceServiceHost(), cc.getResourceServicePort())) {

            resource = resourceClient.get().getGenericResource(GenericResourceGetRequest.newBuilder()
                    .setAuthzToken(cc.getAuthToken())
                    .setResourceId(cc.getResourceId()).build());
        }

        if (resource.getStorageCase() != GenericResource.StorageCase.SWIFTSTORAGE) {
            logger.error("Invalid storage type {} specified for resource {}", resource.getStorageCase(), cc.getResourceId());
            throw new Exception("Invalid storage type specified for resource " + cc.getResourceId());
        }

        SwiftStorage swiftStorage = resource.getSwiftStorage();

        SwiftSecret swiftSecret;

        try (SecretServiceClient secretClient = SecretServiceClientBuilder.buildClient(
                cc.getSecretServiceHost(), cc.getSecretServicePort())) {

            swiftSecret = secretClient.swift().getSwiftSecret(SwiftSecretGetRequest.newBuilder()
                    .setAuthzToken(cc.getAuthToken())
                    .setSecretId(cc.getCredentialToken()).build());

            String provider = "openstack-swift";

            Properties overrides = new Properties();
            overrides.put(KeystoneProperties.KEYSTONE_VERSION, swiftStorage.getKeystoneVersion() + "");

            String identity = null;
            String credential = null;
            switch (swiftSecret.getSecretCase()) {
                case PASSWORDSECRET:
                    identity = swiftSecret.getPasswordSecret().getDomainId() + ":" + swiftSecret.getPasswordSecret().getUserName();
                    credential = swiftSecret.getPasswordSecret().getPassword();
                    overrides.put(KeystoneProperties.SCOPE, "projectId:" + swiftSecret.getPasswordSecret().getProjectId());
                    overrides.put(KeystoneProperties.CREDENTIAL_TYPE, CredentialTypes.PASSWORD_CREDENTIALS);
                    break;
                case AUTHCREDENTIALSECRET:
                    identity = swiftSecret.getAuthCredentialSecret().getCredentialId();
                    credential = swiftSecret.getAuthCredentialSecret().getCredentialSecret();
                    overrides.put(KeystoneProperties.CREDENTIAL_TYPE, CredentialTypes.API_ACCESS_KEY_CREDENTIALS);
                    break;
            }

            swiftApi = ContextBuilder.newBuilder(provider)
                    .endpoint(swiftStorage.getEndpoint())
                    .credentials(identity, credential)
                    .overrides(overrides)
                    .buildApi(SwiftApi.class);

            objectApi = swiftApi.getObjectApi(swiftStorage.getRegion(), swiftStorage.getContainer());

        }
    }

    @Override
    public void complete() throws Exception {
        if (swiftApi != null) {
            swiftApi.close();
        }
    }

    @Override
    public void downloadChunk(int chunkId, long startByte, long endByte, String downloadFile) throws Exception {
        SwiftObject swiftObject = objectApi.get(
                resource.getFile().getResourcePath(),
                GetOptions.Builder.range(startByte, endByte));

        InputStream inputStream = swiftObject.getPayload().openStream();

        File targetFile = new File("/tmp/targetFile.tmp");

        java.nio.file.Files.copy(
                inputStream,
                targetFile.toPath(),
                StandardCopyOption.REPLACE_EXISTING);

        IOUtils.closeQuietly(inputStream);
    }

    @Override
    public InputStream downloadChunk(int chunkId, long startByte, long endByte) throws Exception {

        SwiftObject swiftObject = objectApi.get(
                resource.getFile().getResourcePath(),
                GetOptions.Builder.range(startByte, endByte));

        return swiftObject.getPayload().openStream();
    }
}