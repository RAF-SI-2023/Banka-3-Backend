package com.example.bankservice.domain.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Entity
@Table(schema = "bank_service_schema")
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    private Long userId;

    private Long employeeId;

    @NotNull(message = "This field cannot be NULL")
    @Size(min = 16, max = 16, message = "Account number must be 16 characters")
    private String accountNumber;

    @NotNull(message = "This field cannot be NULL")
    private BigDecimal reservedAmount;

    @NotNull(message = "This field cannot be NULL")
    private BigDecimal availableBalance;

    @NotNull(message = "This field cannot be NULL")
    private Long creationDate;

    @NotNull(message = "This field cannot be NULL")
    private Long expireDate;

    @ManyToOne
    @JoinColumn(name = "currencyId")
    private Currency currency;

    private String accountType;

    @NotNull(message = "This field cannot be NULL")
    private boolean active;
}
