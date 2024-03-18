package com.example.emailservice.controllers;


import com.example.emailservice.domains.dto.CodeSenderDto;
import com.example.emailservice.repositories.CodeSenderRepository;
import com.example.emailservice.services.CodeSenderService;
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
    private CodeSenderService codeSenderService;
    @PostMapping(value = "/activateUser", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> activateUser(@RequestBody CodeSenderDto codeSenderDto) {
        return codeSenderService.activateUser(codeSenderDto);
    }
}
