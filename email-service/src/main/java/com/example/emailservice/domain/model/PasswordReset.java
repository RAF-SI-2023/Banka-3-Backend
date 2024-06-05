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
import java.time.LocalDateTime;

@Document(collection = "password_reset")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordReset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String passwordResetId;

    @NotNull(message = "This field cannot be NULL")
    @Email
    @Indexed(name = "identifierII")
    private String email;

    @Indexed(name = "identifierII")
    @NotNull(message = "This field cannot be NULL")
    @Size(max = 32, message = "The input is too long")
    private String identifier;

    @NotNull(message = "This field cannot be NULL")
    private LocalDateTime date;

    @NotNull(message = "This field cannot be NULL")
    private Boolean active;
}
