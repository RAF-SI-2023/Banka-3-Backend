package com.example.bankservice.domain.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class CompanyOtcTransactionDto {
    private Long companyFromId;
    private Long companyToId;
    private Double amount;
    private double tax;
}
