package com.example.bankservice.domain.model;

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
@Getter
@Setter
@Table(schema = "bank_service_schema")
public class Card implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;

    @NotNull
    @Size(min = 8, max = 8, message = "Card number must be exactly 8 characters long")
    private String cardNumber;

    @NotNull
    private String accountNumber;

    @NotNull
    private String cardName;

    @NotNull
    private Long creationDate;

    @NotNull
    private Long expireDate;

    @NotNull
    @Size(min = 3, max = 3, message = "Currency mark must be exactly 3 characters long")
    private String CVV;

    @NotNull
    private boolean active;
}
