package com.example.bankservice;

import com.example.bankservice.client.EmailServiceClient;
import com.example.bankservice.client.UserServiceClient;
import com.example.bankservice.domains.dto.*;
import com.example.bankservice.domains.model.Transaction;
import com.example.bankservice.domains.model.enums.TransactionState;
import com.example.bankservice.repositories.TransactionRepository;
import com.example.bankservice.services.TransactionService;
import io.cucumber.java.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private EmailServiceClient emailServiceClient;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStartTransaction_Successful() {

        TransactionDto dto = new TransactionDto();
        ResponseEntity<String> successResponse = new ResponseEntity<>("Success", HttpStatus.OK);
        long transactionId = 12345;
        Transaction transaction = new Transaction();
        transaction.setTransactionId(12345L);
        when(userServiceClient.checkEnoughBalance(any(CheckEnoughBalanceDto.class))).thenReturn(successResponse);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // Izvršavanje metode koju testiramo
        ResponseEntity<Long> result = transactionService.startTransaction(dto);

        // Provera rezultata
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(Long.valueOf(transactionId), result.getBody());
        verify(emailServiceClient, times(1)).sendTransactionActivationEmailToEmailService(any(TransactionActivationDto.class));
    }

    @Test
    public void testStartTransaction_NotEnoughBalance() {
        TransactionDto dto = new TransactionDto(/* Popunite polja za TransactionDto */);
        ResponseEntity<String> errorResponse = new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
        when(userServiceClient.checkEnoughBalance(any(CheckEnoughBalanceDto.class))).thenReturn(errorResponse);


        assertThrows(ResponseStatusException.class, ()-> transactionService.startTransaction(dto));
    }

    @Test
    public void testConfirmTransaction_Successful() {

        long transactionId = 12345;
        ConfirmTransactionDto confirmTransactionDto = new ConfirmTransactionDto(transactionId, 1234);
        Transaction transaction = new Transaction();
        transaction.setState(TransactionState.PENDING);
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        // Izvršavanje metode koju testiramo
        ResponseEntity<String> result = transactionService.confirmTransaction(confirmTransactionDto);

        // Provera rezultata
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Transaction with id " + transactionId + " is confirmed", result.getBody());
        assertEquals(TransactionState.ACCEPTED, transaction.getState());
        verify(transactionRepository, times(1)).save(transaction);
        verify(userServiceClient, times(1)).reserveMoney(new RebalanceAccountDto());
    }

    @Test
    public void testConfirmTransaction_TransactionNotFound() {
        // Priprema podataka za test
        long transactionId = 12345;
        ConfirmTransactionDto confirmTransactionDto = new ConfirmTransactionDto(transactionId, 1234);
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        // Izvršavanje metode koju testiramo
        ResponseEntity<String> result = transactionService.confirmTransaction(confirmTransactionDto);

        // Provera rezultata
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Transaction with id " + transactionId + " does not exist", result.getBody());
        verify(transactionRepository, never()).save(any());
        verify(userServiceClient, never()).reserveMoney(any());
    }

    @Test
    public void testConfirmTransaction_TransactionNotPending() {
        // Priprema podataka za test
        long transactionId = 12345;
        ConfirmTransactionDto confirmTransactionDto = new ConfirmTransactionDto(transactionId, 1234);
        Transaction transaction = new Transaction(/* Popunite polja za Transaction */);
        transaction.setState(TransactionState.ACCEPTED);
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        // Izvršavanje metode koju testiramo
        ResponseEntity<String> result = transactionService.confirmTransaction(confirmTransactionDto);

        // Provera rezultata
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Transaction with id " + transactionId + " is not in PENDING state", result.getBody());
        verify(transactionRepository, never()).save(any());
        verify(userServiceClient, never()).reserveMoney(any());
    }

//    @Test
//    public void testProcessTransactions() {
//        // Priprema podataka za test
//        List<Transaction> transactions = new ArrayList<>();
//        Transaction transaction1 = new Transaction();
//        Transaction transaction2 = new Transaction();
//        transactions.add(transaction1);
//        transactions.add(transaction2);
//        when(transactionRepository.findByState(TransactionState.ACCEPTED)).thenReturn(Optional.of(transactions));
//
//        // Izvršavanje metode koju testiramo
//        transactionService.processTransactions();
//
//        // Provera da li su transakcije obrađene
//        verify(transactionRepository, times(1)).findByState(TransactionState.ACCEPTED);
//
//        for (Transaction transaction : transactions) {
//            // Provera da li je stanje transakcije promenjeno u ACCEPTED
//            assertEquals(TransactionState.ACCEPTED, transaction.getState());
//            // Provera da li je pozvana finishTransaction metoda za svaku transakciju
//            verify(transactionService, times(1)).finishTransaction(transaction);
//        }
//
//        // Provera da li je pozvana save metoda nad transactionRepository-jem tačno jednom
//        verify(transactionRepository, times(1)).saveAll(transactions);
//    }
//    @Test
//    public void testGetAllTransactions_NoTransactions() {
//        // Priprema podataka za test
//        String accountId = "123";
//
//        // Podešavanje ponašanja mock TransactionRepository-ja
//        when(transactionRepository.findAllTransactionsByAccountFrom(accountId)).thenReturn(Optional.empty());
//        when(transactionRepository.findAllTransactionsByAccountTo(accountId)).thenReturn(Optional.empty());
//
//        // Izvršavanje metode koju testiramo
//        List<TransactionDto> result = transactionService.getAllTransactions(accountId);
//
//        // Provera rezultata
//        assertEquals(0, result.size());
//    }

//    @Test
//    public void testGetAllTransactions_TransactionsFromAccount() {
//        // Priprema podataka za test
//        String accountId = "123";
//        List<Transaction> transactions = new ArrayList<>();
//        transactions.add(new Transaction());
//
//        // Podešavanje ponašanja mock TransactionRepository-ja
//        when(transactionRepository.findAllTransactionsByAccountFrom(accountId)).thenReturn(Optional.of(transactions));
//        when(transactionRepository.findAllTransactionsByAccountTo(accountId)).thenReturn(Optional.empty());
//
//        // Izvršavanje metode koju testiramo
//        List<TransactionDto> result = transactionService.getAllTransactions(accountId);
//
//        // Provera rezultata
//        assertEquals(transactions.size(), result.size());
//        // Dodajte dodatne provere prema potrebi
//    }

}