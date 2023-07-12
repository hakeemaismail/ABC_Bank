package com.bankingapp.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Accounts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long accountID;
    private AccountType type = AccountType.SAVINGS;
    private long accountBalance;


    @OneToMany(cascade = CascadeType.ALL)
    private List<Transaction> transaction;


}
