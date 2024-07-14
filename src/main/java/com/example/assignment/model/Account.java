package com.example.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import jakarta.validation.*;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;
    private Long customerId;
    private double balance;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;
}