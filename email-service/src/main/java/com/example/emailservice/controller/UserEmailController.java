package com.example.emailservice.controller;

import com.example.emailservice.model.PasswordReset;
import com.example.emailservice.service.EmailService;
import com.example.emailservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/user")
@RequiredArgsConstructor
public class UserEmailController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @GetMapping("resetPassword")
    public String resetPassword(@RequestParam (name = "email") String email) {
        PasswordReset passwordReset = userService.generateResetCode(email);
        return  passwordReset.getEmail() + passwordReset.getIdentifier();
    }

    @PostMapping("tryChangePassword/{identifier}")
    public String tryChangePassword(@PathVariable(name = "identifier") String identifier,
    @RequestBody String password) {
        return userService.tryChangePassword(identifier, password);
//        System.out.println(password);
//        return identifier;
    }





}