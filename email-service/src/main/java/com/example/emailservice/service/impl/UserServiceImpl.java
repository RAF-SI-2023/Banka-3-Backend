package com.example.emailservice.service.impl;

import com.example.emailservice.dto.ActivationCodeDto;
import com.example.emailservice.model.CodeSender;
import com.example.emailservice.repository.CodeSenderRepository;
import com.example.emailservice.service.EmailService;
import com.example.emailservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private CodeSenderRepository codeSenderRepository;

    @Override
    public ActivationCodeDto generateActivationCode(String email) {

        CodeSender codeSender = new CodeSender();
        codeSender.setEmail(email);
        codeSender.setCode(new Random().nextInt(100000, 999999));
        codeSender.setCreateTime(new Date().getTime());
        codeSenderRepository.save(codeSender);

        return new ActivationCodeDto(codeSender.getCode());
    }
}
