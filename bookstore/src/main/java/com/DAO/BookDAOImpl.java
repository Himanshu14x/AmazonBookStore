package com.DAO;

import com.DB.DynamoDBClientProvider;
import com.entity.BookDetails;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BookDAOImpl implements BookDAO {

    // keep field for compatibility, but may be null -> we fall back to provider
    private DynamoDbClient dynamo;
    private final String BOOKS_TABLE = "Books";

    // keep constructor that accepts a client (for DI), but it may be null
    public BookDAOImpl(DynamoDbClient dynamo) {
        this.dynamo = dynamo;
    }

    // convenience no-arg constructor that always uses provider
    public BookDAOImpl() {
        this.dynamo = null;
    }

    @Override
    public boolean addBooks(BookDetails b) {
        try {
            DynamoDbClient client = (this.dynamo != null) ? this.dynamo : com.DB.DynamoDBClientProvider.getClient();

            // Ensure partition key "bookId" exists (table's key is "bookId" per describe-table)
            String bookId = b.getId(); // assuming your entity uses getId(); adjust if it's getBookId()
            if (bookId == null || bookId.trim().isEmpty()) {
                bookId = UUID.randomUUID().toString();
                System.out.println("BookDAOImpl: generated bookId = " + bookId);
            }

            Map<String, AttributeValue> item = new HashMap<>();
            item.put("bookId", AttributeValue.builder().s(bookId).build());

            if (b.getTitle() != null) item.put("title", AttributeValue.builder().s(b.getTitle()).build());
            if (b.getAuthor() != null) item.put("author", AttributeValue.builder().s(b.getAuthor()).build());
            if (b.getGenre() != null) item.put("genre", AttributeValue.builder().s(b.getGenre()).build());
            if (b.getPhoto() != null) item.put("photo", AttributeValue.builder().s(b.getPhoto()).build());
            if (b.getEmail() != null) item.put("email", AttributeValue.builder().s(b.getEmail()).build());

            if (b.getRating() != null && !b.getRating().trim().isEmpty()) {
                try {
                    Double.parseDouble(b.getRating());
                    item.put("rating", AttributeValue.builder().n(b.getRating()).build());
                } catch (NumberFormatException nfe) {
                    item.put("rating", AttributeValue.builder().s(b.getRating()).build());
                }
            }
            if (b.getPrice() != null && !b.getPrice().trim().isEmpty()) {
                try {
                    Double.parseDouble(b.getPrice());
                    item.put("price", AttributeValue.builder().n(b.getPrice()).build());
                } catch (NumberFormatException nfe) {
                    item.put("price", AttributeValue.builder().s(b.getPrice()).build());
                }
            }

            PutItemRequest request = PutItemRequest.builder()
                    .tableName(BOOKS_TABLE)
                    .item(item)
                    .build();

            // Debug print so you can verify keys being sent
            System.out.println("BookDAOImpl: putting item -> " + item);

            client.putItem(request);
            return true;
        } catch (DynamoDbException e) {
            e.printStackTrace();
            return false;
        }
    }
}
