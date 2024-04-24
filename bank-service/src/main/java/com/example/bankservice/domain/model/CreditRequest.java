package com.example.bankservice.domain.model;

import com.example.bankservice.domain.model.enums.CreditRequestStatus;
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
public class CreditRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long creditRequestId;

    private Long userId;

    @NotNull(message = "This field cannot be NULL")
    private String name;

    @NotNull(message = "This field cannot be NULL")
    private String accountNumber;

    @NotNull(message = "This field cannot be NULL")
    private BigDecimal amount;

    @NotNull(message = "This field cannot be NULL")
    private String applianceReason;

    private BigDecimal monthlyPaycheck;

    @NotNull(message = "This field cannot be NULL")
    private Boolean employed;

    @NotNull(message = "This field cannot be NULL")
    private Long dateOfEmployment;

    @NotNull(message = "This field cannot be NULL")
    private int paymentPeriod;

    @NotNull(message = "This field cannot be NULL")
    @Enumerated(EnumType.STRING)
    private CreditRequestStatus status;

    @NotNull(message = "This field cannot be NULL")
    private String currencyMark;
}
