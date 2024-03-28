package com.example.emailservice.service.impl;


import com.example.emailservice.dto.TransactionActivationDTO;
import com.example.emailservice.dto.ConfirmTransactionDTO;
import com.example.emailservice.model.TransactionActivation;
import com.example.emailservice.repository.TransactionActivationRepository;
import com.example.emailservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    public TransactionService(EmailService emailService,TransactionActivationRepository transactionActivationRepository) {
        this.transactionActivationRepository = transactionActivationRepository;
        this.emailService=emailService;
    }

    public void beginTransaction(TransactionActivationDTO dto){
        Integer code = new Random().nextInt(100000, 999999);
        TransactionActivation transactionActivation = new TransactionActivation(null,dto.getEmail(),code,LocalDateTime.now(), true);
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
    public ResponseEntity<String> confirmTransaction(ConfirmTransactionDTO dto){
        Optional<TransactionActivation> optional = transactionActivationRepository.findByIdAndActiveIsTrue(dto.getTransactionId());
        if(optional.isPresent()) {
            TransactionActivation transactionActivation = optional.get();
            if (transactionActivation.getCode() == (dto.getCode())) {
                transactionActivation.setActive(false);
                transactionActivationRepository.save(transactionActivation);
                return ResponseEntity.ok("Transaction is valid");
                //Obavestiti banka servis da je transakcija validna

            } else {
                return ResponseEntity.badRequest().build();
            }
        }else{
            return ResponseEntity.badRequest().build();
        }
    }


    protected String getSubject() {
        return "Transaction activation";
    }
    protected String getText(Integer code) {
        return "Your activation code is:" + code;
    }



}
