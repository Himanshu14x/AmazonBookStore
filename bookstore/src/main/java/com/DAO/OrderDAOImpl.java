package com.DAO;


import com.entity.Order;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.*;

public class OrderDAOImpl implements OrderDAO {
    private DynamoDbClient dynamo;
    private final String ORDERS_TABLE = "amazonOrders";
    public OrderDAOImpl(DynamoDbClient dynamo) { this.dynamo = dynamo; }

    @Override
    public boolean placeOrder(Order o) {
        try {
            Map<String, AttributeValue> item = new HashMap<>();
            item.put("orderId", AttributeValue.builder().s(o.getOrderId()).build());
            item.put("email", AttributeValue.builder().s(o.getEmail()).build());
            item.put("createdAt", AttributeValue.builder().s(o.getCreatedAt()).build());
            item.put("status", AttributeValue.builder().s(o.getStatus()).build());
            item.put("total", AttributeValue.builder().n(Double.toString(o.getTotal())).build());

            // shipping/contact fields (if present)
            if (o.getPhoneNumber() != null) item.put("phoneNumber", AttributeValue.builder().s(o.getPhoneNumber()).build());
            if (o.getAddress() != null) item.put("address", AttributeValue.builder().s(o.getAddress()).build());
            if (o.getLandmark() != null) item.put("landmark", AttributeValue.builder().s(o.getLandmark()).build());
            if (o.getCity() != null) item.put("city", AttributeValue.builder().s(o.getCity()).build());
            if (o.getPincode() != null) item.put("pincode", AttributeValue.builder().s(o.getPincode()).build());
            if (o.getPaymentMethod() != null) item.put("paymentMethod", AttributeValue.builder().s(o.getPaymentMethod()).build());

            // items as a list of maps
            List<AttributeValue> items = new ArrayList<>();
            if (o.getItems() != null) {
                for (Map<String, String> it : o.getItems()) {
                    Map<String, AttributeValue> m = new HashMap<>();
                    for (Map.Entry<String, String> e : it.entrySet()) {
                        m.put(e.getKey(), AttributeValue.builder().s(e.getValue()).build());
                    }
                    items.add(AttributeValue.builder().m(m).build());
                }
            }
            item.put("items", AttributeValue.builder().l(items).build());

            PutItemRequest req = PutItemRequest.builder().tableName(ORDERS_TABLE).item(item).build();
            dynamo.putItem(req);
            return true;
        } catch (DynamoDbException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public List<Order> getOrdersByEmail(String email) {
        List<Order> out = new ArrayList<>();
        try {
            // query GSI email-index
            Map<String, String> exprNames = new HashMap<>();
            exprNames.put("#e", "email");
            Map<String, AttributeValue> exprVals = new HashMap<>();
            exprVals.put(":e", AttributeValue.builder().s(email).build());

            QueryRequest qr = QueryRequest.builder()
                    .tableName(ORDERS_TABLE)
                    .indexName("email-index")
                    .keyConditionExpression("#e = :e")
                    .expressionAttributeNames(exprNames)
                    .expressionAttributeValues(exprVals)
                    .build();

            software.amazon.awssdk.services.dynamodb.model.QueryResponse resp = dynamo.query(qr);
            for (Map<String, AttributeValue> item : resp.items()) {
                Order o = mapOrder(item);
                out.add(o);
            }
        } catch (DynamoDbException e) {
            e.printStackTrace();
        }
        return out;
    }

    @Override
    public List<Order> getAllOrders() {
        List<Order> out = new ArrayList<>();
        try {
            ScanRequest sr = ScanRequest.builder().tableName(ORDERS_TABLE).build();
            ScanResponse resp = dynamo.scan(sr);
            for (Map<String, AttributeValue> item : resp.items()) out.add(mapOrder(item));
        } catch (DynamoDbException e) {
            e.printStackTrace();
        }
        return out;
    }

    private Order mapOrder(Map<String, AttributeValue> item) {
        Order o = new Order();
        if (item.containsKey("orderId")) o.setOrderId(item.get("orderId").s());
        if (item.containsKey("email")) o.setEmail(item.get("email").s());
        if (item.containsKey("createdAt")) o.setCreatedAt(item.get("createdAt").s());
        if (item.containsKey("status")) o.setStatus(item.get("status").s());
        if (item.containsKey("total")) {
            try { o.setTotal(Double.parseDouble(item.get("total").n())); } catch (Exception ignored) {}
        }
        if (item.containsKey("paymentMethod"))o.setPaymentMethod(item.get("paymentMethod").s());
        
        if (item.containsKey("items")) {
            List<Map<String, String>> list = new ArrayList<>();
            for (AttributeValue av : item.get("items").l()) {
                Map<String, String> mm = new HashMap<>();
                for (Map.Entry<String, AttributeValue> e : av.m().entrySet()) {
                    mm.put(e.getKey(), e.getValue().s());
                }
                list.add(mm);
            }
            o.setItems(list);
        }
        return o;
    }
}
