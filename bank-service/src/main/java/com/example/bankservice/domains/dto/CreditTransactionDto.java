package com.example.bankservice.domains.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditTransactionDto {

    private String accountFrom;
    private Long creditId;
    private Double amount;
    private String currencyMark;
    private Long date;
}
