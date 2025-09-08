package com.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.entity.User;

public class userDAOImpl implements userDAO{
	
	private Connection connection;

	
	public userDAOImpl(Connection connection) {
		super();
		this.connection = connection;
	}


	
	public boolean userRegister(User user) {
		boolean success = false;
		try {
			String sql ="insert into user(name,email,password) values(?,?,?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, user.getName());
			ps.setString(2,user.getEmail());
			ps.setString(3,user.getPassword());
			int rows = ps.executeUpdate();
			if(rows>0) {
				success=true;
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}

}
