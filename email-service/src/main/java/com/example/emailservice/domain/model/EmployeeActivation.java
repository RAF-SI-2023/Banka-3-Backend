package com.example.emailservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Document(collection = "employee_activation")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeActivation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String employeeActivationId;
    @NotNull(message = "This field cannot be NULL")
    @Email
    @Indexed
    private String email;

    @Indexed(name = "identifierIx")
    @NotNull(message = "This field cannot be NULL")
    @Size(max = 36, message = "The input is too long")
    private String identifier;

    @NotNull(message = "This field cannot be NULL")
    private LocalDateTime activationTimestamp;

    private boolean activationPossible = true;
}
