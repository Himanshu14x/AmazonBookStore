package com.user.servlet;

import java.io.IOException;


import com.DAO.userDAOImpl;
import com.DB.DynamoDBClientProvider;
import com.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class loginServlet extends HttpServlet {
	private String emailString = "himanshu140198@gmail.com";
	private String passwordString = "123@";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			userDAOImpl dao = new userDAOImpl(DynamoDBClientProvider.getClient());
			HttpSession session = req.getSession();
			String email=req.getParameter("email");
			String password = req.getParameter("password");
			
			if(emailString.equals(email) && passwordString.equals(password)) {
				User user = new User();
				user.setName("Admin");
				
				session.setAttribute("userobj", user);
				resp.sendRedirect("admin/home.jsp");
				
			} else {
				
				User user =dao.login(email, password);
				if(user!=null) {
					session.setAttribute("userobj", user);
					resp.sendRedirect("home.jsp");
					
				} else {
					
					session.setAttribute("failedMsg", "invalid credentials!");
					resp.sendRedirect("login.jsp");
				}
				resp.sendRedirect("home.jsp");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
