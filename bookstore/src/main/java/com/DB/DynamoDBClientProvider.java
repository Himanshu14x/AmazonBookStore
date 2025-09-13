package com.DB;

import java.net.URI;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class DynamoDBClientProvider {

    private static volatile DynamoDbClient dynamoDbClient;
    private static final boolean USE_LOCAL = true;
    private static final String LOCAL_ENDPOINT = "http://localhost:8000";
    private static final Region REGION = Region.of("us-east-1");

    public static DynamoDbClient getClient() {
        if (dynamoDbClient == null) {
            synchronized (DynamoDBClientProvider.class) {
                if (dynamoDbClient == null) {
                    if (USE_LOCAL) {
                        System.out.println("DynamoDBClientProvider: using local endpoint " + LOCAL_ENDPOINT);
                        // Use short dummy creds (works fine for DynamoDB Local)
                        AwsBasicCredentials creds = AwsBasicCredentials.create("dummy", "dummy");

                        dynamoDbClient = DynamoDbClient.builder()
                                .endpointOverride(URI.create(LOCAL_ENDPOINT))
                                .region(REGION)
                                .credentialsProvider(StaticCredentialsProvider.create(creds))
                            
                                .overrideConfiguration(ClientOverrideConfiguration.builder().build())
                                .build();
                    } else {
                        dynamoDbClient = DynamoDbClient.builder()
                                .region(REGION)
                                .build();
                    }
                }
            }
        }
        return dynamoDbClient;
    }

    public static void closeClient() {
        if (dynamoDbClient != null) {
            dynamoDbClient.close();
            dynamoDbClient = null;
        }
    }
}
