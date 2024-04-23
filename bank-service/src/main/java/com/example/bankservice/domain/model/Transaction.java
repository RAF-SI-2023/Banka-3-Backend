package com.example.bankservice.domain.model;

import com.example.bankservice.domain.model.enums.TransactionStatus;
import com.example.bankservice.domain.model.enums.TransactionType;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@ToString
@Table(schema = "bank_service_schema")
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @NotNull(message = "This field cannot be NULL")
    private String accountFrom;

    @NotNull(message = "This field cannot be NULL")
    private String accountTo;

    @NotNull(message = "This field cannot be NULL")
    private BigDecimal amount;

    private int sifraPlacanja;

    private String pozivNaBroj;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "This field cannot be NULL")
    private TransactionStatus transactionStatus;

    @NotNull(message = "This field cannot be NULL")
    private Long date;
}
