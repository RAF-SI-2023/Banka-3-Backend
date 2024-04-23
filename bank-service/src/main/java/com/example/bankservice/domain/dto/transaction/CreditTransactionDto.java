package com.example.bankservice.domain.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CreditTransactionDto {
    private String accountFrom; //accountNumber
    private String accountTo;
    private Double amount;
    private String currencyMark;
    private String transactionType;
}
