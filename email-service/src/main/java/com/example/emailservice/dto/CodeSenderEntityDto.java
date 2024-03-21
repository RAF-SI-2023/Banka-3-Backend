package com.example.emailservice.dto;

import lombok.*;

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
