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

package org.apache.airavata.mft.transport.azure;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobProperties;
import org.apache.airavata.mft.common.AuthToken;
import org.apache.airavata.mft.core.DirectoryResourceMetadata;
import org.apache.airavata.mft.core.FileResourceMetadata;
import org.apache.airavata.mft.core.ResourceTypes;
import org.apache.airavata.mft.core.api.MetadataCollector;
import org.apache.airavata.mft.credential.stubs.azure.AzureSecret;
import org.apache.airavata.mft.credential.stubs.azure.AzureSecretGetRequest;
import org.apache.airavata.mft.resource.client.ResourceServiceClient;
import org.apache.airavata.mft.resource.client.ResourceServiceClientBuilder;
import org.apache.airavata.mft.resource.client.StorageServiceClient;
import org.apache.airavata.mft.resource.client.StorageServiceClientBuilder;
import org.apache.airavata.mft.resource.stubs.azure.storage.AzureStorage;
import org.apache.airavata.mft.resource.stubs.azure.storage.AzureStorageGetRequest;
import org.apache.airavata.mft.resource.stubs.common.FileResource;
import org.apache.airavata.mft.resource.stubs.common.GenericResource;
import org.apache.airavata.mft.resource.stubs.common.GenericResourceGetRequest;
import org.apache.airavata.mft.resource.stubs.s3.storage.S3StorageGetRequest;
import org.apache.airavata.mft.secret.client.SecretServiceClient;
import org.apache.airavata.mft.secret.client.SecretServiceClientBuilder;

public class AzureMetadataCollector implements MetadataCollector {

    private String resourceServiceHost;
    private int resourceServicePort;
    private String secretServiceHost;
    private int secretServicePort;
    boolean initialized = false;

    @Override
    public void init( String resourceServiceHost, int resourceServicePort, String secretServiceHost, int secretServicePort) {
        this.resourceServiceHost = resourceServiceHost;
        this.resourceServicePort = resourceServicePort;
        this.secretServiceHost = secretServiceHost;
        this.secretServicePort = secretServicePort;
        this.initialized = true;
    }

    private void checkInitialized() {
        if (!initialized) {
            throw new IllegalStateException("Azure Metadata Collector is not initialized");
        }
    }

    @Override
    public FileResourceMetadata getFileResourceMetadata(AuthToken authZToken, String resourcePath,
                                                        String storageId, String credentialToken) throws Exception {
        checkInitialized();

        if (!isAvailable(authZToken, resourcePath, storageId, credentialToken)) {
            throw new Exception("Azure blob can not find for resource path " + resourcePath);
        }

        AzureStorage azureStorage;
        try (StorageServiceClient storageServiceClient = StorageServiceClientBuilder
                .buildClient(resourceServiceHost, resourceServicePort)) {

            azureStorage = storageServiceClient.azure()
                    .getAzureStorage(AzureStorageGetRequest.newBuilder().setStorageId(storageId).build());
        }

        AzureSecret azureSecret;
        try (SecretServiceClient secretClient = SecretServiceClientBuilder.buildClient(
                secretServiceHost, secretServicePort)) {
            azureSecret = secretClient.azure().getAzureSecret(AzureSecretGetRequest.newBuilder().setSecretId(credentialToken).build());

        }

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(azureSecret.getConnectionString()).buildClient();

        BlobClient blobClient = blobServiceClient.getBlobContainerClient(azureStorage.getContainer())
                                                .getBlobClient(resourcePath);

        BlobProperties properties = blobClient.getBlockBlobClient().getProperties();
        FileResourceMetadata metadata = new FileResourceMetadata();
        metadata.setResourceSize(properties.getBlobSize());
        metadata.setCreatedTime(properties.getCreationTime().toEpochSecond());
        metadata.setUpdateTime(properties.getCreationTime().toEpochSecond());

        byte[] contentMd5 = properties.getContentMd5();
        StringBuilder md5sb = new StringBuilder();
        for (byte aByte : contentMd5) {
            md5sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }

        metadata.setMd5sum(md5sb.toString());

        return metadata;
    }

    @Override
    public DirectoryResourceMetadata getDirectoryResourceMetadata(AuthToken authZToken, String resourcePath, String storageId, String credentialToken) throws Exception {
        throw new UnsupportedOperationException("Method not implemented");
    }


    @Override
    public Boolean isAvailable(AuthToken authZToken, String resourcePath, String storageId, String credentialToken) throws Exception {
        checkInitialized();

        AzureStorage storage;
        try (StorageServiceClient storageServiceClient = StorageServiceClientBuilder
                .buildClient(resourceServiceHost, resourceServicePort)) {

            storage = storageServiceClient.azure()
                    .getAzureStorage(AzureStorageGetRequest.newBuilder().setStorageId(storageId).build());
        }

        AzureSecret azureSecret;
        try (SecretServiceClient secretClient = SecretServiceClientBuilder.buildClient(
                secretServiceHost, secretServicePort)) {
            azureSecret = secretClient.azure().getAzureSecret(AzureSecretGetRequest.newBuilder().setSecretId(credentialToken).build());

        }

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(azureSecret.getConnectionString()).buildClient();
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(storage.getContainer());
        boolean containerExists = containerClient.exists();
        if (!containerExists) {
            return false;
        }
        return containerClient.getBlobClient(resourcePath).exists();
    }


}
