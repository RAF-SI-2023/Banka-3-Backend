package com.example.emailservice.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
public class CodeSenderEntityDto {

    private String email;
    private Integer code;
    private Long date;
    private Boolean active;
}
