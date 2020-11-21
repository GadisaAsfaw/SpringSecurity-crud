package com.example.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	Role findByRole(String role);

}
