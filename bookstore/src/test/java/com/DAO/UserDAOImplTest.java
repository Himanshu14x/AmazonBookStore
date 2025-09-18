package com.DAO;

import com.entity.User;
import org.junit.jupiter.api.*;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.*;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.net.URI;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDAOImplTest {

    private DynamoDbClient client;
    private final String USERS_TABLE = "amazonUsers";

    @BeforeAll
    public void beforeAll() throws Exception {
        // Configure client to talk to local DynamoDB
        client = DynamoDbClient.builder()
                .endpointOverride(new URI("http://localhost:8000"))
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("dummy", "dummy")))
                .build();

  
    }

   

    @BeforeEach
    public void beforeEach() {
     
        Map<String, AttributeValue> alice = new HashMap<>();
        alice.put("email", AttributeValue.builder().s("Test@amazon.com").build());
        alice.put("name", AttributeValue.builder().s("Himanshu").build());
        alice.put("password", AttributeValue.builder().s("password@123").build());
        client.putItem(PutItemRequest.builder().tableName(USERS_TABLE).item(alice).build());
    }

    @Test
    public void testGetUserByEmail_returnsCorrectUser() {
        userDAOImpl dao = new userDAOImpl(client);
        User u = dao.getUserByEmail("Test@amazon.com");
        assertNotNull(u);
        assertEquals("Himanshu", u.getName());
        assertEquals("password@123", u.getPassword());
    }

    @Test
    public void testUserRegister_preventsOverwriteUsingConditionalPut() {
        userDAOImpl dao = new userDAOImpl(client);

        User u = new User();
        u.setEmail("Test@amazon.com"); 
        u.setName("Himanshu Rajpoot");
        u.setPassword("pw");

        boolean ok = dao.userRegister(u);
        assertFalse(ok, "Registering an existing email should return false (should not overwrite)");

        // Ensure original data didn't change
        User fetched = dao.getUserByEmail("Test@amazon.com");
        assertEquals("Himanshu", fetched.getName());
    }

    @Test
    public void testAddRecentViewed_updatesListCorrectly() {
        userDAOImpl dao = new userDAOImpl(client);

        // add recentViewed B1
        boolean ok1 = dao.addRecentViewed("Test@amazon.com", "B1");
        assertTrue(ok1);

        // add B2 then B1 again to move it to front
        boolean ok2 = dao.addRecentViewed("Test@amazon.com", "B2");
        assertTrue(ok2);
        boolean ok3 = dao.addRecentViewed("Test@amazon.com", "B1");
        assertTrue(ok3);

        
        Map<String, AttributeValue> key = Map.of("email", AttributeValue.builder().s("Test@amazon.com").build());
        GetItemResponse resp = client.getItem(GetItemRequest.builder().tableName(USERS_TABLE).key(key).build());
        assertTrue(resp.hasItem());
        Map<String, AttributeValue> item = resp.item();

        

        if (item.containsKey("recentViewed") && item.get("recentViewed").l() != null) {
            List<AttributeValue> avList = item.get("recentViewed").l();
            List<String> ids = avList.stream().map(AttributeValue::s).collect(Collectors.toList());
            assertEquals("B1", ids.get(0));
        } else {
            fail("recentViewed attribute not found or in unexpected format");
        }
    }
}
