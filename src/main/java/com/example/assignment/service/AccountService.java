package com.example.assignment.service;

import com.example.assignment.dto.AccountDTO;

import com.example.assignment.exception.ResourceNotFoundException;
import com.example.assignment.model.Account;
import com.example.assignment.model.Customer;
import com.example.assignment.model.Transaction;
import com.example.assignment.repository.AccountRepository;
import com.example.assignment.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Account createAccount(Long customerId, double initialCredit) {
        Account account = new Account();
        account.setCustomerId(customerId);
        account.setBalance(initialCredit);

        if (initialCredit > 0) {
            Transaction transaction = new Transaction();
            transaction.setAccountId(account.getAccountId());
            transaction.setAmount(initialCredit);
            transaction.setDate(new Date());

            List<Transaction> transactions = new ArrayList<>();
            transactions.add(transaction);
            account.setTransactions(transactions);

            transactionRepository.save(transaction);
        }

        return accountRepository.save(account);
    }

    public Account getAccountById(Long accountId) {
        return accountRepository.findById(String.valueOf(accountId))
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + accountId));
    }

    public List<Account> getAccountsByCustomerId(Long customerId) {
        return accountRepository.findAll()
                .stream()
                .filter(account -> account.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }
}