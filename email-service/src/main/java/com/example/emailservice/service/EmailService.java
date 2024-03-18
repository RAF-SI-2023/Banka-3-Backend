package com.example.emailservice.service;

public interface EmailService {

    void sendSimpleMessage(String to, String subject, String text);
}
