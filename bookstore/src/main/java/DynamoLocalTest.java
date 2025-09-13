import java.net.URI;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.ListTablesRequest;

public class DynamoLocalTest {
    public static void main(String[] args) {
        DynamoDbClient client = DynamoDbClient.builder()
            .endpointOverride(URI.create("http://localhost:8000"))
            .region(Region.of("us-east-1"))
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create("dummy","dummy")))
            .build();

        System.out.println("Testing listTables...");
        client.listTables(ListTablesRequest.builder().limit(10).build())
              .tableNames().forEach(System.out::println);
        System.out.println("Done");
        client.close();
    }
}
