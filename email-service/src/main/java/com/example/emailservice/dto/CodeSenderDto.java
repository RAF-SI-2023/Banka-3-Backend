package com.example.emailservice.dto;


import lombok.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
public class CodeSenderDto {

    private String email;
    private Integer code;
    private String password;
    private String confirmPassword;
}
