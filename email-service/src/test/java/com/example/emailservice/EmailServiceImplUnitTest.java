package com.example.emailservice;

import com.example.emailservice.service.impl.EmailServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class EmailServiceImplUnitTest {

    @Mock
    private JavaMailSender javaMailSender;
    @InjectMocks
    private EmailServiceImpl emailService;

    @Test
    public void sendSimpleMessageTest() {
        String to = "email@gmail.com";
        String subject = "Test Subject";
        String text = "Test Message";

        emailService.sendSimpleMessage(to,subject,text);
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
        verifyNoMoreInteractions(javaMailSender);
    }
}
