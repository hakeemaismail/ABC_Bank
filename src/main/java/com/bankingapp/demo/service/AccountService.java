package com.bankingapp.demo.service;

import com.bankingapp.demo.entities.Accounts;
import com.bankingapp.demo.entities.User;
import com.bankingapp.demo.entities.dto.UserDto;
import com.bankingapp.demo.repositories.UserRepository;
import com.bankingapp.demo.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accRepo;

    @Autowired
    private UserRepository userRepo;

    public User createAccount(User user, Accounts account) {
        List<Accounts> accounts = user.getAccounts();
        accounts.add(account);
        user.setAccounts(accounts);
        return userRepo.save(user);
    }

    public List<Accounts> getAllAccounts() {

        return accRepo.findAll();
    }

    public List<Accounts> getAccountsOfASpecificUser(User user){
        List<Accounts> accounts = user.getAccounts();
        return accounts;
    }

    public List<Accounts> getAccountsOfAUser(long userID){
        User user = userRepo.findById(userID).get();
        List<Accounts> accounts = user.getAccounts();
        return accounts;
    }
    public Optional<Accounts> findById(long id) {
        return accRepo.findById(id);
    }

    public void deleteAccount(long id){
        accRepo.deleteById(id);
    }

    public void clearAccount(long accID){
        Accounts account = accRepo.findById(accID).get();
        account.setAccountBalance(0);
        account.setTransaction(new ArrayList<>());
        accRepo.save(account);
    }



}
