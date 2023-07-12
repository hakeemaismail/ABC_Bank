package com.bankingapp.demo.demo;

import com.bankingapp.demo.entities.Accounts;
import com.bankingapp.demo.entities.Transaction;
import com.bankingapp.demo.service.TransactionService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/depositAmount/{amount}/accountID/{accID}")
    public Accounts depositAmount(@PathVariable long accID, @PathVariable long amount){
        return transactionService.depositAmount(accID,amount);
    }

    @PostMapping("/withdrawAmount/{amount}/accountID/{accID}")
    public Accounts withdrawAmount(@PathVariable long accID, @PathVariable long amount){
        return transactionService.withdrawAmount(accID,amount);
    }

    @PostMapping("/transferAmount")
    public String transferAmount(@RequestBody Transaction transaction){
        transactionService.transferAmount(transaction);
        return "transaction successful";
    }

    @GetMapping("/viewTransactionsOfASpecificUser/{accID}")
    public List<Transaction> viewTransactionsOfASpecificUser(@PathVariable long accID){
        return transactionService.viewTransactionsOfASpecificUser(accID);
    }

    @GetMapping("/generatePDF/{accID}")
    public void generatePDF(HttpServletResponse response, @PathVariable long accID){
        response.setContentType("application/pdf");

        try{
            transactionService.generateUserListPdf(response, accID);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
