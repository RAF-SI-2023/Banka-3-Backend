package com.example.bankservice.refactor.domain.model;

import com.example.bankservice.refactor.domain.model.enums.TransactionStatus;
import com.example.bankservice.refactor.domain.model.enums.TransactionType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@ToString
@Table(schema = "bank_service_schema")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @NotNull(message = "This field cannot be NULL")
    private String accountFrom;

    @NotNull(message = "This field cannot be NULL")
    private String accountTo;

    @NotNull(message = "This field cannot be NULL")
    private Double amount;

    @Min(value = 100, message = "Sifra placanja must be exactly 3 characters long")
    @Max(value = 999, message = "Sifra placanja must be exactly 3 characters long")
    private int sifraPlacanja;

    private String pozivNaBroj;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "This field cannot be NULL")
    private TransactionStatus status;

    @NotNull(message = "This field cannot be NULL")
    private Long date;
}
