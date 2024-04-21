package com.example.bankservice.domain.dto.userService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEmailDto implements Serializable {
    private Long userId;
    private String email;
}
