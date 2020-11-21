package com.example.Repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	 User findByEmail(String email);
	 @Transactional
	 @Modifying
	 @Query(value="update User u set u.username = :username,u.email = :email where u.id = :id")
	 void updateUser(@Param("id") Long id, @Param("username") String username, @Param("email") String email);


}
