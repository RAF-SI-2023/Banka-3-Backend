package com.example.bankservice;

import com.example.bankservice.client.UserServiceClient;
import com.example.bankservice.domains.dto.RebalanceAccountDto;
import com.example.bankservice.domains.dto.StockTransactionDto;
import com.example.bankservice.domains.model.StockTransaction;
import com.example.bankservice.domains.model.enums.TransactionState;
import com.example.bankservice.repositories.StockTransactionRepository;
import com.example.bankservice.services.StockTransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockTransactionServiceTest {
    @Mock
    private StockTransactionRepository stockTransactionRepository;

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private StockTransactionService stockTransactionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testStartStockTransaction_InsufficientBalance() {

        StockTransactionDto dto = createDummyStockTransactionDto();
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Insufficient balance", HttpStatus.BAD_REQUEST);
        when(userServiceClient.checkCompanyBalance(any())).thenReturn(responseEntity);

        try {
            stockTransactionService.startStockTransaction(dto);
        } catch (ResponseStatusException e) {
            verify(stockTransactionRepository).save(any());
            return;
        }

        fail("Expected ResponseStatusException");
    }

    @Test
    public void testStartStockTransaction_SuccessfulTransaction() {

        StockTransactionDto dto = createDummyStockTransactionDto();
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        when(userServiceClient.checkCompanyBalance(any())).thenReturn(responseEntity);

        stockTransactionService.startStockTransaction(dto);

        verify(stockTransactionRepository).save(any());

        verify(userServiceClient).reserveCompanyMoney(any());
    }

    @Test
    public void testProcessStockTransactions_NoAcceptedTransactions() {

        when(stockTransactionRepository.findByState(TransactionState.ACCEPTED)).thenReturn(null);

        stockTransactionService.processStockTransactions();

        verify(userServiceClient, never()).unreserveCompanyMoney(any(RebalanceAccountDto.class));
        verify(userServiceClient, never()).takeMoneyFromCompanyAccount(any(RebalanceAccountDto.class));
        verify(userServiceClient, never()).addMoneyToCompanyAccount(any(RebalanceAccountDto.class));
    }


    private StockTransactionDto createDummyStockTransactionDto() {
        StockTransactionDto dto = new StockTransactionDto();
        dto.setAccountFrom("sourceAccount");
        dto.setAccountTo("destinationAccount");
        dto.setAmount(100.0);
        return dto;
    }
}