package com.example.bankservice.domain.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class AddToMarginDto {
    @NotBlank
    private Double amount;
}
