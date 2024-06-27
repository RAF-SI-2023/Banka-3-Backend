package com.example.bankservice.domain.dto.account;


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
public class UserMarginAccountDto {
    private Long marginAccountId;
    private Long userId;
    private Long employeeId;
    private String accountNumber;
    private BigDecimal initialMargin;
    private BigDecimal loanValue;
    private boolean active;

}
