//package com.example.bankservice;
//
//import com.example.bankservice.client.EmailServiceClient;
//import com.example.bankservice.client.UserServiceClient;
//import com.example.bankservice.domain.dto.*;
//import com.example.bankservice.domain.model.Transaction;
//import com.example.bankservice.domain.model.enums.TransactionState;
//import com.example.bankservice.repository.TransactionRepository;
//import com.example.bankservice.service.TransactionService;
//import io.cucumber.java.Before;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//
//@ExtendWith(MockitoExtension.class)
//class TransactionServiceTest {
//    @Mock
//    private UserServiceClient userServiceClient;
//
//    @Mock
//    private EmailServiceClient emailServiceClient;
//
//    @Mock
//    private TransactionRepository transactionRepository;
//    @InjectMocks
//    private TransactionService transactionService;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    public void testStartPaymentTransaction_Successful() {
//
//        TransactionDto dto = new TransactionDto();
//        ResponseEntity<String> successResponse = new ResponseEntity<>("Success", HttpStatus.OK);
//        long transactionId = 12345;
//        Transaction transaction = new Transaction();
//        transaction.setTransactionId(12345L);
//        when(userServiceClient.checkEnoughBalance(any(CheckEnoughBalanceDto.class))).thenReturn(successResponse);
//        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
//
//        // Izvršavanje metode koju testiramo
//        ResponseEntity<Long> result = transactionService.startPaymentTransaction(dto);
//
//        // Provera rezultata
//        assertEquals(HttpStatus.OK, result.getStatusCode());
//        assertEquals(Long.valueOf(transactionId), result.getBody());
//        verify(emailServiceClient, times(1)).sendTransactionActivationEmailToEmailService(any(TransactionActivationDto.class));
//    }
//
//    @Test
//    public void testStartPaymentTransaction_NotEnoughBalance() {
//        TransactionDto dto = new TransactionDto(/* Popunite polja za TransactionDto */);
//        ResponseEntity<String> errorResponse = new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
//        when(userServiceClient.checkEnoughBalance(any(CheckEnoughBalanceDto.class))).thenReturn(errorResponse);
//
//
//        assertThrows(ResponseStatusException.class, ()-> transactionService.startPaymentTransaction(dto));
//    }
//
//    @Test
//    public void testConfirmPaymentTransaction_Successful() {
//
//        long transactionId = 12345;
//        ConfirmTransactionDto confirmTransactionDto = new ConfirmTransactionDto(transactionId, 1234);
//        Transaction transaction = new Transaction();
//        transaction.setState(TransactionState.PENDING);
//        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
//
//        // Izvršavanje metode koju testiramo
//        ResponseEntity<String> result = transactionService.confirmPaymentTransaction(confirmTransactionDto);
//
//        // Provera rezultata
//        assertEquals(HttpStatus.OK, result.getStatusCode());
//        assertEquals("Transaction with id " + transactionId + " is confirmed", result.getBody());
//        assertEquals(TransactionState.ACCEPTED, transaction.getState());
//        verify(transactionRepository, times(1)).save(transaction);
//        verify(userServiceClient, times(1)).reserveMoney(new RebalanceAccountDto());
//    }
//
//    @Test
//    public void testConfirmTransaction_Payment_TransactionNotFound() {
//        // Priprema podataka za test
//        long transactionId = 12345;
//        ConfirmTransactionDto confirmTransactionDto = new ConfirmTransactionDto(transactionId, 1234);
//        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());
//
//        // Izvršavanje metode koju testiramo
//        ResponseEntity<String> result = transactionService.confirmPaymentTransaction(confirmTransactionDto);
//
//        // Provera rezultata
//        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
//        assertEquals("Transaction with id " + transactionId + " does not exist", result.getBody());
//        verify(transactionRepository, never()).save(any());
//        verify(userServiceClient, never()).reserveMoney(any());
//    }
//
//    @Test
//    public void testConfirmTransaction_Payment_TransactionNotPending() {
//        // Priprema podataka za test
//        long transactionId = 12345;
//        ConfirmTransactionDto confirmTransactionDto = new ConfirmTransactionDto(transactionId, 1234);
//        Transaction transaction = new Transaction(/* Popunite polja za Transaction */);
//        transaction.setState(TransactionState.ACCEPTED);
//        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
//
//        // Izvršavanje metode koju testiramo
//        ResponseEntity<String> result = transactionService.confirmPaymentTransaction(confirmTransactionDto);
//
//        // Provera rezultata
//        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
//        assertEquals("Transaction with id " + transactionId + " is not in PENDING state", result.getBody());
//        verify(transactionRepository, never()).save(any());
//        verify(userServiceClient, never()).reserveMoney(any());
//    }
//    @Test
//    public void testGetAllTransactions_WithValidData() {
//        // Priprema testnih podataka
//        String accountId = "123456";
//        List<Transaction> transactionsFrom = new ArrayList<>();
//        List<Transaction> transactionsTo = new ArrayList<>();
//        transactionsFrom.add(new Transaction());
//        transactionsTo.add(new Transaction());
//
//        // Podešavanje ponašanja mock-a
//        when(transactionRepository.findAllTransactionsByAccountFrom(accountId)).thenReturn(Optional.of(transactionsFrom));
//        when(transactionRepository.findAllTransactionsByAccountTo(accountId)).thenReturn(Optional.of(transactionsTo));
//
//        // Izvršavanje metode koju testiramo
//        List<TransactionDto> result = transactionService.getAllTransactions(accountId);
//
//        // Provera rezultata
//        assertNotNull(result);
//        assertEquals(2, result.size()); // Provera da li su svi podaci uspešno dobijeni
//    }
//
//    @Test
//    public void testGetAllTransactions_WithNoData() {
//
//        String accountId = "123456";
//
//        when(transactionRepository.findAllTransactionsByAccountFrom(accountId)).thenReturn(Optional.empty());
//        when(transactionRepository.findAllTransactionsByAccountTo(accountId)).thenReturn(Optional.empty());
//
//        List<TransactionDto> result = transactionService.getAllTransactions(accountId);
//
//        assertEquals(null, result); // Provera da li je vraćena null vrednost kada nema podataka
//    }
//    @Test
//    public void testProcessTransactions_NoAcceptedTransactions() {
//
//        when(transactionRepository.findByState(TransactionState.ACCEPTED)).thenReturn(Optional.empty());
//
//        transactionService.processTransactions();
//
//        verify(userServiceClient, never()).unreserveMoney(any(RebalanceAccountDto.class));
//        verify(userServiceClient, never()).takeMoneyFromAccount(any(RebalanceAccountDto.class));
//        verify(userServiceClient, never()).addMoneyToAccount(any(RebalanceAccountDto.class));
//        verify(transactionRepository, never()).save(any(Transaction.class));
//    }
//
//    @Test
//    public void testProcessTransactions_WithAcceptedTransactions() {
//
//        List<Transaction> transactions = createDummyTransactions();
//
//        when(transactionRepository.findByState(TransactionState.ACCEPTED)).thenReturn(Optional.of(transactions));
//
//        transactionService.processTransactions();
//
//
//        for (Transaction transaction : transactions) {
//            verify(transactionRepository, times(1)).save(transaction);
//        }
//    }
//    private List<Transaction> createDummyTransactions() {
//        List<Transaction> transactions = new ArrayList<>();
//
//        TransactionDto transactionDto1 = createDummyTransactionDto("123456789", "987654321", 100.0, "USD", 123456789.0, "ABC123", 1647774000L);
//        TransactionDto transactionDto2 = createDummyTransactionDto("987654321", "123456789", 50.0, "EUR", 987654321.0, "XYZ456", 1647775000L);
//        TransactionDto transactionDto3 = createDummyTransactionDto("111222333", "444555666", 200.0, "GBP", 111222333.0, "DEF789", 1647776000L);
//
//        transactions.add(convertDtoToTransaction(transactionDto1));
//        transactions.add(convertDtoToTransaction(transactionDto2));
//        transactions.add(convertDtoToTransaction(transactionDto3));
//
//        return transactions;
//    }
//
//    private TransactionDto createDummyTransactionDto(String accountFrom, String accountTo, Double amount, String currencyMark, Double sifraPlacanja, String pozivNaBroj, Long date) {
//        TransactionDto transactionDto = new TransactionDto();
//        transactionDto.setAccountFrom(accountFrom);
//        transactionDto.setAccountTo(accountTo);
//        transactionDto.setAmount(amount);
//        transactionDto.setCurrencyMark(currencyMark);
//        transactionDto.setSifraPlacanja(sifraPlacanja);
//        transactionDto.setPozivNaBroj(pozivNaBroj);
//        transactionDto.setDate(date);
//        return transactionDto;
//    }
//
//    private Transaction convertDtoToTransaction(TransactionDto transactionDto) {
//        Transaction transaction = new Transaction();
//        transaction.setAccountFrom(transactionDto.getAccountFrom());
//        transaction.setAccountTo(transactionDto.getAccountTo());
//        transaction.setAmount(transactionDto.getAmount());
//        transaction.setCurrencyMark(transactionDto.getCurrencyMark());
//        //transaction.setSifraPlacanja(Integer.parseInt(String.valueOf(transactionDto.getSifraPlacanja())));
//        transaction.setPozivNaBroj(transactionDto.getPozivNaBroj());
//        transaction.setDate(transactionDto.getDate());
//        return transaction;
//    }
//}