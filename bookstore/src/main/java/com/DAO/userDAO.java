package com.DAO;

import com.entity.User;

public interface userDAO {
boolean userRegister(User user);


User login(String email, String password);
}
