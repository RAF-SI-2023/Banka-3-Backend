package com.example.emailservice.dto.password;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetUserPasswordCodeDto implements Serializable {
    private String email;
    private int code;
    private String password;
}