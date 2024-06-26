package com.example.bankservice.domain.model;

import lombok.*;

import javax.persistence.*;
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
public class CurrencyExchange  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long currencyExchangeId;

    @NotNull(message = "This field cannot be NULL")
    private String accountFrom;

    @NotNull(message = "This field cannot be NULL")
    private String accountTo;

    @NotNull(message = "This field cannot be NULL")
    private BigDecimal amount;

    @NotNull(message = "This field cannot be NULL")
    private BigDecimal commission;

    @NotNull(message = "This field cannot be NULL")
    private String currencyMark;

    @NotNull(message = "This field cannot be NULL")
    private Long date;
}
