package com.example.emailservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "code_sender")
public class CodeSender implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codeSenderId;

    @Email
    @NotNull(message = "This field cannot be NULL")
    private String email;

    @Min(value = 100000, message = "Code must contain 6 digits")
    @Max(value = 999999, message = "Code must contain 6 digits")
    @NotNull(message = "This field cannot be NULL")
    private Integer code;

    @Column(name = "create_time")
    @NotNull(message = "This field cannot be NULL")
    private Long createTime;
}
