package com.order.servlet;

import com.DAO.OrderDAOImpl;
import com.DAO.CartDAOImpl;
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
        CartDAOImpl cartDao = new CartDAOImpl(client);
        Map<String, String> cart = cartDao.getCart(u.getEmail());
        if (cart == null || cart.isEmpty()) {
            session.setAttribute("failedMsg", "Cart empty"); resp.sendRedirect(req.getContextPath()+"/cart.jsp"); return;
        }

        // read address/payment params from form (fallback to user's saved values)
        String phoneNumber = req.getParameter("phoneNumber");
        String address = req.getParameter("address");
        String landmark = req.getParameter("landmark");
        String city = req.getParameter("city");
        String pincode = req.getParameter("pincode");
        String paymentMethod = req.getParameter("paymentMethod");

        if ((phoneNumber == null || phoneNumber.trim().isEmpty()) && u.getPhoneNumber() != null) phoneNumber = u.getPhoneNumber();
        if ((address == null || address.trim().isEmpty()) && u.getAddress() != null) address = u.getAddress();
        if ((landmark == null || landmark.trim().isEmpty()) && u.getLandmark() != null) landmark = u.getLandmark();
        if ((city == null || city.trim().isEmpty()) && u.getCity() != null) city = u.getCity();
        if ((pincode == null || pincode.trim().isEmpty()) && u.getPincode() != null) pincode = u.getPincode();
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) paymentMethod = "COD";

        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase());
        order.setEmail(u.getEmail());
        order.setCreatedAt(Instant.now().toString());
        order.setStatus("PLACED");
        order.setPhoneNumber(phoneNumber);
        order.setAddress(address);
        order.setLandmark(landmark);
        order.setCity(city);
        order.setPincode(pincode);
        order.setPaymentMethod(paymentMethod);

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
        order.setItems(items);
        order.setTotal(total);

        OrderDAOImpl od = new OrderDAOImpl(client);
        boolean ok = od.placeOrder(order);
        if (ok) {
            // clear cart
            cartDao.clearCart(u.getEmail());

            // update session user object with these address fields (so next time form is pre-filled)
            u.setPhoneNumber(phoneNumber);
            u.setAddress(address);
            u.setLandmark(landmark);
            u.setCity(city);
            u.setPincode(pincode);
            session.setAttribute("userobj", u);

         
        } else {
            session.setAttribute("failedMsg", "Order failed");
        }
        resp.sendRedirect(req.getContextPath() + "/orders.jsp");
    }
}
