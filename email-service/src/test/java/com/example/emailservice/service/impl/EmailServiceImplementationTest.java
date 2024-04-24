package com.example.emailservice.service.impl;

import com.example.emailservice.service.email.EmailServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceImplementationTest {

    @Mock
    private JavaMailSender javaMailSender;
    @InjectMocks
    private EmailServiceImplementation emailService;

    @Test
    public void sendSimpleMessageTest() {
        String to = "email@gmail.com";
        String subject = "Test Subject";
        String text = "Test Message";

        emailService.sendSimpleMessage(to, subject, text);
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
        verifyNoMoreInteractions(javaMailSender);
    }
}