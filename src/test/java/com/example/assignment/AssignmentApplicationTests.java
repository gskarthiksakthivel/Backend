// AccountControllerTest.java
package com.example.assignment;

import com.example.assignment.dto.AccountDTO;
import com.example.assignment.model.Account;
import com.example.assignment.model.Customer;
import com.example.assignment.repository.AccountRepository;
import com.example.assignment.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AssignmentApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private ObjectMapper objectMapper;

	private String baseUrl;

	@BeforeEach
	public void setUp() {
		baseUrl = "http://localhost:" + port + "/api";
		accountRepository.deleteAll();
		customerRepository.deleteAll();
	}

	@Test
	public void testCreateAccount() throws Exception {
		Customer customer = new Customer();
		customer.setName("John");
		customer.setSurname("Doe");
		customer = customerRepository.save(customer);

		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setCustomerId(customer.getId());
		accountDTO.setInitialCredit(100.0);

		HttpEntity<AccountDTO> request = new HttpEntity<>(accountDTO);
		ResponseEntity<Account> response = restTemplate.postForEntity(baseUrl + "/accounts", request, Account.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getCustomerId()).isEqualTo(customer.getId());
		assertThat(response.getBody().getBalance()).isEqualTo(100.0);
	}

	@Test
	public void testGetCustomerById() throws Exception {
		Customer customer = new Customer();
		customer.setName("John");
		customer.setSurname("Doe");
		customer = customerRepository.save(customer);

		ResponseEntity<Customer> response = restTemplate.getForEntity(baseUrl + "/customers/" + customer.getId(), Customer.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getId()).isEqualTo(customer.getId());
		assertThat(response.getBody().getName()).isEqualTo("John");
		assertThat(response.getBody().getSurname()).isEqualTo("Doe");
	}

	@Test
	public void testGetAllCustomers() throws Exception {
		Customer customer1 = new Customer();
		customer1.setName("John");
		customer1.setSurname("Doe");
		customerRepository.save(customer1);

		Customer customer2 = new Customer();
		customer2.setName("Jane");
		customer2.setSurname("Smith");
		customerRepository.save(customer2);

		ResponseEntity<Customer[]> response = restTemplate.getForEntity(baseUrl + "/customers", Customer[].class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		List<Customer> customers = List.of(response.getBody());
		assertThat(customers).hasSize(2);
		assertThat(customers).extracting(Customer::getName).containsExactlyInAnyOrder("John", "Jane");
		assertThat(customers).extracting(Customer::getSurname).containsExactlyInAnyOrder("Doe", "Smith");
	}

	@Test
	public void testGetCustomerAccounts() throws Exception {
		Customer customer = new Customer();
		customer.setName("John");
		customer.setSurname("Doe");
		customer = customerRepository.save(customer);

		Account account1 = new Account();
		account1.setCustomerId(customer.getId());
		account1.setBalance(100.0);
		accountRepository.save(account1);

		Account account2 = new Account();
		account2.setCustomerId(customer.getId());
		account2.setBalance(200.0);
		accountRepository.save(account2);

		ResponseEntity<Account[]> response = restTemplate.getForEntity(baseUrl + "/customers/" + customer.getId() + "/accounts", Account[].class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		List<Account> accounts = List.of(response.getBody());
		assertThat(accounts).hasSize(2);
		assertThat(accounts).extracting(Account::getBalance).containsExactlyInAnyOrder(100.0, 200.0);
	}
}
