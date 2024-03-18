package com.example.emailservice.controller;


import com.example.emailservice.dto.CodeSenderDto;
import com.example.emailservice.service.impl.CodeSenderServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@CrossOrigin()
@RequestMapping("/user")
public class CodeSenderController {

    @Autowired
    private CodeSenderServiceImpl codeSenderService;
    @PostMapping(value = "/activateUser", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> activateUser(@RequestBody CodeSenderDto codeSenderDto) {
        return codeSenderService.activateUser(codeSenderDto);
    }
}
