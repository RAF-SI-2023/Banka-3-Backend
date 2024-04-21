package com.example.emailservice.service;

import com.example.emailservice.client.UserServiceClient;
import com.example.emailservice.dto.password.SetPasswordDto;
import com.example.emailservice.dto.password.SetUserPasswordCodeDto;
import com.example.emailservice.dto.password.TryPasswordResetDto;
import com.example.emailservice.model.PasswordReset;
import com.example.emailservice.model.UserActivation;
import com.example.emailservice.repository.PasswordResetRepository;
import com.example.emailservice.repository.UserActivationRepository;
import com.example.emailservice.service.email.EmailService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UserService {
    private final EmailService emailService;
    private final UserServiceClient userServiceClient;
    private final UserActivationRepository userActivationRepository;
    private final PasswordResetRepository passwordResetRepository;

    //generise sestocifreni kod koji se salje na mail
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

    public Boolean setUserPassword(SetUserPasswordCodeDto setUserPasswordCodeDto) {
        UserActivation userActivation = userActivationRepository.findUserActivationByCodeAndActivationPossibleIsTrue(setUserPasswordCodeDto.getCode()).orElseThrow(
                () -> new NotFoundException("Activation code not found."));
        if (userActivation.isActivationPossible()) {
            SetPasswordDto setPasswordDto = new SetPasswordDto();
            setPasswordDto.setEmail(setUserPasswordCodeDto.getEmail());
            setPasswordDto.setPassword(setUserPasswordCodeDto.getPassword());

            userServiceClient.setUserPassword(setPasswordDto);
            return true;
        }
        return false;
    }

    //slanje linka do stranice za reset passworda
    public void generateResetCode(String email) {
        String identifier = UUID.randomUUID().toString().replace("-", "");
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
    }

    //saljemo novu sifru za korisnika
    public String tryChangePassword(TryPasswordResetDto tryPasswordResetDTO) {
        PasswordReset passwordReset = passwordResetRepository.findByIdentifier(tryPasswordResetDTO.getIdentifier()).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request not found."));

        if (passwordReset.getActive()) {
            SetPasswordDto resetPasswordDto = new SetPasswordDto();
            resetPasswordDto.setEmail(passwordReset.getEmail());
            resetPasswordDto.setPassword(tryPasswordResetDTO.getPassword());
            ResponseEntity<String> response = userServiceClient.setUserPassword(resetPasswordDto);
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
