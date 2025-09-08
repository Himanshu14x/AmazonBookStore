package com.user.servlet;

import java.io.IOException;

import com.DAO.userDAOImpl;
import com.DB.DBConnect;
import com.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/register")
public class registerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // show the registration form when user visits /register
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String name = req.getParameter("name");
            String email = req.getParameter("email");
            String password = req.getParameter("password");

            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);

            userDAOImpl dao = new userDAOImpl(DBConnect.getConnection());
            boolean success = dao.userRegister(user);

            HttpSession session = req.getSession();
            if (success) {
                session.setAttribute("successMessage", "Registration successful!");
            } else {
                session.setAttribute("errorMessage", "Registration failed. Please try again.");
            }

            // IMPORTANT: redirect and stop — do NOT forward or write after redirect
            resp.sendRedirect(req.getContextPath() + "/register.jsp");
            return;

        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = req.getSession();
            session.setAttribute("errorMessage", "Server error. See logs.");
            resp.sendRedirect(req.getContextPath() + "/register.jsp");
            return;
        }
    }
}
