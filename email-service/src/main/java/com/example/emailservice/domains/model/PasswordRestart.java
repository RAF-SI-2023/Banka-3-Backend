package com.example.emailservice.domains.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "passwordRestart", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class PasswordRestart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long passwordRestartID;

    @NotNull(message = "This field cannot be NULL")
    @Email
    private String email;

    @NotNull(message = "This field cannot be NULL")
    private String identifier;

    @NotNull(message = "This field cannot be NULL")
    private Long date;

    @NotNull(message = "This field cannot be NULL")
    private Boolean active;

}


