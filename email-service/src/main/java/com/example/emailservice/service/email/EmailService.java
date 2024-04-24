package com.example.emailservice.service.email;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
}
