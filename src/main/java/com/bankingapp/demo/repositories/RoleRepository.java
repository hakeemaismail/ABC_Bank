package com.bankingapp.demo.repositories;


import com.bankingapp.demo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String role_employee);
}
