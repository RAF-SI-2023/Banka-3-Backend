package com.example.emailservice.service;

import com.example.emailservice.dto.TryPasswordResetDTO;
import com.example.emailservice.model.PasswordReset;

public interface UserService {

    void  userActivation(String email);

    String setUserPassword(int code, String password);
    PasswordReset generateResetCode(String email);

    String tryChangePassword(TryPasswordResetDTO tryPasswordResetDTO);
}
