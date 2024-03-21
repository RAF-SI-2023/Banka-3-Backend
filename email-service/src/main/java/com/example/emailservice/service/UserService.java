package com.example.emailservice.service;

import com.example.emailservice.dto.TryPasswordResetDTO;
import com.example.emailservice.model.PasswordReset;

public interface UserService {

    PasswordReset generateResetCode(String email);

    String tryChangePassword(TryPasswordResetDTO tryPasswordResetDTO);
}
