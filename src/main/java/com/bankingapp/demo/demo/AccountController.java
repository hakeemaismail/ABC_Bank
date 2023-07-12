package com.bankingapp.demo.demo;

import com.bankingapp.demo.config.JwtService;
import com.bankingapp.demo.entities.Accounts;
import com.bankingapp.demo.entities.User;
import com.bankingapp.demo.repositories.AccountRepository;
import com.bankingapp.demo.service.AccountService;
import com.bankingapp.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "*", maxAge=3600)
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/")
public class AccountController {

    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    JwtService jwtService;

    @PostMapping("/createAccount")
    public User createAccount (HttpServletRequest req, @RequestBody Accounts account){

        try{
            User user = jwtService.getAuthUser(req);

//            System.out.println(user.toString());

            return accountService.createAccount(user, account);
        }
        catch (Exception e){
            return new User();
        }
    }
    @GetMapping("/getAccounts")
    public List<Accounts> getAllAccounts () {
        return accountService.getAllAccounts();
    }

    @GetMapping("/getAccountsOfASpecificUser")
    public List<Accounts> getAllAccountsOfSpecificUser(HttpServletRequest req){
        User user = jwtService.getAuthUser(req);
        return accountService.getAccountsOfASpecificUser(user);
    }

    @GetMapping("/getAccountsOfAUser/{id}")
    public List<Accounts> getAccountsOfAUser(@PathVariable long id){
        return accountService.getAccountsOfAUser(id);
    }
    @GetMapping("/Accounts/{id}")
    public Accounts getAccountByAccountId (@PathVariable long id){

        return accountService.findById(id).get();

    }

    @DeleteMapping("/deleteAccount/{id}")
    public void deleteAccount(@PathVariable long id){
        accountService.deleteAccount(id);
    }

    @PostMapping("/clearAccount/{id}")
    public void clearAccount(@PathVariable long id){
        accountService.clearAccount(id);
    }


}
