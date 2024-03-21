package com.example.emailservice.service.impl;

import com.example.emailservice.client.UserServiceClient;
import com.example.emailservice.dto.ResetUserPasswordDTO;
import com.example.emailservice.model.PasswordReset;
import com.example.emailservice.repository.CodeSenderRepository;
import com.example.emailservice.repository.PasswordResetRepository;
import com.example.emailservice.service.EmailService;
import com.example.emailservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.lang.Thread.sleep;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private CodeSenderRepository codeSenderRepository;

    @Autowired
    private PasswordResetRepository passwordResetRepository;
    @Autowired
    private EmailService emailService;

    @Autowired
    private UserServiceClient userServiceClient;

    @Override
    public PasswordReset generateResetCode(String email) {
        String identifier = UUID.randomUUID().toString().replace("-", "");;
        PasswordReset passwordReset = new PasswordReset(
                null,
                email,
                identifier,
                LocalDateTime.now(),
                true);
        passwordResetRepository.save(passwordReset);
        emailService.sendSimpleMessage(email, "Reset password request", "localhost:4200/resetPassword/" + identifier);
        new Thread(()->{
            long activationAvailableTime = 5*60*1000;
            try {
                sleep(activationAvailableTime);
                passwordReset.setActive(false);
                passwordResetRepository.save(passwordReset);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        return passwordReset;
    }

    @Override
    public String tryChangePassword(String identifier, String password) {
        PasswordReset passwordReset =  passwordResetRepository.findByIdentifier(identifier).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request not found."));
        return identifier;
//        if(passwordReset.getActive()){
//            ResetUserPasswordDTO resetPasswordDTO = new ResetUserPasswordDTO(password, passwordReset.getEmail());
//            ResponseEntity<String> response = userServiceClient.resetUserPassword(resetPasswordDTO);
//            if(!response.getStatusCode().is2xxSuccessful()){
//                throw new ResponseStatusException(response.getStatusCode(), response.getBody());
//            }
//            return "Password successfully changed";
//        }else{
//            return "Password reset failed";
//        }
    }

}
