package com.cart.servlet;

import com.DAO.userDAOImpl;
import com.DAO.CartDAOImpl;
import com.DB.DynamoDBClientProvider;
import com.entity.User;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/cart/add")
public class CartAddServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = null;
        if (session != null) user = (User) session.getAttribute("userobj");

        if (user == null) {
            // redirect to login if not logged in
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String productId = req.getParameter("bookId");
        String qtyStr = req.getParameter("qty");
        int qty = 1;
        try { qty = Integer.parseInt(qtyStr); } catch (Exception ignored) {}

        try {
            DynamoDbClient client = DynamoDBClientProvider.getClient();
            CartDAOImpl udao = new CartDAOImpl(client);
            boolean ok = udao.addToCart(user.getEmail(), productId, qty);
            // optional: set success message in session
            if (ok) session.setAttribute("successMsg", "");
            else session.setAttribute("failedMsg", "Could not add to cart");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("failedMsg", "Error adding to cart");
        }

        // Redirect back to cart or product page
        resp.sendRedirect(req.getContextPath() + "/cart.jsp");
    }
}
