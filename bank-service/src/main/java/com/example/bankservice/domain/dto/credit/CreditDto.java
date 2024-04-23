package com.example.bankservice.domain.dto.credit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditDto {
    private Long userId;
    private Long employeeId;
    private String name;
    private String accountNumber;
    private Double amount;
    private int paymentPeriod;
    private double fee;
    private Long startDate;
    private Long endDate;
    private Double monthlyFee;
    private Double remainingAmount;
    private String currencyMark;
}