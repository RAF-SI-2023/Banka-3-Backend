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

    private Random randomCodeGenerator = new Random();

    /**
     * Metoda na osnovu prosledjenog email-a kreira CodeSender objekat koji sadrzi nasumicno generisan aktivacioni kod
     * i cuva ga u bazi, a vraca ActivationCodeDto koji sadrzi sam kod.
     * @param email
     * @return ActivationCodeDto
     */
    @Override
    public ActivationCodeDto generateActivationCode(String email) {

        CodeSender codeSender = new CodeSender();
        codeSender.setEmail(email);
        codeSender.setCode(randomCodeGenerator.nextInt(100000, 999999));
        codeSender.setCreateTime(new Date().getTime());
        codeSenderRepository.save(codeSender);

        return new ActivationCodeDto(codeSender.getCode());
    }
}
