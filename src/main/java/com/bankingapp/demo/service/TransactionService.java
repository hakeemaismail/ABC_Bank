package com.bankingapp.demo.service;

import com.bankingapp.demo.demo.TransactionController;
import com.bankingapp.demo.entities.Accounts;
import com.bankingapp.demo.entities.TransType;
import com.bankingapp.demo.entities.Transaction;
import com.bankingapp.demo.entities.User;
import com.bankingapp.demo.repositories.AccountRepository;
import com.bankingapp.demo.repositories.TransactionRepository;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.dsig.Transform;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountService accountService;
    public Accounts depositAmount(long accID, long amount){
        Accounts account = accountService.findById(accID).orElseThrow(() -> new RuntimeException("Account not found"));
        Transaction transaction = new Transaction();
        account.setAccountBalance(account.getAccountBalance() + amount);
        long balance = account.getAccountBalance();
        transaction.setType(TransType.DEPOSIT);
        transaction.setBalance(balance);

        List<Transaction> transactionList = account.getTransaction();
        transactionList.add(transaction);
        account.setTransaction(transactionList);
       transactionRepository.save(transaction);
       return accountRepository.save(account);

    }

    public Accounts withdrawAmount(long accID, long amount){
        Accounts account = accountService.findById(accID).orElseThrow(() -> new RuntimeException("Account not found"));
        Transaction transaction = new Transaction();

        if(amount>account.getAccountBalance()){
            return new Accounts();
        }
        else {
            account.setAccountBalance(account.getAccountBalance() - amount);
            long balance = account.getAccountBalance();
            transaction.setType(TransType.WITHDRAWAL);
            transaction.setBalance(balance);

            List<Transaction> transactionList = account.getTransaction();
            transactionList.add(transaction);
            account.setTransaction(transactionList);
            transactionRepository.save(transaction);
            return accountRepository.save(account);
        }

    }

    public String transferAmount(Transaction transaction){
       long amount = transaction.getTransferAmount();
       long sourceAccountID = transaction.getSourceAccountID();
       Accounts sourceAccount = accountService.findById(sourceAccountID).orElseThrow(() -> new RuntimeException("Account not found"));
       long destAccountID = transaction.getDestAccountID();
       Accounts destAccount = accountService.findById(destAccountID).orElseThrow(() -> new RuntimeException("Account not found"));
       long sourceBalance  = sourceAccount.getAccountBalance();
       long destBalance = destAccount.getAccountBalance();

       if(amount>sourceBalance){
            return "Not enough balance";
       }
       else{
          long newSourceBalance = sourceBalance - amount;
          long newDestBalance = destBalance + amount;
          sourceAccount.setAccountBalance(newSourceBalance);
          destAccount.setAccountBalance(newDestBalance);

          //create transaction records for source and destination accounts
           Transaction sourceTransaction = new Transaction();
           Transaction destTransaction = new Transaction();
           sourceTransaction.setBalance(newSourceBalance);
           sourceTransaction.setType(TransType.TRANSFER);
           sourceTransaction.setTransferAmount(amount);
           sourceTransaction.setSourceAccountID(sourceAccountID);
           sourceTransaction.setDestAccountID(destAccountID);

           destTransaction.setType(TransType.DEPOSIT);
           destTransaction.setBalance(newDestBalance);
           destTransaction.setTransferAmount(amount);
           destTransaction.setSourceAccountID(sourceAccountID);
           destTransaction.setDestAccountID(destAccountID);

           List<Transaction> transactionSourceList = sourceAccount.getTransaction();
           transactionSourceList.add(sourceTransaction);
           sourceAccount.setTransaction(transactionSourceList);
           transactionRepository.save(sourceTransaction);
           accountRepository.save(sourceAccount);

           List<Transaction> transactionDestList = destAccount.getTransaction();
           transactionDestList.add(destTransaction);
           destAccount.setTransaction(transactionDestList);
           transactionRepository.save(destTransaction);
           accountRepository.save(destAccount);

           return "transfer successful";
       }

    }

    public List<Transaction> viewTransactionsOfASpecificUser(long accID){
       Optional<Accounts> account = accountService.findById(accID);
       return account.get().getTransaction();
    }

    public void generateUserListPdf(HttpServletResponse response, long accID) throws IOException {

        Document document = new Document(PageSize.A4);

        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());


        document.open();

        Font textFont = FontFactory.getFont(FontFactory.COURIER_BOLD);
        textFont.setSize(14);
        textFont.setColor(Color.BLUE);

        Font tableHeaderFont = FontFactory.getFont(FontFactory.TIMES_BOLD);
        textFont.setSize(16);

        PdfPTable table = new PdfPTable(7);

        table.setWidthPercentage(100f);

        table.setSpacingBefore(10);

        PdfPCell headerCell = new PdfPCell();

        headerCell.setPhrase(new Phrase("Transaction ID", tableHeaderFont));
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setBackgroundColor(Color.LIGHT_GRAY);

        table.addCell(headerCell);


        headerCell.setPhrase(new Phrase("Date", tableHeaderFont));
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setBackgroundColor(Color.LIGHT_GRAY);

        table.addCell(headerCell);


        headerCell.setPhrase(new Phrase("Transaction Type", tableHeaderFont));
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setBackgroundColor(Color.LIGHT_GRAY);

        table.addCell(headerCell);


        headerCell.setPhrase(new Phrase("Source Account", tableHeaderFont));
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setBackgroundColor(Color.LIGHT_GRAY);

        table.addCell(headerCell);


        headerCell.setPhrase(new Phrase("Destination Account", tableHeaderFont));
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setBackgroundColor(Color.LIGHT_GRAY);

        table.addCell(headerCell);


        headerCell.setPhrase(new Phrase("Transfer Amount", tableHeaderFont));
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setBackgroundColor(Color.LIGHT_GRAY);

        table.addCell(headerCell);


        headerCell.setPhrase(new Phrase("Account Balance", tableHeaderFont));
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setBackgroundColor(Color.LIGHT_GRAY);

        table.addCell(headerCell);

        List<Transaction> transactionList = viewTransactionsOfASpecificUser(accID);


        for(Transaction transaction : transactionList){

            table.addCell(String.valueOf(transaction.getTransID()));
            table.addCell(String.valueOf(transaction.getDate()));
            table.addCell(String.valueOf(transaction.getType()));
            table.addCell(String.valueOf(transaction.getSourceAccountID()));
            table.addCell(String.valueOf(transaction.getDestAccountID()));
            table.addCell(String.valueOf(transaction.getTransferAmount()));
            table.addCell(String.valueOf(transaction.getBalance()));

        }

        document.add(table);

        document.close();
        
        writer.close();


    }
}
