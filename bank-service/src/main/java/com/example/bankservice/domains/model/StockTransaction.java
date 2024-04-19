package com.example.bankservice.domains.model;

import com.example.bankservice.domains.model.enums.TransactionState;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@ToString
@Table(schema = "bank_service_schema")
public class StockTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockTransactionId;

    @NotNull(message = "This field cannot be NULL")
    private String accountFrom;

    @NotNull(message = "This field cannot be NULL")
    private String accountTo;

    @NotNull(message = "This field cannot be NULL")
    private Double amount;

    @Size(min = 3, max = 4, message = "Currency mark must be exactly 3 characters long")
    private String currencyMark;

    @Enumerated(EnumType.STRING)
    private TransactionState state;

    private String pozivNaBroj;

    private Long date;

}
