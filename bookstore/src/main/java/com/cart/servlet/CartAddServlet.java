package com.cart.servlet;

import com.entity.User;
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

        // If not logged in, redirect to login page. Optionally save target to return after login.
        if (user == null) {
            // Save attempted action so you can redirect back after login (optional)
            String redirectAfter = req.getContextPath() + "/view_books.jsp?bid=" + req.getParameter("productId");
            req.getSession(true).setAttribute("afterLoginRedirect", redirectAfter);

            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        // At this point user is authenticated — handle add to cart logic
        String productId = req.getParameter("productId");
        String qtyStr = req.getParameter("qty");
        int qty = 1;
        try { qty = Integer.parseInt(qtyStr); } catch (Exception ignored) {}

        // TODO: your existing cart logic here (add item to session/cart table)
        // Example: Cart cart = (Cart) session.getAttribute("cart"); cart.add(productId, qty);

        // Redirect back to cart or product page
        resp.sendRedirect(req.getContextPath() + "/cart.jsp"); // or wherever you want
    }
}
