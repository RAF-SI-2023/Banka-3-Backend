package com.example.emailservice.service;

import com.example.emailservice.dto.ActivationCodeDto;

public interface UserService {

    ActivationCodeDto generateActivationCode(String email);
}
