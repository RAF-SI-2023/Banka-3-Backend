package com.example.emailservice.service;

import com.example.emailservice.dto.CodeSenderDto;
import com.example.emailservice.model.CodeSender;
import com.example.emailservice.model.PasswordReset;
import org.springframework.http.ResponseEntity;

import com.example.emailservice.model.PasswordReset;


public interface UserService {

    PasswordReset generateResetCode(String email);

    String tryChangePassword(String identifier, String password);


    ResponseEntity<String> activateUser(CodeSenderDto codeSenderDto);

}
