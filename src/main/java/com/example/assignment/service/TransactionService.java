// TransactionService.java
package com.example.assignment.service;

import com.example.assignment.model.Transaction;
import com.example.assignment.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction createTransaction(Long accountId, double amount) {
        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setAmount(amount);
        transaction.setDate(new Date());

        return transactionRepository.save(transaction);
    }
}
