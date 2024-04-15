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
@Table(name = "password_reset", indexes = @Index(name = "identifierII", columnList = "email, identifier, date, active"), schema = "email_service_schema")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordReset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long passwordResetId;

    @NotNull(message = "This field cannot be NULL")
    @Email
    private String email;

    @Column(name = "identifier")
    @NotNull(message = "This field cannot be NULL")
    @Size(max = 32, message = "The input is too long")
    private String identifier;

    @NotNull(message = "This field cannot be NULL")
    private LocalDateTime date;

    @NotNull(message = "This field cannot be NULL")
    private Boolean active;
}
