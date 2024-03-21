package com.example.emailservice.service;

import com.example.emailservice.dto.SetUserPasswordCodeDTO;
import com.example.emailservice.dto.TryPasswordResetDTO;
import com.example.emailservice.model.PasswordReset;

public interface UserService {

    void  userActivation(String email);

    Boolean setUserPassword(SetUserPasswordCodeDTO setUserPasswordCodeDTO);
    PasswordReset generateResetCode(String email);

    String tryChangePassword(TryPasswordResetDTO tryPasswordResetDTO);
}
