package com.bankingapp.demo.demo;

import com.bankingapp.demo.config.JwtService;
import com.bankingapp.demo.entities.ResponseWrapper;
import com.bankingapp.demo.entities.User;
import com.bankingapp.demo.entities.dto.UserCreationDto;
import com.bankingapp.demo.model.Role;
import com.bankingapp.demo.repositories.RoleRepository;
import com.bankingapp.demo.repositories.UserRepository;
import com.bankingapp.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/auth")
public class UserController {

    @Autowired
    UserService service;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    JwtService jwtService;

    @PostMapping("/dto")
    public ResponseWrapper createFromDto (@RequestBody UserCreationDto userCreationDto) {
        try{
            String email = userCreationDto.getEmail();
            Optional<User> existingUser = userRepository.findByEmail(email);
            //System.out.println(existingUser);

          if(existingUser.isEmpty()){

            List<Role> userRole = new ArrayList<>();
            Role role = roleRepository.findByName("User");
            userRole.add(role);
            userCreationDto.setRoles(userRole);
            User user = service.createFromDto(userCreationDto);

            //User user = service.createFromDto(userCreationDto);
            return new ResponseWrapper<>(user, "success", "created");}
          else {
              return new ResponseWrapper<>(null, "exists", "already exists");
          }

        }catch (Exception e){
            return new ResponseWrapper<>(null, "failed", e.getMessage());
        }
    }

    @PostMapping("/createEmployee")
    public ResponseWrapper createEmployee (@RequestBody UserCreationDto userCreationDto) {
        try{
            List<Role> userRole = new ArrayList<>();
            Role role = roleRepository.findByName("Employee");
            userRole.add(role);
            userCreationDto.setRoles(userRole);
            User user = service.createFromDto(userCreationDto);


            //User user = service.createFromDto(userCreationDto);
            return new ResponseWrapper<>(user, "success", "created");
        }catch (Exception e){
            return new ResponseWrapper<>(null, "failed", e.getMessage());
        }
    }

    @PostMapping("/create")
    public User createUser(@RequestBody User user) {

        return service.createUser(user);
    }
    @GetMapping("/getUsers")
    public List<User> getAllUsers () {
        return service.getAllUsers();
    }

    @DeleteMapping("/deleteUser/{id}")
    public void deleteUser(@PathVariable long id){
        service.deleteUser(id);
    }

    @PostMapping("/clearBankAccounts/user/{id}")
    public void clearAccountOfUser(@PathVariable long id){
        service.clearAccountsOfUser(id);
    }


      }
