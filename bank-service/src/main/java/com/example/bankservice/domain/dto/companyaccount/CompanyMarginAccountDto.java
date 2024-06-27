package com.example.bankservice.domain.dto.companyaccount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class CompanyMarginAccountDto {
    private Long marginAccountId;
    private Long userId;
    private Long employeeId;
    private String accountNumber;
    private BigDecimal initialMargin;
    private BigDecimal loanValue;
    private boolean active;
}
