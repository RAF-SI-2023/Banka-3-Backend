package com.example.bankservice.domain.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CheckAccountBalanceDto {
    @NotBlank
    private Long id;
    @NotBlank
    private Double amount;
}
