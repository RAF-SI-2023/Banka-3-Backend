package com.example.emailservice.domains.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class CodeSenderDto {

    private String email;
    private Integer code;
    private String password;
    private String confirmPassword;
}
