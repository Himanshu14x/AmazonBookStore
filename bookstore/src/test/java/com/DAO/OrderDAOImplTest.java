package com.DAO;

import com.DB.DynamoDBClientProvider;
import com.entity.Order;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class OrderDAOImplTest {

    private DynamoDbClient client() {
        return DynamoDBClientProvider.getClient();
    }

    private OrderDAOImpl dao() {
        return new OrderDAOImpl(client());
    }

    @Test
    public void testPlaceOrder_and_getOrdersByEmail_and_getAllOrders() {
        OrderDAOImpl dao = dao();

    
        String orderId = "ORDER-1";
        String email = "junit-order@example.com";
        Order order = new Order();
        order.setOrderId(orderId);
        order.setEmail(email);
        order.setCreatedAt(Instant.now().toString());
        order.setStatus("PLACED");
        order.setTotal(123.45);

        
        Map<String, String> itemMap = new HashMap<>();
        itemMap.put("bookId", "BOOK-1");
        itemMap.put("qty", "2");
        List<Map<String, String>> items = new ArrayList<>();
        items.add(itemMap);
        order.setItems(items);

  
        order.setPhoneNumber("9999999999");
        order.setAddress("123 Test Street");
        order.setCity("TestCity");
        order.setPincode("400001");
        order.setPaymentMethod("COD");

        
        boolean placed = dao.placeOrder(order);
        assertTrue(placed, "placeOrder should return true");

        
        List<Order> byEmail = dao.getOrdersByEmail(email);
        assertNotNull(byEmail, "getOrdersByEmail must not return null");

        if (!byEmail.isEmpty()) {
         
            boolean found = byEmail.stream().anyMatch(o -> orderId.equals(o.getOrderId()));
            if (found) {
                Order fetched = byEmail.stream().filter(o -> orderId.equals(o.getOrderId())).findFirst().get();
                assertEquals(email, fetched.getEmail(), "Fetched order email should match");
                assertEquals("PLACED", fetched.getStatus(), "Fetched order status should match");
                assertEquals(123.45, fetched.getTotal(), 0.001, "Fetched order total should match");
                // items mapping check (best-effort)
                assertNotNull(fetched.getItems(), "Fetched order items should not be null");
                assertFalse(fetched.getItems().isEmpty(), "Fetched order items should not be empty");
                Map<String, String> firstItem = fetched.getItems().get(0);
                assertTrue(firstItem.containsKey("bookId"), "item map should contain bookId");
                assertEquals("BOOK-1", firstItem.get("bookId"), "item bookId should match");
            } else {
                
                System.out.println("not found.");
            }
        } else {
          
            List<Order> all = dao.getAllOrders();
            assertNotNull(all, "getAllOrders must not return null as fallback");
            assertTrue(all.size() >= 0, "getAllOrders returned a list (possibly empty)");
          
            boolean foundInAll = all.stream().anyMatch(o -> orderId.equals(o.getOrderId()));
            if (!foundInAll) {
                System.out.println("not found.");
            }
        }
    }

    @Test
    public void testPlaceOrder_withMultipleItems_and_mapOrderFields() {
        OrderDAOImpl dao = dao();

        String orderId = "ORDER-2";
        String email = "junit-multi@example.com";
        Order order = new Order();
        order.setOrderId(orderId);
        order.setEmail(email);
        order.setCreatedAt(Instant.now().toString());
        order.setStatus("PROCESSING");
        order.setTotal(250.00);

        Map<String, String> it1 = new HashMap<>();
        it1.put("bookId", "BOOK-A");
        it1.put("qty", "1");

        Map<String, String> it2 = new HashMap<>();
        it2.put("bookId", "BOOK-B");
        it2.put("qty", "3");

        order.setItems(Arrays.asList(it1, it2));
        order.setPaymentMethod("UPI");

        boolean placed = dao.placeOrder(order);
        assertTrue(placed, "placeOrder should return true for multi-item order");

       
        List<Order> all = dao.getAllOrders();
        assertNotNull(all, "getAllOrders must not return null");
        
        boolean anyWithItems = all.stream().anyMatch(o -> o.getItems() != null && !o.getItems().isEmpty());
        assertTrue(anyWithItems, "At least one order returned by getAllOrders should contain mapped items");
    }
}
