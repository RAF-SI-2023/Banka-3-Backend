package com.example.bankservice.domain.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class StockTransactionDto {
    private Long userId;
    private Long companyId;
    private Long employeeId;
    private Double amount;
    private String currencyMark;
    private double tax;
}
