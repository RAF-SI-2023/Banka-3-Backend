package com.example.bankservice.domains.model;

import com.example.bankservice.domains.model.enums.TransactionState;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@ToString
public class CreditTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "This field cannot be NULL")
    private String accountFrom;

    @NotNull(message = "This field cannot be NULL")
    private Long creditId;

    @NotNull(message = "This field cannot be NULL")
    private Double amount;

    @NotNull(message = "This field cannot be NULL")
    @Size(min = 3, max = 3, message = "Currency mark must be exactly 3 characters long")
    private String currencyMark;

    @NotNull(message = "This field cannot be NULL")
    private Long date;
}
