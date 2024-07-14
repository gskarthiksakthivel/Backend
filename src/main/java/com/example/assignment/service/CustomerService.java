package com.example.assignment.service;

import com.example.assignment.exception.ResourceNotFoundException;
import com.example.assignment.model.Customer;
import com.example.assignment.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(String.valueOf(id))
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
    }

    public Customer addCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    /*public List<Customer> getAllCustomer() {

        return customerRepository.findAll()
                .stream()
                .map(converter::convertToCustomerDto)
                .collect(Collectors.toList());
    }*/
}