package com.example.emailservice.bootstrap;

import com.example.emailservice.model.CodeSender;
import com.example.emailservice.repository.CodeSenderRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@AllArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final CodeSenderRepository codeSenderRepository;
    @Override
    public void run(String... args) {

        CodeSender codeSender = CodeSender.builder()
                .code(123456)
                .email("marko@user.com")
                .date(System.currentTimeMillis())
                .active(false)
                .build();

        CodeSender codeSender2 = CodeSender.builder()
                .code(111111)
                .email("marko1@user.com")
                .date(System.currentTimeMillis())
                .active(false)
                .build();

        CodeSender codeSender3 = CodeSender.builder()
                .code(111112)
                .email("marko2@user.com")
                .date(System.currentTimeMillis())
                .active(false)
                .build();

        List<CodeSender> codeSenders = new ArrayList<>();
        codeSenders.add(codeSender);
        codeSenders.add(codeSender2);
        codeSenders.add(codeSender3);

        codeSenderRepository.saveAll(codeSenders);
    }
}
