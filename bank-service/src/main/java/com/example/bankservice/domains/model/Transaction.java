package com.example.bankservice.domains.model;

import com.example.bankservice.domains.model.enums.TransactionState;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@ToString
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @NotNull(message = "This field cannot be NULL")
    private String accountFrom;

    @NotNull(message = "This field cannot be NULL")
    private String accountTo;

    @NotNull(message = "This field cannot be NULL")
    private Double amount;

    @NotNull(message = "This field cannot be NULL")
    @Size(min = 3, max = 4, message = "Currency mark must be exactly 3 characters long")
    private String currencyMark;

    @NotNull(message = "This field cannot be NULL")
    @Min(value = 100, message = "Sifra placanja must be exactly 3 characters long")
    @Max(value = 999, message = "Sifra placanja must be exactly 3 characters long")
    private int sifraPlacanja;

    @NotNull(message = "This field cannot be NULL")
    private String pozivNaBroj;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "This field cannot be NULL")
    private TransactionState state;

    @NotNull(message = "This field cannot be NULL")
    private Long date;


}
