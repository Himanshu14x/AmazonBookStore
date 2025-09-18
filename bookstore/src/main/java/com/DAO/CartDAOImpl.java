package com.DAO;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import java.util.*;


/**
 * CartDAOImpl - DynamoDB-backed implementation of CartDAO.
 */
public class CartDAOImpl implements CartDAO {

    private final DynamoDbClient dynamo;
    private final String USERS_TABLE = "amazonUsers"; // ensure matches your table name

    public CartDAOImpl(DynamoDbClient client) {
        this.dynamo = client;
    }

    @Override
    public Map<String, String> getCart(String email) {
        Map<String, String> out = new HashMap<>();
        if (email == null || email.trim().isEmpty()) return out;
        try {
            Map<String, AttributeValue> key = Map.of("email", AttributeValue.builder().s(email).build());
            GetItemRequest gir = GetItemRequest.builder().tableName(USERS_TABLE).key(key).build();
            GetItemResponse gres = dynamo.getItem(gir);
            if (gres == null || !gres.hasItem()) return out;
            Map<String, AttributeValue> item = gres.item();
            if (item.containsKey("cart") && item.get("cart").m() != null) {
                Map<String, AttributeValue> cartMap = item.get("cart").m();
                for (Map.Entry<String, AttributeValue> e : cartMap.entrySet()) {
                    String bookId = e.getKey();
                    AttributeValue av = e.getValue();
                    // store qty as string for compatibility
                    if (av.n() != null) out.put(bookId, av.n());
                    else if (av.s() != null) out.put(bookId, av.s());
                }
            }
        } catch (DynamoDbException ex) {
            ex.printStackTrace();
        }
        return out;
    }

    @Override
    public boolean addToCart(String email, String bookId, int qtyToAdd) {
        if (email == null || email.trim().isEmpty() || bookId == null || bookId.trim().isEmpty()) return false;
        try {
            // Fetch user item
            Map<String, AttributeValue> key = Map.of("email", AttributeValue.builder().s(email).build());
            GetItemRequest gir = GetItemRequest.builder().tableName(USERS_TABLE).key(key).build();
            GetItemResponse gres = dynamo.getItem(gir);
            Map<String, AttributeValue> userItem = gres.item();
            if (userItem == null) {
                System.out.println("[CartDAOImpl] addToCart: user not found for " + email);
                return false;
            }

            // make mutable copy
            Map<String, AttributeValue> mutable = new HashMap<>(userItem);

            // current cart map (may be null)
            Map<String, AttributeValue> cartMap = new HashMap<>();
            if (mutable.containsKey("cart") && mutable.get("cart").m() != null) {
                cartMap.putAll(mutable.get("cart").m());
            }

            int currentQty = 0;
            if (cartMap.containsKey(bookId)) {
                AttributeValue av = cartMap.get(bookId);
                try { currentQty = Integer.parseInt(av.n()); } catch (Exception ignored) {
                    try { currentQty = Integer.parseInt(av.s()); } catch (Exception ignored2) {}
                }
            }
            int newQty = currentQty + Math.max(0, qtyToAdd);
            if (newQty <= 0) {
                // remove entry
                cartMap.remove(bookId);
            } else {
                cartMap.put(bookId, AttributeValue.builder().n(Integer.toString(newQty)).build());
            }

            // Put back cart map (if empty, remove attribute)
            if (cartMap.isEmpty()) {
                mutable.remove("cart");
            } else {
                mutable.put("cart", AttributeValue.builder().m(cartMap).build());
            }

            PutItemRequest pir = PutItemRequest.builder().tableName(USERS_TABLE).item(mutable).build();
            dynamo.putItem(pir);
            return true;

        } catch (DynamoDbException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateCartItem(String email, String bookId, int qty) {
        if (email == null || email.trim().isEmpty() || bookId == null || bookId.trim().isEmpty()) return false;
        try {
            Map<String, AttributeValue> key = Map.of("email", AttributeValue.builder().s(email).build());
            GetItemRequest gir = GetItemRequest.builder().tableName(USERS_TABLE).key(key).build();
            GetItemResponse gres = dynamo.getItem(gir);
            Map<String, AttributeValue> userItem = gres.item();
            if (userItem == null) {
                System.out.println("[CartDAOImpl] updateCartItem: user not found for " + email);
                return false;
            }

            Map<String, AttributeValue> mutable = new HashMap<>(userItem);
            Map<String, AttributeValue> cartMap = new HashMap<>();
            if (mutable.containsKey("cart") && mutable.get("cart").m() != null) {
                cartMap.putAll(mutable.get("cart").m());
            }

            if (qty <= 0) {
                cartMap.remove(bookId);
            } else {
                cartMap.put(bookId, AttributeValue.builder().n(Integer.toString(qty)).build());
            }

            if (cartMap.isEmpty()) {
                mutable.remove("cart");
            } else {
                mutable.put("cart", AttributeValue.builder().m(cartMap).build());
            }

            PutItemRequest pir = PutItemRequest.builder().tableName(USERS_TABLE).item(mutable).build();
            dynamo.putItem(pir);
            return true;
        } catch (DynamoDbException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeFromCart(String email, String bookId) {
        return updateCartItem(email, bookId, 0);
    }

    @Override
    public boolean clearCart(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        try {
            Map<String, AttributeValue> key = Map.of("email", AttributeValue.builder().s(email).build());
            GetItemRequest gir = GetItemRequest.builder().tableName(USERS_TABLE).key(key).build();
            GetItemResponse gres = dynamo.getItem(gir);
            Map<String, AttributeValue> userItem = gres.item();
            if (userItem == null) {
                System.out.println("[CartDAOImpl] clearCart: user not found for " + email);
                return false;
            }
            Map<String, AttributeValue> mutable = new HashMap<>(userItem);
            mutable.remove("cart"); // remove the attribute entirely
            PutItemRequest pir = PutItemRequest.builder().tableName(USERS_TABLE).item(mutable).build();
            dynamo.putItem(pir);
            return true;
        } catch (DynamoDbException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
