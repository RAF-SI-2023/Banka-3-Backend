package com.example.emailservice.service;

import com.example.emailservice.model.PasswordReset;

public interface UserService {

    PasswordReset generateResetCode(String email);

    String tryChangePassword(String identifier, String password);
}
