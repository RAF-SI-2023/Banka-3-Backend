package com.example.emailservice.controller;

import com.example.emailservice.dto.SetUserPasswordCodeDTO;
import com.example.emailservice.dto.TryPasswordResetDTO;
import com.example.emailservice.model.PasswordReset;
import com.example.emailservice.service.EmailService;
import com.example.emailservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/user")
@RequiredArgsConstructor
public class UserEmailController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/userActivation")
    public ResponseEntity<Void> userActivation(@RequestParam(name = "email") String email){
        userService.userActivation(email);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping("/activateUser")
    public ResponseEntity<?> setUserPassword(@RequestBody SetUserPasswordCodeDTO setUserPasswordCodeDTO){
        Boolean userActivated = userService.setUserPassword(setUserPasswordCodeDTO);
        if(userActivated){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/resetPassword")
    public String resetPassword(@RequestParam (name = "email") String email) {
        PasswordReset passwordReset = userService.generateResetCode(email);
        return  passwordReset.getEmail() + passwordReset.getIdentifier();
    }

    @PostMapping("/tryPasswordReset")
    public String tryChangePassword(@RequestBody TryPasswordResetDTO tryPasswordResetDTO) {
        return userService.tryChangePassword(tryPasswordResetDTO);
    }
}
