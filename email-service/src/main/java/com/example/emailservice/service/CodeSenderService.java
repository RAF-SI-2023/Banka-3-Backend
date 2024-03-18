package com.example.emailservice.service;
import com.example.emailservice.dto.CodeSenderDto;

import org.springframework.http.ResponseEntity;

public interface CodeSenderService {

    ResponseEntity<String> activateUser(CodeSenderDto codeSenderDto);
}
