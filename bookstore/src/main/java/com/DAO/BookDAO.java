package com.DAO;


import java.util.List;

import com.entity.BookDetails;

public interface BookDAO {

	
	
	boolean addBooks(BookDetails b);
	List<BookDetails> getAllBooks();
}
