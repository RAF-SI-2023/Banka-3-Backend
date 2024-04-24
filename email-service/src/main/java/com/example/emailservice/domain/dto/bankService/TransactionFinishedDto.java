package com.example.emailservice.domain.dto.bankService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionFinishedDto implements Serializable {
    String email;
    String currencyMark;
    BigDecimal amount;
}