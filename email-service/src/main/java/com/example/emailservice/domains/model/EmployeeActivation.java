package com.example.emailservice.domains.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "employee_activation", indexes = @Index(name = "identifierIx", columnList = "identifier, activation_possible"))
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeActivation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeActivationId;
    @NotNull(message = "This field cannot be NULL")
    @Email
    private String email;

    @Column(name = "identifier")
    @NotNull(message = "This field cannot be NULL")
    @Size(max = 36, message = "The input is too long")
    private String identifier;

    @NotNull(message = "This field cannot be NULL")
    private LocalDateTime activationTimestamp;
    @Column(name = "activation_possible")
    private boolean activationPossible = false;
}
