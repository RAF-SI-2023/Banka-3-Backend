package com.example.bankservice.domains.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditPayoutDto {
    private BigDecimal amount;
    private String currencyMark;
    private Long date;
}
