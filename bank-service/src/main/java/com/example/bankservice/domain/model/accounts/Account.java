package com.example.bankservice.domain.model.accounts;

import com.example.bankservice.domain.model.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(schema = "bank_service_schema")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    private Long employeeId;

    @NotNull(message = "This field cannot be NULL")
    private String accountNumber;

    @NotNull(message = "This field cannot be NULL")
    private BigDecimal availableBalance;

    @NotNull(message = "This field cannot be NULL")
    private BigDecimal reservedAmount;

    @NotNull(message = "This field cannot be NULL")
    private Long creationDate;

    @NotNull(message = "This field cannot be NULL")
    private Long expireDate;

    @ManyToOne
    @JoinColumn(name = "currencyId")
    private Currency currency;

    @NotNull(message = "This field cannot be NULL")
    private boolean active;
}
