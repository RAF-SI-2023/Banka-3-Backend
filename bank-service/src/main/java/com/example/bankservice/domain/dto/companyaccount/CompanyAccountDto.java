package com.example.bankservice.domain.dto.companyaccount;

import com.example.bankservice.domain.model.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class CompanyAccountDto {
    private Long companyAccountId;
    private Long companyId;
    private Long employeeId;
    private String accountNumber;
    private BigDecimal reservedAmount;
    private BigDecimal availableBalance;
    private Long creationDate;
    private Long expireDate;
    private boolean active;
    private Currency currency;

}

