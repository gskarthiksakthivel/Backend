// AccountController.java
package com.example.assignment.controller;

import com.example.assignment.dto.AccountDTO;
import com.example.assignment.model.Account;
import com.example.assignment.model.Customer;
import com.example.assignment.service.AccountService;
import com.example.assignment.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerService customerService;


    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount(@Valid @RequestBody AccountDTO accountDTO){
        return ResponseEntity.ok(accountService.createAccount(accountDTO.getCustomerId(), accountDTO.getInitialCredit()));
    }

    @GetMapping("/customers/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable String customerId){
        return ResponseEntity.ok(customerService.getCustomerById(Long.valueOf(customerId)));
    }

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }
    @GetMapping("/customers/{customerId}/accounts")
    public List<Account> getCustomerAccounts(@PathVariable Long customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        return accountService.getAccountsByCustomerId(customer.getId());
    }





}
