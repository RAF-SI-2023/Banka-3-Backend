package com.example.emailservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_activation", indexes = @Index(columnList = "email, code, timeCreated, activationPossible"))
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserActivation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "This field cannot be NULL")
    @Email
    private String email;

    @NotNull(message = "This field cannot be NULL")
    private int code;

    @NotNull
    private LocalDateTime timeCreated;

    @NotNull
    private boolean activationPossible;
}