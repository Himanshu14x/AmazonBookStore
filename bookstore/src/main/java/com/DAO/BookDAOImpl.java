package com.DAO;

import com.DB.DynamoDBClientProvider;
import com.entity.BookDetails;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import java.time.Instant;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BookDAOImpl implements BookDAO {

  
    private DynamoDbClient dynamo;
    private final String BOOKS_TABLE = "amazonbooks";

 
    public BookDAOImpl(DynamoDbClient dynamo) {
        this.dynamo = dynamo;
    }

  
    public BookDAOImpl() {
        this.dynamo = null;
    }

    @Override
    public boolean addBooks(BookDetails b) {
        try {
            DynamoDbClient client = (this.dynamo != null) ? this.dynamo : com.DB.DynamoDBClientProvider.getClient();

           
            String bookId = b.getId(); 
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
                item.put("rating", AttributeValue.builder().s(b.getRating()).build());
            }
            if (b.getPrice() != null && !b.getPrice().trim().isEmpty()) {
                item.put("price", AttributeValue.builder().s(b.getPrice()).build());
            }
            item.put("createdAt", AttributeValue.builder().s(Instant.now().toString()).build());


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

    @Override
    public List<BookDetails> getAllBooks() {
        List<BookDetails> books = new java.util.ArrayList<>();

        try {
            DynamoDbClient client = (this.dynamo != null) ? this.dynamo : com.DB.DynamoDBClientProvider.getClient();

            // Build a scan request for the amazonbooks table
            software.amazon.awssdk.services.dynamodb.model.ScanRequest scanRequest =
                    software.amazon.awssdk.services.dynamodb.model.ScanRequest.builder()
                            .tableName(BOOKS_TABLE)
                            .build();

            software.amazon.awssdk.services.dynamodb.model.ScanResponse response = client.scan(scanRequest);

            for (Map<String, AttributeValue> item : response.items()) {
                BookDetails book = new BookDetails();

                // Map each attribute back into your entity
                if (item.containsKey("bookId")) book.setId(item.get("bookId").s());
                if (item.containsKey("title")) book.setTitle(item.get("title").s());
                if (item.containsKey("author")) book.setAuthor(item.get("author").s());
                if (item.containsKey("genre")) book.setGenre(item.get("genre").s());
                if (item.containsKey("photo")) book.setPhoto(item.get("photo").s());
                if (item.containsKey("email")) book.setEmail(item.get("email").s());
                if (item.containsKey("rating")) book.setRating(item.get("rating").s());
                if (item.containsKey("price")) book.setPrice(item.get("price").s());


                books.add(book);
            }

        } catch (DynamoDbException e) {
            e.printStackTrace();
        }

        return books;
    }

	@Override
	public BookDetails getBookById(String id) {
		
		  BookDetails bookDetails = null;

		    try {
		    	

		        Map<String, AttributeValue> key = Map.of(
		            "bookId", AttributeValue.builder().s(id).build()
		        );

		        GetItemRequest getReq = GetItemRequest.builder()
		                .tableName(BOOKS_TABLE)
		                .key(key)
		                .build();

		        GetItemResponse resp = dynamo.getItem(getReq);

		        Map<String, AttributeValue> item = resp.item();
		        if (item == null || item.isEmpty()) {
		            return null; // not found
		        }

		     
		        bookDetails = new BookDetails();
		        // set the ID (use same attribute name you used above)
		        bookDetails.setId(item.containsKey("bookId") ? item.get("bookId").s() : id);

		        if (item.containsKey("title")) bookDetails.setTitle(item.get("title").s());
		        if (item.containsKey("author")) bookDetails.setAuthor(item.get("author").s());
		        if (item.containsKey("genre")) bookDetails.setGenre(item.get("genre").s());
		        if (item.containsKey("photo")) bookDetails.setPhoto(item.get("photo").s());
		        if (item.containsKey("email")) bookDetails.setEmail(item.get("email").s());
		        if (item.containsKey("rating")) bookDetails.setRating(item.get("rating").s());
		        if (item.containsKey("price")) bookDetails.setPrice(item.get("price").s());
		 

		    } catch (DynamoDbException e) {
		        e.printStackTrace();
		        // handle or rethrow as needed
		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return bookDetails;
    
    
	}


	@Override

	public List<BookDetails> getNewBook() {
	    List<BookDetails> books = new ArrayList<>();

	    try {
	        DynamoDbClient client = (this.dynamo != null) ? this.dynamo : DynamoDBClientProvider.getClient();

	        // Scan all items
	        software.amazon.awssdk.services.dynamodb.model.ScanRequest scanRequest =
	                software.amazon.awssdk.services.dynamodb.model.ScanRequest.builder()
	                        .tableName(BOOKS_TABLE)
	                        .build();

	        software.amazon.awssdk.services.dynamodb.model.ScanResponse response = client.scan(scanRequest);

	        for (Map<String, AttributeValue> item : response.items()) {
	            BookDetails book = new BookDetails();
	            if (item.containsKey("bookId")) book.setId(item.get("bookId").s());
	            if (item.containsKey("title")) book.setTitle(item.get("title").s());
	            if (item.containsKey("author")) book.setAuthor(item.get("author").s());
	            if (item.containsKey("genre")) book.setGenre(item.get("genre").s());
	            if (item.containsKey("photo")) book.setPhoto(item.get("photo").s());
	            if (item.containsKey("email")) book.setEmail(item.get("email").s());
	            if (item.containsKey("rating")) book.setRating(item.get("rating").s());
	            if (item.containsKey("price")) book.setPrice(item.get("price").s());
	            if (item.containsKey("createdAt")) book.setCreatedAt(item.get("createdAt").s());
	            books.add(book);
	        }
	        
	        books.sort((b1, b2) -> b2.getCreatedAt().compareTo(b1.getCreatedAt()));

	        // Take top 8
	        return books.size() > 8 ? books.subList(0, 8) : books;
	       

	    } catch (DynamoDbException e) {
	        e.printStackTrace();
	    }

	    return books; // return whatever we got
	}

	
	
	
	
}
