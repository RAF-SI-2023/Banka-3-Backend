package com.example.emailservice.domain.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Document(collection = "transaction_activation")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionActivation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "This field cannot be NULL")
    @Email
    @Indexed
    private String email;

    @NotNull(message = "This field cannot be NULL")
    private int code;

    @NotNull
    private LocalDateTime timeCreated;

    @NotNull
    private boolean active;
}
