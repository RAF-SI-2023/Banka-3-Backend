package com.example.emailservice.dto.password;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TryPasswordResetDto implements Serializable {
    private String identifier;
    private String password;
}
