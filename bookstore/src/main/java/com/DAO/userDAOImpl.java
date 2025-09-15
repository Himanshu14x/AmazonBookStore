package com.DAO;

import com.entity.User;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class userDAOImpl implements userDAO{

	private DynamoDbClient dynamo;
	private final String USERS_TABLE = "amazonUsers";

	public userDAOImpl(DynamoDbClient dynamo) {
		this.dynamo = dynamo;
	}

	@Override
	public boolean userRegister(User user) {
		try {
			Map<String, AttributeValue> item = new HashMap<>();
			
			item.put("email", AttributeValue.builder().s(user.getEmail()).build());
			item.put("name", AttributeValue.builder().s(user.getName()).build());
			item.put("password", AttributeValue.builder().s(user.getPassword()).build());
			if (user.getAddress() != null)
				item.put("address", AttributeValue.builder().s(user.getAddress()).build());
			if (user.getLandmark() != null)
				item.put("landmark", AttributeValue.builder().s(user.getLandmark()).build());
			if (user.getCity() != null)
				item.put("city", AttributeValue.builder().s(user.getCity()).build());
			if (user.getPincode() != null)
				item.put("pincode", AttributeValue.builder().s(user.getPincode()).build());

			PutItemRequest request = PutItemRequest.builder().tableName(USERS_TABLE).item(item).build();
			dynamo.putItem(request);
			return true;
		} catch (DynamoDbException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public User login(String email, String password) {
		try {
			Map<String, AttributeValue> key = new HashMap<>();
			key.put("email", AttributeValue.builder().s(email).build());

			GetItemRequest request = GetItemRequest.builder().tableName(USERS_TABLE).key(key).build();

			Map<String, AttributeValue> returned = dynamo.getItem(request).item();
			if (returned == null || returned.isEmpty())
				return null;

			String pw = returned.containsKey("password") ? returned.get("password").s() : null;
			if (pw == null || !pw.equals(password))
				return null;

			User u = new User();
			// fill user fields
			u.setEmail(email);
			u.setName(returned.containsKey("name") ? returned.get("name").s() : null);
			u.setPassword(pw);
			u.setAddress(returned.containsKey("address") ? returned.get("address").s() : null);
			u.setLandmark(returned.containsKey("landmark") ? returned.get("landmark").s() : null);
			u.setCity(returned.containsKey("city") ? returned.get("city").s() : null);
			u.setPincode(returned.containsKey("pincode") ? returned.get("pincode").s() : null);

			return u;
		} catch (DynamoDbException e) {
			e.printStackTrace();
			return null;
		}
	}

	// optional: implement a helper that lists all users (used for admin or debug)
	public java.util.List<User> listAllUsers() {
		java.util.List<User> users = new java.util.ArrayList<>();
		try {
			ScanRequest scanRequest = ScanRequest.builder().tableName(USERS_TABLE).build();
			ScanResponse response = dynamo.scan(scanRequest);
			for (Map<String, AttributeValue> item : response.items()) {
				User u = new User();
				u.setEmail(item.get("email").s());
				if (item.containsKey("name"))
					u.setName(item.get("name").s());
				if (item.containsKey("address"))
					u.setAddress(item.get("address").s());
				// set other fields similarly
				users.add(u);
			}
		} catch (DynamoDbException e) {
			e.printStackTrace();
		}
		return users;
	}
	
	
	// -- get recent viewed bookIds for a user (returns List<String>)
	public List<String> getRecentViewed(String email) {
	    try {
	        Map<String, AttributeValue> key = new HashMap<>();
	        key.put("email", AttributeValue.builder().s(email).build());
	        GetItemRequest request = GetItemRequest.builder().tableName(USERS_TABLE).key(key).build();
	        GetItemResponse resp = dynamo.getItem(request);
	        if (resp.hasItem() && resp.item().containsKey("recentViewed")) {
	            List<AttributeValue> list = resp.item().get("recentViewed").l();
	            List<String> out = new ArrayList<>();
	            for (AttributeValue av : list) {
	                out.add(av.s());
	            }
	            return out;
	        }
	    } catch (DynamoDbException e) {
	        e.printStackTrace();
	    }
	    return new ArrayList<>();
	}

	// -- add a bookId to front of recentViewed; keep uniqueness & max 10
	// Add/update recentViewed for a user: keep newest-first, unique, max 10
	public boolean addRecentViewed(String email, String bookId) {
	    if (email == null || email.trim().isEmpty() || bookId == null || bookId.trim().isEmpty()) {
	        System.out.println("[userDAOImpl] addRecentViewed: invalid email or bookId");
	        return false;
	    }
	    final String USERS_TABLE = "amazonUsers"; // adjust if your constant differs
	    try {
	        // 1) Fetch the existing user item
	        Map<String, AttributeValue> key = new HashMap<>();
	        key.put("email", AttributeValue.builder().s(email).build());
	        GetItemRequest gir = GetItemRequest.builder().tableName(USERS_TABLE).key(key).build();
	        GetItemResponse gres = dynamo.getItem(gir);

	        Map<String, AttributeValue> fetched = gres.item();
	        if (fetched == null || fetched.isEmpty()) {
	            System.out.println("[userDAOImpl] addRecentViewed: user item not found for " + email);
	            return false;
	        }

	        // 2) Copy into a mutable map (IMPORTANT: GetItemResponse.item() returns an unmodifiable Map)
	        Map<String, AttributeValue> userItem = new HashMap<>(fetched);

	        // 3) Read existing recentViewed (if any)
	        List<String> current = new ArrayList<>();
	        if (userItem.containsKey("recentViewed") && userItem.get("recentViewed").l() != null) {
	            List<AttributeValue> avList = userItem.get("recentViewed").l();
	            for (AttributeValue av : avList) {
	                if (av.s() != null) current.add(av.s());
	            }
	        }

	        // 4) Update list: move bookId to front, ensure uniqueness and limit 10
	        current.removeIf(x -> x.equals(bookId));
	        current.add(0, bookId);
	        if (current.size() > 10) {
	            current = current.subList(0, 10);
	        }

	        // 5) Convert back to AttributeValue list and put in userItem
	        List<AttributeValue> newAvList = current.stream()
	                .map(s -> AttributeValue.builder().s(s).build())
	                .collect(Collectors.toList());
	        userItem.put("recentViewed", AttributeValue.builder().l(newAvList).build());

	        // 6) Write updated item back to DynamoDB
	        PutItemRequest pir = PutItemRequest.builder().tableName(USERS_TABLE).item(userItem).build();
	        dynamo.putItem(pir);

	        System.out.println("[userDAOImpl] addRecentViewed: updated recentViewed for " + email + " -> " + current);
	        return true;

	    } catch (DynamoDbException e) {
	        System.out.println("[userDAOImpl] addRecentViewed: DynamoDbException");
	        e.printStackTrace();
	    } catch (Exception ex) {
	        System.out.println("[userDAOImpl] addRecentViewed: Exception");
	        ex.printStackTrace();
	    }
	    return false;
	}


	// -- cart methods: getCart stored as a map of bookId -> quantity (numbers stored as strings here)
	public Map<String, String> getCart(String email) {
	    try {
	        Map<String, AttributeValue> key = new HashMap<>();
	        key.put("email", AttributeValue.builder().s(email).build());
	        GetItemResponse resp = dynamo.getItem(GetItemRequest.builder().tableName(USERS_TABLE).key(key).build());
	        if (resp.hasItem() && resp.item().containsKey("cart")) {
	            Map<String, AttributeValue> avalmap = resp.item().get("cart").m();
	            Map<String, String> out = new HashMap<>();
	            for (Map.Entry<String, AttributeValue> e : avalmap.entrySet()) {
	                out.put(e.getKey(), e.getValue().n()); // using number as string
	            }
	            return out;
	        }
	    } catch (DynamoDbException e) {
	        e.printStackTrace();
	    }
	    return new HashMap<>();
	}

	// add or increment quantity for bookId in cart
	public boolean addToCart(String email, String bookId, int qtyToAdd) {
	    try {
	        Map<String, String> cart = getCart(email);
	        int currentQty = 0;
	        if (cart.containsKey(bookId)) {
	            try { currentQty = Integer.parseInt(cart.get(bookId)); } catch (Exception ignored) {}
	        }
	        cart.put(bookId, Integer.toString(currentQty + qtyToAdd));

	        // convert to AttributeValue map
	        Map<String, AttributeValue> m = new HashMap<>();
	        for (Map.Entry<String, String> e : cart.entrySet()) {
	            m.put(e.getKey(), AttributeValue.builder().n(e.getValue()).build());
	        }

	        // fetch full user item, update cart, put
	        Map<String, AttributeValue> key = new HashMap<>();
	        key.put("email", AttributeValue.builder().s(email).build());
	        GetItemResponse resp = dynamo.getItem(GetItemRequest.builder().tableName(USERS_TABLE).key(key).build());
	        Map<String, AttributeValue> userItem = resp.item();
	     
	        userItem.put("cart", AttributeValue.builder().m(m).build());
	        dynamo.putItem(builder -> builder.tableName(USERS_TABLE).item(userItem));
	        return true;
	    } catch (DynamoDbException e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	// clear cart on order placed
	public boolean clearCart(String email) {
	    try {
	        Map<String, AttributeValue> key = new HashMap<>();
	        key.put("email", AttributeValue.builder().s(email).build());
	        GetItemResponse resp = dynamo.getItem(GetItemRequest.builder().tableName(USERS_TABLE).key(key).build());
	        Map<String, AttributeValue> userItem = resp.item();
	        if (userItem == null) return false;
	        userItem.remove("cart"); // remove attribute
	        dynamo.putItem(builder -> builder.tableName(USERS_TABLE).item(userItem));
	        return true;
	    } catch (DynamoDbException e) {
	        e.printStackTrace();
	    }
	    return false;
	}

}
