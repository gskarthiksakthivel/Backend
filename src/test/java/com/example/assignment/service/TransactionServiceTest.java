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

class TransactionServiceTest {

	@Mock
	private TransactionRepository transactionRepository;

	@InjectMocks
	private TransactionService transactionService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCreateTransaction() {
		Long accountId = 1L;
		double amount = 100.0;

		Transaction transaction = new Transaction();
		transaction.setTransactionId(1L);
		transaction.setAccountId(accountId);
		transaction.setAmount(amount);
		transaction.setDate(new Date());

		when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

		Transaction createdTransaction = transactionService.createTransaction(accountId, amount);

		assertEquals(amount, createdTransaction.getAmount());
		assertEquals(accountId, createdTransaction.getAccountId());
		verify(transactionRepository, times(1)).save(any(Transaction.class));
	}
}