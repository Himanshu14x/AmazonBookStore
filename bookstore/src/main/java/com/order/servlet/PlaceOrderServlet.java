package com.order.servlet;

import com.DAO.OrderDAOImpl;
import com.DAO.userDAOImpl;
import com.DB.DynamoDBClientProvider;
import com.entity.Order;
import com.entity.User;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

@WebServlet("/order/place")
public class PlaceOrderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userobj") == null) {
            resp.sendRedirect(req.getContextPath()+"/login.jsp"); return;
        }
        User u = (User) session.getAttribute("userobj");
        DynamoDbClient client = DynamoDBClientProvider.getClient();
        userDAOImpl udao = new userDAOImpl(client);
        Map<String, String> cart = udao.getCart(u.getEmail());
        if (cart == null || cart.isEmpty()) {
            session.setAttribute("failedMsg", "Cart empty"); resp.sendRedirect(req.getContextPath()+"/cart.jsp"); return;
        }

        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString());
        order.setEmail(u.getEmail());
        order.setCreatedAt(Instant.now().toString());
        order.setStatus("PLACED");
        List<Map<String, String>> items = new ArrayList<>();
        double total = 0.0;
        com.DAO.BookDAOImpl bdao = new com.DAO.BookDAOImpl(client);

        for (Map.Entry<String, String> e : cart.entrySet()) {
            String bookId = e.getKey();
            String qtyStr = e.getValue();
            int qty = 1;
            try { qty = Integer.parseInt(qtyStr); } catch (Exception ignored) {}
            com.entity.BookDetails bd = bdao.getBookById(bookId);
            double price = 0.0;
            try { if (bd != null && bd.getPrice()!=null) price = Double.parseDouble(bd.getPrice()); } catch(Exception ignored){}
            Map<String, String> it = new HashMap<>();
            it.put("bookId", bookId);
            it.put("qty", Integer.toString(qty));
            it.put("price", Double.toString(price));
            items.add(it);
            total += price * qty;
        }
        order.setItems(items); order.setTotal(total);

        OrderDAOImpl od = new OrderDAOImpl(client);
        boolean ok = od.placeOrder(order);
        if (ok) {
            udao.clearCart(u.getEmail());
            session.setAttribute("successMsg", "Order placed: " + order.getOrderId());
        } else {
            session.setAttribute("failedMsg", "Order failed");
        }
        resp.sendRedirect(req.getContextPath() + "/orders.jsp");
    }
}
