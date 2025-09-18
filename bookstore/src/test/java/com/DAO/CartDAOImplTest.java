package com.DAO;

import com.DB.DynamoDBClientProvider;
import org.junit.jupiter.api.*;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;



import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CartDAOImplTest {

    private DynamoDbClient client;
    private com.DAO.CartDAOImpl cartDao;
    private com.DAO.userDAOImpl userDao;

    private final String USERS_TABLE = "amazonUsers";
    private final String TEST_EMAIL = "Test@amazon.com";

    @BeforeAll
    public void init() {
        client = DynamoDBClientProvider.getClient();
        assertNotNull(client);
        cartDao = new CartDAOImpl(client);
        userDao = new userDAOImpl(client);
    }

    

 
    @Test
    public void testAddToCart_andGetCart() {
       
        Map<String, AttributeValue> key = Map.of("email", AttributeValue.builder().s(TEST_EMAIL).build());
        GetItemResponse before = client.getItem(GetItemRequest.builder().tableName(USERS_TABLE).key(key).build());

        boolean ok = cartDao.addToCart(TEST_EMAIL, "BOOK-1", 2);
        assertTrue(ok, "addToCart should succeed");

        try {
            Map<String, String> cart = cartDao.getCart(TEST_EMAIL); 
            assertNotNull(cart);
            assertTrue(cart.containsKey("BOOK-1"));
            assertEquals("2", cart.get("BOOK-1"));

        } catch (Exception e) {
          
        e.printStackTrace();
        }
    }

    @Test
    public void testClearCart_clearsCart() {
        
        boolean ok = cartDao.addToCart(TEST_EMAIL, "BOOK-2", 1);
        assertTrue(ok);

     
        boolean cleared = cartDao.clearCart(TEST_EMAIL);
        assertTrue(cleared, "clearCart should succeed");

        // verify user item cart attribute removed or empty
        Map<String, AttributeValue> key = Map.of("email", AttributeValue.builder().s(TEST_EMAIL).build());
        GetItemResponse after = client.getItem(GetItemRequest.builder().tableName(USERS_TABLE).key(key).build());
        assertTrue(after.hasItem());
        Map<String, AttributeValue> item = after.item();
        // Depending on implementation, cart may be absent or empty map — accept either
        if (item.containsKey("cart")) {
            // if it's a map, size may be zero - best effort check
            AttributeValue av = item.get("cart");
          
            if (av.m() != null) {
                assertTrue(av.m().isEmpty() || !av.m().containsKey("BOOK-2"));
            }
        }
    }
}
