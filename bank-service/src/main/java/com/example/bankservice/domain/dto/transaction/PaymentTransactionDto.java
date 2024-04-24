package com.example.bankservice.domain.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class PaymentTransactionDto {
    private String accountFrom; //accountNumber
    private String accountTo;
    private Double amount;
    private String currencyMark;
    private int sifraPlacanja;
    private String pozivNaBroj;
}
