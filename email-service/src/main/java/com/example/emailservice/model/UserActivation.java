package com.example.emailservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "is_active_user", indexes = @Index( columnList = "email, identifier, code, date, active"))
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

    @Column(name = "identifier")
    @NotNull(message = "This field cannot be NULL")
    @Size(max = 32, message = "The input is too long")
    private String identifier;

    @NotNull(message = "This field cannot be NULL")
    Integer code;

    @NotNull
    private LocalDateTime timeCreated;

    @NotNull
    private boolean activationPossible;
}
