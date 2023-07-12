package com.bankingapp.demo.service;


import com.bankingapp.demo.entities.Accounts;
import com.bankingapp.demo.entities.User;
import com.bankingapp.demo.entities.dto.UserCreationDto;
import com.bankingapp.demo.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public User createFromDto(UserCreationDto userCreationDto) {
        User user = new User();
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.map(userCreationDto, user);
        user.setPassword(passwordEncoder.encode(userCreationDto.getPassword()));

        return userRepository.save(user);

    }

    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    public void deleteUser(long id){
        userRepository.deleteById(id);
    }

   public User findUserByEmail(String username) {
        return userRepository.findByEmail(username).get();
   }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public String clearAccountsOfUser(long userID){
        User user = userRepository.findById(userID).get();
        user.setAccounts(new ArrayList<>());
        userRepository.save(user);
        return "cleared";
    }

}
