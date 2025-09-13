package com.DAO;

import com.entity.User;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.HashMap;
import java.util.Map;

public class userDAOImpl implements userDAO{

	private DynamoDbClient dynamo;
	private final String USERS_TABLE = "Users";

	public userDAOImpl(DynamoDbClient dynamo) {
		this.dynamo = dynamo;
	}

	@Override
	public boolean userRegister(User user) {
		try {
			Map<String, AttributeValue> item = new HashMap<>();
			// Use email as primary key (partition key). If you want numeric id, generate
			// one.
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
}
