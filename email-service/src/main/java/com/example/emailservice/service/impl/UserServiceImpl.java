package com.example.emailservice.service.impl;

import com.example.emailservice.client.UserServiceClient;
import com.example.emailservice.dto.ResetUserPasswordDto;
import com.example.emailservice.dto.SetPasswordDto;
import com.example.emailservice.dto.SetUserPasswordCodeDto;
import com.example.emailservice.dto.TryPasswordResetDto;
import com.example.emailservice.model.PasswordReset;
import com.example.emailservice.model.UserActivation;
import com.example.emailservice.repository.PasswordResetRepository;
import com.example.emailservice.repository.UserActivationRepository;
import com.example.emailservice.service.EmailService;
import com.example.emailservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import static java.lang.Thread.sleep;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private PasswordResetRepository passwordResetRepository;
    @Autowired
    private EmailService emailService;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private UserActivationRepository userActivationRepository;

    @Override
    public void userActivation(String email) {
        Integer code = new Random().nextInt(100000, 999999);
        UserActivation userActivation = new UserActivation(null,
                email,
                code,
                LocalDateTime.now(),
                true);
        userActivationRepository.save(userActivation);
        emailService.sendSimpleMessage(email, getSubject(), getText(code));
        new Thread(() -> {
            long activationAvailableTime = 5 * 60 * 1000;
            try {
                sleep(activationAvailableTime);
                userActivation.setActivationPossible(false);
                userActivationRepository.save(userActivation);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @Override
    public Boolean setUserPassword(SetUserPasswordCodeDto setUserPasswordCodeDTO) {
        UserActivation userActivation =
                userActivationRepository.findUserActivationByCodeAndActivationPossibleIsTrue(setUserPasswordCodeDTO.getCode())
                        .orElseThrow(() -> new NotFoundException("Activation code not found."));
        if (userActivation.isActivationPossible()) {
            userServiceClient.setUserPassword(new SetPasswordDto(setUserPasswordCodeDTO.getPassword(),
                    setUserPasswordCodeDTO.getEmail()));
            return true;
        }
        return false;
    }

    @Override
    public PasswordReset generateResetCode(String email) {
        String identifier = UUID.randomUUID().toString().replace("-", "");
        ;
        PasswordReset passwordReset = new PasswordReset(
                null,
                email,
                identifier,
                LocalDateTime.now(),
                true);
        passwordResetRepository.save(passwordReset);
        emailService.sendSimpleMessage(email, "Reset password request", "localhost:80/resetPassword/" + identifier);
        new Thread(() -> {
            long activationAvailableTime = 5 * 60 * 1000;
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
    public String tryChangePassword(TryPasswordResetDto tryPasswordResetDTO) {
        PasswordReset passwordReset =
                passwordResetRepository.findByIdentifier(tryPasswordResetDTO.getIdentifier()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request not found."));
        if (passwordReset.getActive()) {
            ResetUserPasswordDto resetPasswordDTO = new ResetUserPasswordDto(tryPasswordResetDTO.getPassword(),
                    passwordReset.getEmail());
            ResponseEntity<String> response = userServiceClient.resetUserPassword(resetPasswordDTO);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ResponseStatusException(response.getStatusCode(), response.getBody());
            }
            return "Password successfully changed";
        } else {
            return "Password reset failed";
        }
    }

    protected String getSubject() {
        return "User account activation";
    }

    protected String getText(Integer code) {
        return "Your activation code is:" + code;
    }

}
