package com.bankingapp.demo.auth;

import com.bankingapp.demo.entities.User;
import com.bankingapp.demo.entities.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class AuthenticationResponse {
    private String token;
    private UserDto user;

}
