package com.bankingapp.demo.entities;

import com.bankingapp.demo.service.AccountService;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Entity
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long transID;
    private Date date = new Date();
    private long balance;
    private TransType type;
    private long sourceAccountID;
    private long destAccountID;
    private long transferAmount;

//    public long deposit(long amount){
//      balance = balance + amount;
//      return balance;
//    }
//
//    public long withdrawal(long amount){
//        if(amount>balance){
//            return -1;
//        }
//        else {
//            balance = balance - amount;
//            return balance;
//        }
//    }
//    public long updateBalance(){
//
//        return balance;
//    }

}
