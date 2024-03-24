package com.example.bankservice.domains.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class TransactionDto {

    private String accountFrom; //accountNumber
    private String accountTo;
    private Double amount;
    private String currencyMark;
    private Double sifraPlacanja;
    private String pozivNaBroj;

}
