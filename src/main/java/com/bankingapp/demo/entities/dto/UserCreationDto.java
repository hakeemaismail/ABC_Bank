package com.bankingapp.demo.entities.dto;

import com.bankingapp.demo.model.Role;
import lombok.Data;

import java.util.List;

@Data
public class UserCreationDto {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private List<Role> roles;
}
