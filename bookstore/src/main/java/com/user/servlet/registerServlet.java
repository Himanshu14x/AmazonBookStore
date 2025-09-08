package com.user.servlet;

import java.io.IOException;

import com.DAO.userDAOImpl;
import com.DB.DBConnect;
import com.entity.User;

import jakarta.servlet.Registration;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/register")
public class registerServlet extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String name = req.getParameter("name");
			String email = req.getParameter("email");
			String password = req.getParameter("password");
//			System.out.println(name+" "+email+" "+password);
			
			User user = new User();
			user.setName(name);
			user.setEmail(email);
			user.setPassword(password);
			
			HttpSession session = req.getSession();
//			
			userDAOImpl dao = new userDAOImpl(DBConnect.getConnection());
			boolean success = dao.userRegister(user);
			
			if(success) {
				session.setAttribute("SuccessMessage", "Registration Successful!");
				resp.sendRedirect("/register.jsp");
//				System.out.println("User Register Success!!");
			} else {
//				System.out.println("Something went wrong");
				session.setAttribute("Error", "Server down...");
				resp.sendRedirect("/register.jsp");
			}
//			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	

}
