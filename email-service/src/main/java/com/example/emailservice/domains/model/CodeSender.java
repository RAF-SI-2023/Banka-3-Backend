package com.example.emailservice.domains.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "codeSender", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class CodeSender {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codeSenderID;

    @NotNull(message = "This field cannot be NULL")
    @Email
    private String email;

    @NotNull(message = "This field cannot be NULL")
    @Size(max = 6, message = "The input is too long") //
    private Integer code;

    @NotNull(message = "This field cannot be NULL")
    private Long date;

    @NotNull(message = "This field cannot be NULL")
    private Boolean active;

}
