// AccountControllerTest.java
package com.example.assignment;

import com.example.assignment.dto.AccountDTO;
import com.example.assignment.model.Account;
import com.example.assignment.model.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AssignmentApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void createAccount() {
		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setCustomerId(1L);
		accountDTO.setInitialCredit(100);

		ResponseEntity<Account> response = restTemplate.postForEntity("/api/accounts", accountDTO, Account.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getBalance()).isEqualTo(100);
	}

	@Test
	public void getCustomerInfo() {
		ResponseEntity<Customer> response = restTemplate.getForEntity("/api/customers/1", Customer.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
}
