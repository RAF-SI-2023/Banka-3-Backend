package com.example.emailservice.service;
import com.example.emailservice.dto.CodeSenderDto;

import com.example.emailservice.dto.CodeSenderEntityDto;
import org.springframework.http.ResponseEntity;

public interface CodeSenderService {

    ResponseEntity<String> activateUser(CodeSenderDto codeSenderDto);

    CodeSenderEntityDto findCodeSenderByCode(Integer code);
}
