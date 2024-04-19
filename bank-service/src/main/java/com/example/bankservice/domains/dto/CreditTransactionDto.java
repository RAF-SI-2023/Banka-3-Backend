package com.example.bankservice.domains.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditTransactionDto {
    private String accountFrom;
    private Long creditId;
    private BigDecimal amount;
    private String currencyMark;
    private Long date;
}
