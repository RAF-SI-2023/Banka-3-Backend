package com.example.bankservice.domain.dto.companyaccount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CompanyAccountCreateDto {
    @NotBlank
    private Long companyId;
    @NotBlank
    private Long employeeId;
    @NotBlank
    private Double balance;
    @NotBlank
    private String currencyMark;
    @NotBlank
    private String accountType;
}

