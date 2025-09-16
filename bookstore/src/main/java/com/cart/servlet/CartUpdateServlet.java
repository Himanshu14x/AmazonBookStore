package com.cart.servlet;

import com.DAO.CartDAO;
import com.DAO.CartDAOImpl;
import com.DB.DynamoDBClientProvider;
import com.entity.User;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Accepts POST params:
 *   - bookId  (String)
 *   - qty     (int)  (0 = remove)
 *
 * Updates the cart via CartDAOImpl.updateCartItem(...) and redirects back to the referrer
 * (or cart.jsp if referrer missing).
 */
@WebServlet("/cart/update")
public class CartUpdateServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = null;
        if (session != null) user = (User) session.getAttribute("userobj");

        if (user == null) {
            // not logged in -> go to login
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String bookId = req.getParameter("bookId");
        String qtyStr = req.getParameter("qty");
        int qty = 0;
        try { qty = Integer.parseInt(qtyStr); } catch (Exception ignored) {}

        if (bookId == null || bookId.trim().isEmpty()) {
            session.setAttribute("failedMsg", "Invalid product id");
            resp.sendRedirect(req.getContextPath() + "/cart.jsp");
            return;
        }

        try {
            DynamoDbClient client = DynamoDBClientProvider.getClient();
            CartDAO cartDao = new CartDAOImpl(client);
            boolean ok = cartDao.updateCartItem(user.getEmail(), bookId, qty); // qty=0 removes
           
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("failedMsg", "Error updating cart");
        }

        // safe redirect back to where request came from (so +/- buttons keep user on cart.jsp)
        String referer = req.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            resp.sendRedirect(referer);
        } else {
            resp.sendRedirect(req.getContextPath() + "/cart.jsp");
        }
    }

    // optional: accept GET too (redirect to cart)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(req.getContextPath() + "/cart.jsp");
    }
}
