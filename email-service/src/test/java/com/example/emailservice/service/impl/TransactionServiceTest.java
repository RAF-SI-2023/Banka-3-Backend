package com.example.emailservice.service.impl;


import com.example.emailservice.dto.TransactionActivationDTO;
import com.example.emailservice.dto.ConfirmTransactionDTO;
import com.example.emailservice.model.TransactionActivation;
import com.example.emailservice.repository.TransactionActivationRepository;
import com.example.emailservice.service.EmailService;
import com.example.emailservice.service.impl.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionActivationRepository transactionActivationRepository;

    @Mock
    private EmailService emailService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void beginTransactionShouldSaveTransactionActivation() {
        TransactionActivationDTO dto = new TransactionActivationDTO();
        dto.setEmail("test@example.com");

        transactionService.beginTransaction(dto);

        verify(transactionActivationRepository, times(1)).save(any(TransactionActivation.class));
    }

    @Test
    public void confirmTransactionShouldReturnOkWhenTransactionIsValid() {
        ConfirmTransactionDTO dto = new ConfirmTransactionDTO();
        dto.setTransactionId(1L);
        dto.setCode(123456);

        TransactionActivation transactionActivation = new TransactionActivation(1L, "test@example.com", 123456, LocalDateTime.now(), true);

        when(transactionActivationRepository.findByIdAndActiveIsTrue(dto.getTransactionId())).thenReturn(Optional.of(transactionActivation));

        ResponseEntity<String> response = transactionService.confirmTransaction(dto);

        assertEquals(ResponseEntity.ok("Transaction is valid"), response);
    }

    @Test
    public void confirmTransactionShouldReturnBadRequestWhenTransactionIsInvalid() {
        ConfirmTransactionDTO dto = new ConfirmTransactionDTO();
        dto.setTransactionId(1L);
        dto.setCode(123456);

        when(transactionActivationRepository.findByIdAndActiveIsTrue(dto.getTransactionId())).thenReturn(Optional.empty());

        ResponseEntity<String> response = transactionService.confirmTransaction(dto);

        assertEquals(ResponseEntity.badRequest().build(), response);
    }
}
