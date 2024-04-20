package com.example.bankservice.domain.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class AccountCreateDto {
    @NotBlank
    private Long employeeId;
    @NotBlank
    private Long userId;
    @NotBlank
    private Double availableBalance;
    @NotBlank
    private String currencyMark;
    @NotBlank
    private String accountType;
}
