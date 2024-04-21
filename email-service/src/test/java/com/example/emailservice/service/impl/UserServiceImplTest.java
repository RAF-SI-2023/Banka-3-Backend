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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private SetPasswordDto setPasswordDTO;
    @Mock
    private SetUserPasswordCodeDto setUserPasswordCodeDTO;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private PasswordResetRepository passwordResetRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private UserActivationRepository userActivationRepository;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    public void userActivationTest() {
        String email = "test@example.com";
        Random random = new Random();
        int code = random.nextInt(999999 - 100000 + 1) + 100000;
        UserActivation userActivation = new UserActivation(
                null,
                email,
                code,
                LocalDateTime.now(),
                true);

        when(userActivationRepository.save(any())).thenReturn(userActivation);
        doNothing().when(emailService).sendSimpleMessage(eq(email), anyString(), anyString());

        userServiceImpl.userActivation(email);

        verify(userActivationRepository, times(1)).save(any(UserActivation.class));
    }

    @Test
    public void setUserPasswordTest() {
        SetUserPasswordCodeDto setUserPasswordCodeDTO = new SetUserPasswordCodeDto();
        setUserPasswordCodeDTO.setCode(1234);
        setUserPasswordCodeDTO.setEmail("test@example.com");
        setUserPasswordCodeDTO.setPassword("newPassword");

        UserActivation userActivation = new UserActivation();
        userActivation.setActivationPossible(true);

        when(userActivationRepository.findUserActivationByCodeAndActivationPossibleIsTrue(1234))
                .thenReturn(Optional.of(userActivation));

        boolean result = userServiceImpl.setUserPassword(setUserPasswordCodeDTO);


        assertTrue(result);
        verify(userServiceClient, times(1)).setUserPassword(any(SetPasswordDto.class));
    }

    @Test
    public void setUserPasswordTest_Fail() {
        SetUserPasswordCodeDto setUserPasswordCodeDTO = new SetUserPasswordCodeDto();
        setUserPasswordCodeDTO.setCode(1234);

        UserActivation userActivation = new UserActivation();
        userActivation.setActivationPossible(false);

        when(userActivationRepository.findUserActivationByCodeAndActivationPossibleIsTrue(1234))
                .thenReturn(Optional.of(userActivation));

        boolean result = userServiceImpl.setUserPassword(setUserPasswordCodeDTO);

        assertFalse(result);
        verify(userServiceClient, never()).setUserPassword(any(SetPasswordDto.class));
    }

    @Test
    public void setUserPasswordTest_Fail_Code() {
        SetUserPasswordCodeDto setUserPasswordCodeDTO = new SetUserPasswordCodeDto();
        setUserPasswordCodeDTO.setCode(1234);

        when(userActivationRepository.findUserActivationByCodeAndActivationPossibleIsTrue(1234))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userServiceImpl.setUserPassword(setUserPasswordCodeDTO));
        verify(userServiceClient, never()).setUserPassword(any(SetPasswordDto.class));
    }

    @Test
    public void generateResetCodeTest() throws InterruptedException {
        String email = "test@example.com";
        String identifier = UUID.randomUUID().toString().replace("-", "");
        PasswordReset passwordReset = new PasswordReset(null, email, identifier, LocalDateTime.now(), true);

        when(passwordResetRepository.save(any(PasswordReset.class))).thenReturn(passwordReset);

        userServiceImpl.generateResetCode(email);

        verify(passwordResetRepository, times(1)).save(any(PasswordReset.class));
        verify(emailService, times(1)).sendSimpleMessage(eq(email), anyString(), anyString());

        Thread.sleep(100); // Sleep to ensure thread has started
        verify(passwordResetRepository, times(1)).save(any(PasswordReset.class)); // Check if thread updates the reset
    }

    @Test
    public void tryChangePasswordTest() {
        TryPasswordResetDto tryPasswordResetDTO = new TryPasswordResetDto();
        tryPasswordResetDTO.setIdentifier("testIdentifier");
        tryPasswordResetDTO.setPassword("newPassword");

        PasswordReset passwordReset = new PasswordReset();
        passwordReset.setActive(true);
        passwordReset.setEmail("test@example.com");

        when(passwordResetRepository.findByIdentifier("testIdentifier")).thenReturn(Optional.of(passwordReset));

        ResponseEntity<String> successResponse = new ResponseEntity<>("Success", HttpStatus.OK);
        when(userServiceClient.resetUserPassword(any(ResetUserPasswordDto.class))).thenReturn(successResponse);

        String result = userServiceImpl.tryChangePassword(tryPasswordResetDTO);

        assertEquals("Password successfully changed", result);
        verify(userServiceClient, times(1)).resetUserPassword(any(ResetUserPasswordDto.class));
    }

    @Test
    public void tryChangePasswordTest_Fail_Identifier() {
        TryPasswordResetDto tryPasswordResetDTO = new TryPasswordResetDto();
        tryPasswordResetDTO.setIdentifier("nonExistentIdentifier");

        when(passwordResetRepository.findByIdentifier("nonExistentIdentifier")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userServiceImpl.tryChangePassword(tryPasswordResetDTO));
        verify(userServiceClient, never()).resetUserPassword(any(ResetUserPasswordDto.class));
    }

    @Test
    public void tryChangePasswordTest_Fail() {
        TryPasswordResetDto tryPasswordResetDTO = new TryPasswordResetDto();
        tryPasswordResetDTO.setIdentifier("testIdentifier");

        PasswordReset passwordReset = new PasswordReset();
        passwordReset.setActive(false);

        when(passwordResetRepository.findByIdentifier("testIdentifier")).thenReturn(Optional.of(passwordReset));

        String result = userServiceImpl.tryChangePassword(tryPasswordResetDTO);

        assertEquals("Password reset failed", result);
        verify(userServiceClient, never()).resetUserPassword(any(ResetUserPasswordDto.class));
    }
}

