package com.example.bankservice.domain.dto.creditRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CreditRequestCreateDto {
    private Long userId;
    private String name;
    private String accountNumber;
    private Double amount;
    private String applianceReason;
    private Boolean employed;
    private Long dateOfEmployment;
    private int paymentPeriod;
    private String currencyMark;
}
