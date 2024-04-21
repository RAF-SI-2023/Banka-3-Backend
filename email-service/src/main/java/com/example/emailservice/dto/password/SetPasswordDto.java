package com.example.emailservice.dto.password;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetPasswordDto implements Serializable {
    //saljemo email zaposlenog ili korisnika,
    //i password koji mu treba postaviti
    private String email;
    private String password;
}
