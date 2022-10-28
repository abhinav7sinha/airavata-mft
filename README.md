[![Build Status](https://travis-ci.org/apache/airavata-mft.svg?branch=master)](https://travis-ci.org/apache/airavata-mft)


# Airavata Managed File Transfer
Airavata MFT provides services and clients for data transfer operations across storage platforms.

1. [Prerequisites](#prerequisites)
2. [Set up the application locally](#local-set-up)
3. [Make an S3 transfer from Command Line](#s3-transfer)
4. [Sample API Call](#sample-api-call)


## 1. Prerequisites<a name="prerequisites"/>
* Git - [Download & Install Git](https://git-scm.com/downloads)
* Java 18 - [Download & Install Java 18](https://jdk.java.net/18/)
* Maven 3 - [Download and Install Maven 3](https://maven.apache.org/install.html)
* Consul 1.7.1 - [Install Consul](https://releases.hashicorp.com/consul/)

## 2. Set up the application locally<a name="local-set-up"/>

### Build the project from scripts
* Go to scripts directory
```bash
cd scripts
```
* If you're on an apple-silicon computer, run the following:
```bash
./build.sh mac-arm64
```
otherwise, run:
```bash
/bin/bash build.sh
```
* This will build the whole project and unzip distributions of each service to the `airavata-mft` directory inside the root of the project

### 2a. Start airavata-mft services from IDE
* Start consul server
```bash
./consul agent -dev
```
* Start ResourceServiceApplication
`
org.apache.airavata.mft.resource.server.ResourceServiceApplication.java
`
* Start SecretServiceApplication
`
org/apache/airavata/mft/secret/server/SecretServiceApplication.java
`
* Start MftController
`
org/apache/airavata/mft/controller/MFTController.java
`
* Start ApiServiceApplication
`
org/apache/airavata/mft/api/ApiServiceApplication.java
`
* Start the agent
`
org/apache/airavata/mft/agent/MFTAgent.java
`

### 2b. Starting the distribution from Scripts
* You should have consul running inorder to start MFT. You can start consul by running ```/bin/bash start-consul.sh <os distribution>```.
  For example ```/bin/bash start-consul.sh mac-arm64```. You can see supported distributions by running ```/bin/bash start-consul.sh -h```
* If your OS distribution is not provided in the script, you can manually install Consul using pre compiled binaries https://www.consul.io/docs/install/index.html#precompiled-binaries
* To start MFT stack, run ```/bin/bash start-mft.sh```. This will start all the services and an Agent to transfer data
* To stop MFT stack, run ```/bin/bash start-mft.sh```
* If you want to see logs of any running service, run ```/bin/bash log.sh <service name>```. For example, ```/bin/bash log.sh secret```.
  To view available services, run  ```/bin/bash log.sh -h```

Now you can run airavata-mft using command line:
* go to `airavata-mft` directory inside the root of the project
```bash
cd airavata-mft
```
* run the following command:
```bash
java -jar mft-client.jar -h
```

### Defaults for Resource and Secret Service
* The Backends for Secret and Resource services are by default set to File based backend so you can find sample config json 
files in conf directory of each distribution
* You can easily change the backend by updating the applicationContext.xml file in conf directory

## 3. Make an S3 transfer from Command Line<a name="ss3-transfer"/>
* go to `airavata-mft` directiory inside the project root. Maker sure `mft-client.jar` is present here.
```bash
cd airavata-mft
ls
```
* Add S3 resources:
```bash
java -jar mft-client.jar s3 remote add -b=<bucket-name-1> -e=https://s3.us-east-2.amazonaws.com -k=<access-key-1> -n=<identifier-name-1> -r=us-east-2 -s=<secret-key-1>
java -jar mft-client.jar s3 remote add -b=<bucket-name-2> -e=https://s3.us-east-2.amazonaws.com -k=<access-key-2> -n=<identifier-name-2> -r=us-east-2 -s=<secret-key-2>
```
* You can view storage ids along with other details for these resources by running the following command:
```bash
java -jar mft-client.jar s3 remote list
```
* Submit a transfer command
```bash
java -jar mft-client.jar transfer submit -d=<destination-storage-id> -dp=test/test-file2.txt -s=<source-storage-id> -sp=test-file.txt
```

## 4. Sample API Call<a name="sample-api-call"/>

* This sample is for the Java gRPC client of MFT API. Request resource ids and secret ids are analogous to default values 
in secret.json and resources.json files available for File based backends. 

* You should have mft-api-client dependency in your project

```
<dependency>
    <groupId>org.apache.airavata</groupId>
    <artifactId>mft-api-client</artifactId>
    <version>0.01-SNAPSHOT</version>
</dependency>
```

```
import org.apache.airavata.mft.api.client.MFTApiClient;
import org.apache.airavata.mft.api.service.*;

public class SampleClient {
    public static void main(String args[]) throws InterruptedException {

        MFTApiServiceGrpc.MFTApiServiceBlockingStub client = MFTApiClient.buildClient("localhost", 7004);

        String sourceId = "remote-ssh-resource2";
        String sourceToken = "local-ssh-cred";
        String destId = "remote-ssh-resource";
        String destToken = "local-ssh-cred";

        TransferApiRequest request = TransferApiRequest.newBuilder()
                .setSourceId(sourceId)
                .setSourceToken(sourceToken)
                .setSourceType("SCP")
                .setDestinationId(destId)
                .setDestinationToken(destToken)
                .setDestinationType("SCP")
                .setAffinityTransfer(false).build();

        // Submitting the transfer to MFT
        TransferApiResponse transferApiResponse = client.submitTransfer(request);
        while(true) {
            // Monitoring transfer status
            try {
                TransferStateApiResponse transferState = client.getTransferState(TransferStateApiRequest.newBuilder().setTransferId(transferApiResponse.getTransferId()).build());
                System.out.println("Latest Transfer State " + transferState.getState());
                if ("COMPLETED".equals(transferState.getState()) || "FAILED".equals(transferState.getState()) {
                    break;
                }

            } catch (Exception e) {
                System.out.println("Errored " + e.getMessage());
            }
            Thread.sleep(1000);
        }
    }
}
```