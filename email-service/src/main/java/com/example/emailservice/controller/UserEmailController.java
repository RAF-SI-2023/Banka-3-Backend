package com.example.emailservice.controller;

import com.example.emailservice.dto.ActivationCodeDto;
import com.example.emailservice.service.EmailService;
import com.example.emailservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1/user")
public class UserEmailController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    /**
     * Metoda kreira aktivacioni kod i salje ga korisniku ciji je email prosledjen
     * @param email
     * @return
     */
    @GetMapping(value = "/userActivation")
    public ResponseEntity<?> createActivationCode(@RequestParam(value = "email") String email) {

        ActivationCodeDto activationCodeDto = userService.generateActivationCode(email);
        emailService.sendSimpleMessage(email, "Activation Code", "Your activation code is: "+activationCodeDto.getCode());

        return ResponseEntity.ok(activationCodeDto);
    }

}
