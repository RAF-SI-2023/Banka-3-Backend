package com.example.emailservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "company_activation", indexes = @Index(columnList = "email, code, timeCreated, activationPossible"), schema = "email_service_schema")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyActivation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "email field cannot be NULL")
    @Email
    private String email;

    @NotNull(message = "code field cannot be NULL")
    private int code;

    @NotNull
    private LocalDateTime timeCreated;

    @NotNull
    private boolean activationPossible;
}
