package com.example.emailservice.service.impl;


import com.example.emailservice.client.BankServiceClient;
import com.example.emailservice.domain.dto.ConfirmTransactionDto;
import com.example.emailservice.domain.dto.FinalizeTransactionDto;
import com.example.emailservice.domain.dto.TransactionActivationDto;
import com.example.emailservice.domain.dto.bankService.TransactionFinishedDto;
import com.example.emailservice.domain.model.TransactionActivation;

import com.example.emailservice.repository.TransactionActivationRepository;
import com.example.emailservice.service.email.EmailService;
import com.example.emailservice.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {


    @Mock
    private TransactionActivationRepository transactionActivationRepository;

    @Mock
    private BankServiceClient bankServiceClient;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void beginTransactionShouldSaveTransactionActivation() {
        TransactionActivationDto dto = new TransactionActivationDto();
        dto.setEmail("test@example.com");

        transactionService.beginTransaction(dto);

        verify(transactionActivationRepository, times(1)).save(any(TransactionActivation.class));
    }

    @Test
    public void testConfirmTransaction_Successful() {
        ConfirmTransactionDto dto = new ConfirmTransactionDto();
        dto.setTransactionId(123L);
        dto.setCode(123456); // valid code

        TransactionActivation transactionActivation = new TransactionActivation();
        transactionActivation.setId(123L);
        transactionActivation.setCode(123456);
        transactionActivation.setActive(true);

        when(transactionActivationRepository.findByIdAndActiveIsTrue(123L))
                .thenReturn(Optional.of(transactionActivation));

        transactionService.confirmTransaction(dto);

        assertFalse(transactionActivation.isActive());

        verify(bankServiceClient).confirmPaymentTransaction(new FinalizeTransactionDto(123L));
    }

    @Test
    public void testConfirmTransaction_InvalidCode() {
        ConfirmTransactionDto dto = new ConfirmTransactionDto();
        dto.setTransactionId(123L);
        dto.setCode(654321); // invalid code

        TransactionActivation transactionActivation = new TransactionActivation();
        transactionActivation.setId(123L);
        transactionActivation.setCode(123456);
        transactionActivation.setActive(true);

        when(transactionActivationRepository.findByIdAndActiveIsTrue(123L))
                .thenReturn(Optional.of(transactionActivation));

        assertThrows(RuntimeException.class, () -> transactionService.confirmTransaction(dto));

        assertTrue(transactionActivation.isActive());

        verifyNoInteractions(bankServiceClient);
    }

    @Test
    public void testConfirmTransaction_TransactionNotFound() {
        ConfirmTransactionDto dto = new ConfirmTransactionDto();
        dto.setTransactionId(123L);
        dto.setCode(123456); // valid code

        when(transactionActivationRepository.findByIdAndActiveIsTrue(123L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> transactionService.confirmTransaction(dto));

        verifyNoInteractions(bankServiceClient);
    }

    @Test
    public void testSendTransactionFinishedEmail() {
        TransactionFinishedDto dto = new TransactionFinishedDto();
        dto.setEmail("recipient@example.com");
        dto.setAmount(BigDecimal.valueOf(100.00));
        dto.setCurrencyMark("USD");
        transactionService.sendTransactionFinishedEmail(dto);

        verify(emailService).sendSimpleMessage("recipient@example.com",
                "Payment recieved", "You have recieved 100.0 USD.");
    }
}
