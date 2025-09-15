package com.DAO;

import com.entity.Order;
import java.util.List;

public interface OrderDAO {
    boolean placeOrder(Order o);
    List<Order> getOrdersByEmail(String email);
    List<Order> getAllOrders();
}
