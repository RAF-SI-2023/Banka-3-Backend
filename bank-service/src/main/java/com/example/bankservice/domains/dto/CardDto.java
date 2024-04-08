package com.example.bankservice.domains.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class CardDto {
    private String cardNumber;
    private Long expireDate;
    private String cvc;
}
