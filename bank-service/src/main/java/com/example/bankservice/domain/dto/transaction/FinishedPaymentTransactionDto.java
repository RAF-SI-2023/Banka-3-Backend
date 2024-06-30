package com.example.bankservice.domain.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class FinishedPaymentTransactionDto implements Serializable {
    private String accountFrom; //accountNumber
    private String accountTo;
    private Double amount;
    private int sifraPlacanja;
    private String pozivNaBroj;
    private Long date;
}
