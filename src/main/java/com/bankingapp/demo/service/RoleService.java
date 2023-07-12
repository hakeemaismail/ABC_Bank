package com.bankingapp.demo.service;


import com.bankingapp.demo.model.Role;
import com.bankingapp.demo.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> findAll (){
        return roleRepository.findAll();
    }

    public Role create (String roleName){

        Role role = new Role(roleName);
        return roleRepository.save(role);
    }
}
