package com.example.emailservice.service;

import com.example.emailservice.dto.TryPasswordResetDTO;
import com.example.emailservice.model.PasswordReset;

public interface UserService {

    void  userActivation(String email);

    String setUserPassword(String identifier, String password);
    PasswordReset generateResetCode(String email);

    String tryChangePassword(TryPasswordResetDTO tryPasswordResetDTO);
}
