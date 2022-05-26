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

package org.apache.airavata.mft.transport.scp;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;
import org.apache.airavata.mft.core.api.ConnectorConfig;
import org.apache.airavata.mft.core.api.OutgoingStreamingConnector;
import org.apache.airavata.mft.credential.stubs.scp.SCPSecret;
import org.apache.airavata.mft.credential.stubs.scp.SCPSecretGetRequest;
import org.apache.airavata.mft.resource.client.ResourceServiceClient;
import org.apache.airavata.mft.resource.client.ResourceServiceClientBuilder;
import org.apache.airavata.mft.resource.stubs.common.GenericResource;
import org.apache.airavata.mft.resource.stubs.common.GenericResourceGetRequest;
import org.apache.airavata.mft.resource.stubs.scp.storage.SCPStorage;
import org.apache.airavata.mft.secret.client.SecretServiceClient;
import org.apache.airavata.mft.secret.client.SecretServiceClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class SCPOutgoingConnector implements OutgoingStreamingConnector {

    private static final Logger logger = LoggerFactory.getLogger(SCPOutgoingConnector.class);

    private GenericResource resource;
    private Session session;
    private OutputStream out;
    private InputStream in;
    private Channel channel;
    private ConnectorConfig cc;
    private final byte[] buf = new byte[1024];


    @Override
    public void init(ConnectorConfig cc) throws Exception {

        this.cc = cc;
        try (ResourceServiceClient resourceClient = ResourceServiceClientBuilder
                .buildClient(cc.getResourceServiceHost(), cc.getResourceServicePort())) {

            resource = resourceClient.get().getGenericResource(GenericResourceGetRequest.newBuilder()
                    .setAuthzToken(cc.getAuthToken())
                    .setResourceId(cc.getResourceId()).build());
        }

        if (resource.getStorageCase() != GenericResource.StorageCase.SCPSTORAGE) {
            logger.error("Invalid storage type {} specified for resource {}", resource.getStorageCase(), cc.getResourceId());
            throw new Exception("Invalid storage type specified for resource " + cc.getResourceId());
        }

        SCPSecret scpSecret;

        try (SecretServiceClient secretClient = SecretServiceClientBuilder.buildClient(
                cc.getSecretServiceHost(), cc.getSecretServicePort())) {

            scpSecret = secretClient.scp().getSCPSecret(SCPSecretGetRequest.newBuilder()
                    .setAuthzToken(cc.getAuthToken())
                    .setSecretId(cc.getCredentialToken()).build());
        }

        SCPStorage scpStorage = resource.getScpStorage();
        logger.info("Creating a ssh session for {}@{}:{}", scpSecret.getUser(), scpStorage.getHost(), scpStorage.getPort());

        this.session = SCPTransportUtil.createSession(
                scpSecret.getUser(),
                scpStorage.getHost(),
                scpStorage.getPort(),
                scpSecret.getPrivateKey().getBytes(),
                scpSecret.getPublicKey().getBytes(),
                scpSecret.getPassphrase().equals("")? null : scpSecret.getPassphrase().getBytes());

        if (session == null) {
            logger.error("Session can not be null. Make sure that SCP Receiver is properly initialized");
            throw new Exception("Session can not be null. Make sure that SCP Receiver is properly initialized");
        }
    }

    private String escapeSpecialChars(String path) {
        return  path.replace(" ", "\\ ");
    }

    @Override
    public OutputStream fetchOutputStream() throws Exception {
        String resourcePath = null;
        switch (resource.getResourceCase()){
            case FILE:
                resourcePath = resource.getFile().getResourcePath();
                break;
            case DIRECTORY:
                throw new Exception("A directory path can not be streamed");
            case RESOURCE_NOT_SET:
                throw new Exception("Resource was not set in resource with id " + resource.getResourceId());
        }

        return fetchOutputStreamJCraft(escapeSpecialChars(resourcePath), cc.getMetadata().getResourceSize());
    }

    @Override
    public OutputStream fetchOutputStream(String childPath) throws Exception {
        String resourcePath = null;
        switch (resource.getResourceCase()){
            case FILE:
                throw new Exception("A child path can not be associated with a file parent");
            case DIRECTORY:
                resourcePath = resource.getDirectory().getResourcePath();
                if (!childPath.startsWith(resourcePath)) {
                    throw new Exception("Child path " + childPath + " is not in the parent path " + resourcePath);
                }
                resourcePath = childPath;
                break;
            case RESOURCE_NOT_SET:
                throw new Exception("Resource was not set in resource with id " + resource.getResourceId());
        }

        return fetchOutputStreamJCraft(escapeSpecialChars(resourcePath), cc.getMetadata().getResourceSize());
    }

    public OutputStream fetchOutputStreamJCraft(String resourcePath, long fileSize) throws Exception {
        boolean ptimestamp = true;

        // exec 'scp -t rfile' remotely
        String command = "scp " + (ptimestamp ? "-p" : "") + " -t " + resourcePath;
        channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);

        // get I/O streams for remote scp
        out = channel.getOutputStream();
        in = channel.getInputStream();

        channel.connect();

        if (checkAck(in) != 0) {
            throw new IOException("Error code found in ack " + (checkAck(in)));
        }

        if (ptimestamp) {
            command = "T" + (System.currentTimeMillis() / 1000) + " 0";
            // The access time should be sent here,
            // but it is not accessible with JavaAPI ;-<
            command += (" " + (System.currentTimeMillis() / 1000) + " 0\n");
            out.write(command.getBytes());
            out.flush();
            if (checkAck(in) != 0) {
                throw new IOException("Error code found in ack " + (checkAck(in)));
            }
        }

        // send "C0644 filesize filename", where filename should not include '/'
        command = "C0644 " + fileSize + " ";
        if (resourcePath.lastIndexOf('/') > 0) {
            command += resourcePath.substring(resourcePath.lastIndexOf('/') + 1);
        } else {
            command += resourcePath;
        }

        command += "\n";
        out.write(command.getBytes());
        out.flush();

        if (checkAck(in) != 0) {
            throw new IOException("Error code found in ack " + (checkAck(in)));
        }

        return out;
    }

    @Override
    public void complete() throws Exception {
        buf[0] = 0;
        out.write(buf, 0, 1);
        out.flush();

        if (checkAck(in) != 0) {
            throw new IOException("Error code found in ack " + (checkAck(in)));
        }
        out.close();
        channel.disconnect();
        session.disconnect();
    }

    public int checkAck(InputStream in) throws IOException {
        int b = in.read();
        // b may be 0 for success,
        //          1 for error,
        //          2 for fatal error,
        //         -1
        if (b == 0) return b;
        if (b == -1) return b;

        if (b == 1 || b == 2) {
            StringBuffer sb = new StringBuffer();
            int c;
            do {
                c = in.read();
                sb.append((char) c);
            }
            while (c != '\n');
            if (b == 1) { // error
                logger.error("Check Ack Failure b = 1 " + sb.toString());
            }
            if (b == 2) { // fatal error
                logger.error("Check Ack Failure b = 2 " + sb.toString());
            }
        }
        return b;
    }
}
