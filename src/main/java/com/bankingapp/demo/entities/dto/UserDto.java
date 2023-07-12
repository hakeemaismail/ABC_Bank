package com.bankingapp.demo.entities.dto;


import com.bankingapp.demo.entities.Accounts;
import com.bankingapp.demo.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class UserDto {
    
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    
    private List<Role> roles;
    private List<Accounts> accounts;

}
