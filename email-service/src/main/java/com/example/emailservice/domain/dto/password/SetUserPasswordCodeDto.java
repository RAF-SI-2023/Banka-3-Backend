package com.example.emailservice.domain.dto.password;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetUserPasswordCodeDto implements Serializable {
    //koristimo ga i za User-a i za Company
    //kada se prvi put loguju i moraju da unesu kod
    private String email;
    private int code;
    private String password;
}