package com.DAO;

import java.util.Map;
import java.util.List;


public interface CartDAO {
   
    Map<String, String> getCart(String email);

 
    boolean addToCart(String email, String bookId, int qtyToAdd);

  
    boolean updateCartItem(String email, String bookId, int qty);

    boolean removeFromCart(String email, String bookId);

    boolean clearCart(String email);
}
