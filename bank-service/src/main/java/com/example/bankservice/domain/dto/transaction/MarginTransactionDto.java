package com.example.bankservice.domain.dto.transaction;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor

public class MarginTransactionDto implements Serializable {
    private String accountFrom; //accountNumber
    private String accountTo;
    private Double amount;
    private String currencyMark;
    private String transactionType;
    private Long date;
}
