package com.example.emailservice;

import com.example.emailservice.dto.ActivationCodeDto;
import com.example.emailservice.model.CodeSender;
import com.example.emailservice.repository.CodeSenderRepository;
import com.example.emailservice.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTests {

    @Mock
    private CodeSenderRepository codeSenderRepository;

    @Mock
    private Random randomCodeGenerator;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testGenerateActivationCode() {
        String email = "pere123@gmail.com";
        int code = 123456;

        when(randomCodeGenerator.nextInt(100000, 999999)).thenReturn(123456);

        CodeSender codeSender = new CodeSender();
        codeSender.setEmail(email);
        codeSender.setCode(randomCodeGenerator.nextInt(100000, 999999));

        given(codeSenderRepository.save(codeSender)).willReturn(codeSender);

        CodeSender savedCodeSender = codeSenderRepository.save(codeSender);

        assertEquals(email, savedCodeSender.getEmail());
        assertEquals(code, savedCodeSender.getCode());

        ActivationCodeDto activationCodeDto = userService.generateActivationCode(email);

        assertEquals(code, activationCodeDto.getCode());
    }

}
