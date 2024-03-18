package com.example.emailservice.controller;

import com.example.emailservice.service.UserEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1/user")
public class UserEmailController {

    @Autowired
    private UserEmailService userService;

    @GetMapping(value = "/userActivation")
    public void getActivationCode(@RequestParam(value = "email") String email) {


    }

}
