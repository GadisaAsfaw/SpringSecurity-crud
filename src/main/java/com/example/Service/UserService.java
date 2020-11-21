package com.example.Service;


import com.example.Model.User;

public interface UserService {
	public User findUserByEmail(String email);
	public void saveUser(User user);
	

}
