package com.example.emailservice.controller;

import com.example.emailservice.dto.CodeSenderDto;
import com.example.emailservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserAccountActivationController {


    @Autowired
    private UserService userService;

    @PostMapping(value = "/activateUser", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> activateUser(@RequestBody CodeSenderDto codeSenderDto) {
        return userService.activateUser(codeSenderDto);
    }


}
