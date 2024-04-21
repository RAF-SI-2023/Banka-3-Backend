package com.example.bankservice.domain.dto.card;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class CardLoginDto {
    private String cardNumber;
    private String CVV;
}
