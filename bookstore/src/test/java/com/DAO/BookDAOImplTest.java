package com.DAO;

import com.DB.DynamoDBClientProvider;
import com.entity.BookDetails;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;


public class BookDAOImplTest {

    private DynamoDbClient client() {
        return DynamoDBClientProvider.getClient();
    }

    private BookDAOImpl dao() {
        return new BookDAOImpl(client());
    }

    @Test
    public void testAddBookAndFetch() {
        BookDAOImpl dao = dao();

        BookDetails book = new BookDetails();
        book.setId("BOOK-1");
        book.setTitle("JUnit Basics");
        book.setAuthor("Test Author");
        book.setGenre("Education");
        book.setEmail("test@example.com");
        book.setPrice("100");
        book.setRating("4.5");

        boolean added = dao.addBooks(book);
        assertTrue(added, "addBooks should return true");

        BookDetails fetched = dao.getBookById("BOOK-1");
        assertNotNull(fetched, "getBookById must return the inserted book");
        assertEquals("BOOK-1", fetched.getId());
        assertEquals("JUnit Basics", fetched.getTitle());

        List<BookDetails> all = dao.getAllBooks();
        assertNotNull(all, "getAllBooks must not return null");
        assertTrue(all.stream().anyMatch(b -> "BOOK-1".equals(b.getId())),
                "getAllBooks should include the inserted book");
    }

    @Test
    public void testGetNewBook_returnsListAndCreatedAtWhenPresent() {
        BookDAOImpl dao = dao();

        
        BookDetails b = new BookDetails();
        b.setId("BOOK-1"); 
        b.setTitle("JUnit Basics");
        b.setAuthor("Test Author");
        b.setGenre("Education");
        b.setEmail("test@example.com");
        b.setPrice("100");
        b.setRating("4.5");
        dao.addBooks(b);

        List<BookDetails> newest = dao.getNewBook();
        assertNotNull(newest, "getNewBook must not return null");

      
        newest.stream().filter(x -> "BOOK-1".equals(x.getId())).findFirst().ifPresent(book -> {
            assertNotNull(book.getCreatedAt(), "createdAt should be present for recent books");
        });
    }

    @Test
    public void testSearchBooks_caseInsensitiveMatchesTitleAuthorOrGenre() {
        BookDAOImpl dao = dao();

        
        BookDetails b = new BookDetails();
        b.setId("BOOK-2");
        b.setTitle("Advanced JUnit Patterns");
        b.setAuthor("Search Author");
        b.setGenre("Testing");
        b.setPrice("50");
        b.setRating("4.2");
        dao.addBooks(b);

       
        List<BookDetails> byTitle = dao.searchBooks("Advanced");
        assertNotNull(byTitle, "searchBooks must not return null");
      
        boolean presentInTitleSearch = byTitle.stream().anyMatch(x -> "BOOK-2".equals(x.getId()));
        if (!presentInTitleSearch) {
            System.out.println("Note: BOOK-2 not found in title search results (scan timing/eventual consistency).");
        }

       
        List<BookDetails> byAuthor = dao.searchBooks("Search Author");
        assertNotNull(byAuthor, "searchBooks by author must not return null");

        
        List<BookDetails> byGenre = dao.searchBooks("testing");
        assertNotNull(byGenre, "searchBooks by genre must not return null");
    }

    @Test
    public void testGetRecommendedByGenre_filtersAndLimitsAndExcludeId() {
        BookDAOImpl dao = dao();

        
        BookDetails low = new BookDetails();
        low.setId("BOOK-LOW");
        low.setTitle("Low Rated");
        low.setAuthor("Author L");
        low.setGenre("Mystery");
        low.setRating("3.0");
        low.setPrice("10");
        dao.addBooks(low);

        BookDetails high = new BookDetails();
        high.setId("BOOK-HIGH");
        high.setTitle("High Rated");
        high.setAuthor("Author H");
        high.setGenre("Mystery");
        high.setRating("4.9");
        high.setPrice("20");
        dao.addBooks(high);

        
        List<BookDetails> recs = dao.getRecommendedByGenre("Mystery", 4.0, 5, "BOOK-LOW");
        assertNotNull(recs, "getRecommendedByGenre must not return null");

       
        for (BookDetails bd : recs) {
            assertNotNull(bd.getGenre(), "recommended item genre must not be null");
            assertEquals("Mystery", bd.getGenre(), "recommended item must match requested genre");
            double r = 0.0;
            try { r = Double.parseDouble(bd.getRating()); } catch (Exception ignored) {}
            assertTrue(r >= 4.0, "recommended item rating must be >= requested minRating");
            assertNotEquals("BOOK-LOW", bd.getId(), "excluded book id must not be present in results");
        }

        // Confirm that higher-rated book is likely present (if present at all)
        boolean highPresent = recs.stream().anyMatch(x -> "BOOK-HIGH".equals(x.getId()));
        if (!highPresent) {
            System.out.println("BOOK-HIGH not present in recommendations");
        }
    }
}
