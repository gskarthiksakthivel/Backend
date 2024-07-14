// AccountServiceTest.java
package com.example.assignment.service;

import com.example.assignment.exception.ResourceNotFoundException;
import com.example.assignment.model.Account;
import com.example.assignment.model.Transaction;
import com.example.assignment.repository.AccountRepository;
import com.example.assignment.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

	@Mock
	private AccountRepository accountRepository;

	@Mock
	private TransactionRepository transactionRepository;

	@InjectMocks
	private AccountService accountService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCreateAccountWithInitialCredit() {
		Long customerId = 1L;
		double initialCredit = 100.0;

		Account account = new Account();
		account.setAccountId(1L);
		account.setCustomerId(customerId);
		account.setBalance(initialCredit);

		Transaction transaction = new Transaction();
		transaction.setTransactionId(1L);
		transaction.setAccountId(account.getAccountId());
		transaction.setAmount(initialCredit);
		transaction.setDate(new Date());

		List<Transaction> transactions = new ArrayList<>();
		transactions.add(transaction);
		account.setTransactions(transactions);

		when(accountRepository.save(any(Account.class))).thenReturn(account);
		when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

		Account createdAccount = accountService.createAccount(customerId, initialCredit);

		assertEquals(initialCredit, createdAccount.getBalance());
		assertEquals(1, createdAccount.getTransactions().size());
		verify(accountRepository, times(1)).save(any(Account.class));
		verify(transactionRepository, times(1)).save(any(Transaction.class));
	}

	@Test
	void testCreateAccountWithoutInitialCredit() {
		Long customerId = 1L;
		double initialCredit = 0.0;

		Account account = new Account();
		account.setAccountId(1L);
		account.setCustomerId(customerId);
		account.setBalance(initialCredit);

		when(accountRepository.save(any(Account.class))).thenReturn(account);

		Account createdAccount = accountService.createAccount(customerId, initialCredit);

		assertEquals(initialCredit, createdAccount.getBalance());
		assertNull(createdAccount.getTransactions());
		verify(accountRepository, times(1)).save(any(Account.class));
		verify(transactionRepository, times(0)).save(any(Transaction.class));
	}

	@Test
	void testGetAccountById() {
		Long accountId = 1L;

		Account account = new Account();
		account.setAccountId(accountId);

		when(accountRepository.findById(String.valueOf(accountId))).thenReturn(Optional.of(account));

		Account foundAccount = accountService.getAccountById(accountId);

		assertNotNull(foundAccount);
		assertEquals(accountId, foundAccount.getAccountId());
		verify(accountRepository, times(1)).findById(String.valueOf(accountId));
	}

	@Test
	void testGetAccountByIdNotFound() {
		Long accountId = 1L;

		when(accountRepository.findById(String.valueOf(accountId))).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> accountService.getAccountById(accountId));
		verify(accountRepository, times(1)).findById(String.valueOf(accountId));
	}
}
