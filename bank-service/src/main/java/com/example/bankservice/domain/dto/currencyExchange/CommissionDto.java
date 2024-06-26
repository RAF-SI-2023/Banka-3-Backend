package com.example.bankservice.domain.dto.currencyExchange;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class CommissionDto {
    private String accountFrom;
    private BigDecimal commission;
    private String currencyMark;
    private Long date;
}
