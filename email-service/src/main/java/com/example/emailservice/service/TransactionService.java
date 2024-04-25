package com.example.emailservice.service;


import com.example.emailservice.client.BankServiceClient;
import com.example.emailservice.domain.dto.ConfirmTransactionDto;
import com.example.emailservice.domain.dto.FinalizeTransactionDto;
import com.example.emailservice.domain.dto.TransactionActivationDto;
import com.example.emailservice.domain.dto.bankService.TransactionFinishedDto;
import com.example.emailservice.domain.model.TransactionActivation;
import com.example.emailservice.repository.TransactionActivationRepository;
import com.example.emailservice.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import static java.lang.Thread.sleep;

@Service
@RequiredArgsConstructor
public class TransactionService {

    @Autowired
    private TransactionActivationRepository transactionActivationRepository;

    @Autowired
    private EmailService emailService;
    @Autowired
    private BankServiceClient bankServiceClient;

    public TransactionService(EmailService emailService,
                              TransactionActivationRepository transactionActivationRepository) {
        this.transactionActivationRepository = transactionActivationRepository;
        this.emailService = emailService;
    }

    public void beginTransaction(TransactionActivationDto dto) {
        Integer code = new Random().nextInt(100000, 999999);
        TransactionActivation transactionActivation = new TransactionActivation(null, dto.getEmail(), code,
                LocalDateTime.now(), true);
        transactionActivationRepository.save(transactionActivation);
        emailService.sendSimpleMessage(dto.getEmail(), getSubject(), getText(code));

        new Thread(() -> {
            long activationAvailableTime = 5 * 60 * 1000;
            try {
                sleep(activationAvailableTime);
                transactionActivation.setActive(false);
                transactionActivationRepository.save(transactionActivation);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }

    //validate transaction,return ResponseEntity with message
    public void confirmTransaction(ConfirmTransactionDto dto) {
        Optional<TransactionActivation> optional =
                transactionActivationRepository.findByCodeAndActiveIsTrue(dto.getCode());
        if (optional.isPresent()) {
            TransactionActivation transactionActivation = optional.get();
            if (transactionActivation.getCode() == (dto.getCode())) {
                transactionActivation.setActive(false);
                transactionActivationRepository.save(transactionActivation);
                bankServiceClient.confirmPaymentTransaction(new FinalizeTransactionDto(dto.getTransactionId()));
            } else {
                throw new RuntimeException("Invalid code");
            }
        } else {
            throw new RuntimeException("Transaction not found");
        }
    }

    public void sendTransactionFinishedEmail(TransactionFinishedDto transactionFinishedDto) {
        emailService.sendSimpleMessage(transactionFinishedDto.getEmail(),
                "Payment recieved", "You have recieved " +
                        transactionFinishedDto.getAmount() + " " +
                transactionFinishedDto.getCurrencyMark() + ".");
    }

    protected String getSubject() {
        return "Transaction activation";
    }

    protected String getText(Integer code) {
        return "Your activation code is:" + code;
    }


}
