package com.example.emailservice.service;

import com.example.emailservice.dto.SetUserPasswordCodeDto;
import com.example.emailservice.dto.TryPasswordResetDto;
import com.example.emailservice.model.PasswordReset;

public interface UserService {

    void userActivation(String email);

    Boolean setUserPassword(SetUserPasswordCodeDto setUserPasswordCodeDTO);

    PasswordReset generateResetCode(String email);

    String tryChangePassword(TryPasswordResetDto tryPasswordResetDTO);
}
