// AccountControllerTest.java
package com.example.assignment.controller;

import com.example.assignment.dto.AccountDTO;
import com.example.assignment.model.Account;
import com.example.assignment.model.Customer;
import com.example.assignment.service.AccountService;
import com.example.assignment.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private CustomerService customerService;

    @Test
    void testCreateAccount() throws Exception {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setCustomerId(1L);
        accountDTO.setInitialCredit(100);

        Account account = new Account();
        account.setAccountId(1L);
        account.setCustomerId(1L);
        account.setBalance(100);

        when(accountService.createAccount(any(Long.class), any(Double.class))).thenReturn(account);

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\":1,\"initialCredit\":100}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(1L))
                .andExpect(jsonPath("$.customerId").value(1L))
                .andExpect(jsonPath("$.balance").value(100.0));
    }

    @Test
    void testGetCustomerById() throws Exception {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John");
        customer.setSurname("Doe");

        when(customerService.getCustomerById(1L)).thenReturn(customer);

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.surname").value("Doe"));
    }

    @Test
    void testGetCustomerAccounts() throws Exception {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John");
        customer.setSurname("Doe");

        Account account1 = new Account();
        account1.setAccountId(1L);
        account1.setCustomerId(1L);
        account1.setBalance(100);

        Account account2 = new Account();
        account2.setAccountId(2L);
        account2.setCustomerId(1L);
        account2.setBalance(200);

        List<Account> accounts = new ArrayList<>();
        accounts.add(account1);
        accounts.add(account2);

        when(customerService.getCustomerById(1L)).thenReturn(customer);
        when(accountService.getAccountsByCustomerId(1L)).thenReturn(accounts);

        mockMvc.perform(get("/api/customers/1/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountId").value(1L))
                .andExpect(jsonPath("$[0].balance").value(100.0))
                .andExpect(jsonPath("$[1].accountId").value(2L))
                .andExpect(jsonPath("$[1].balance").value(200.0));
    }
}
