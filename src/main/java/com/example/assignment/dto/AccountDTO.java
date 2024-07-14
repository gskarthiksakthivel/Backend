// AccountDTO.java
package com.example.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AccountDTO {
    private Long customerId;
    private double initialCredit;
}
