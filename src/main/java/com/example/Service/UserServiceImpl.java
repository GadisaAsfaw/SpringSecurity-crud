package com.example.Service;

import java.util.Arrays;
import java.util.HashSet;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Model.Role;
import com.example.Model.User;
import com.example.Repository.RoleRepository;
import com.example.Repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
    private RoleRepository roleRepository;
	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	

	private JdbcTemplate jdbc;
	@Autowired
	public UserServiceImpl(JdbcTemplate jdbc) {
		this.jdbc =jdbc;
	}
   
	
	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
		
	}

	@Override
	public void saveUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        Long uno = userRepository.count();
        Role userRole; 
        if(uno==0) {
        	userRole= roleRepository.findByRole("ADMIN");
        }else {  userRole = roleRepository.findByRole("EMP");
        }
        
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		userRepository.save(user);
		
		
		
	}

		
	
	public void deleteFromUserRole(Long uid) {
		jdbc.update("delete from user_role where user_id =?",uid);
		
	}

}
