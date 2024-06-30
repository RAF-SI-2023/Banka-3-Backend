package com.example.bankservice;

import com.example.bankservice.client.EmailServiceClient;
import com.example.bankservice.client.UserServiceClient;
import com.example.bankservice.domain.dto.currencyExchange.CurrencyExchangeDto;
import com.example.bankservice.domain.dto.transaction.*;
import com.example.bankservice.domain.dto.userService.UserEmailDto;
import com.example.bankservice.domain.mapper.TransactionMapper;
import com.example.bankservice.domain.model.Currency;
import com.example.bankservice.domain.model.Transaction;
import com.example.bankservice.domain.model.accounts.Account;
import com.example.bankservice.domain.model.accounts.CompanyAccount;
import com.example.bankservice.domain.model.accounts.UserAccount;
import com.example.bankservice.domain.model.enums.CurrencyName;
import com.example.bankservice.domain.model.enums.TransactionStatus;
import com.example.bankservice.domain.model.enums.TransactionType;
import com.example.bankservice.domain.model.marginAccounts.CompanyMarginAccount;
import com.example.bankservice.domain.model.marginAccounts.MarginAccount;
import com.example.bankservice.domain.model.marginAccounts.UserMarginAccount;
import com.example.bankservice.repository.AccountRepository;
import com.example.bankservice.repository.MarginAccountRepository;
import com.example.bankservice.repository.TransactionRepository;
import com.example.bankservice.service.AccountService;
import com.example.bankservice.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Isolation;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;


import static com.example.bankservice.domain.model.enums.CurrencyMark.RSD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private MarginAccountRepository marginAccountRepository;
    @Mock
    private EmailServiceClient emailServiceClient;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private AccountService accountService;
    @Mock
    private TransactionMapper transactionMapper;
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService paymentTransactionService;

    private StockTransactionDto stockTransactionDto;
    private UserOtcTransactionDto userOtcTransactionDto;

    private CompanyOtcTransactionDto companyOtcTransactionDto;

    private Account accountFrom;
    private Account accountTo;

    private String accountNumber;

    private static final String ACCOUNT_NUMBER = "1234567890";


    @BeforeEach
    public void setUp() {
        stockTransactionDto = new StockTransactionDto();
        stockTransactionDto.setEmployeeId(1L);
        stockTransactionDto.setCurrencyMark("USD");
        stockTransactionDto.setAmount(100.0);

        userOtcTransactionDto = new UserOtcTransactionDto();
        userOtcTransactionDto.setUserFromId(1L);
        userOtcTransactionDto.setUserToId(2L);
        userOtcTransactionDto.setAmount(100.0);

        companyOtcTransactionDto = new CompanyOtcTransactionDto();
        companyOtcTransactionDto.setCompanyFromId(1L);
        companyOtcTransactionDto.setCompanyToId(2L);
        companyOtcTransactionDto.setAmount(100.0);

        accountFrom = new Account();
        accountFrom.setAccountNumber("12345");
        accountFrom.setAvailableBalance(BigDecimal.valueOf(200));
        accountFrom.setCurrency(new Currency(1L, CurrencyName.DINAR, "RSD"));

        accountTo = new Account();
        accountTo.setAccountNumber("67890");
        accountTo.setCurrency(new Currency(2L, CurrencyName.DINAR, "RSD"));

        accountNumber = "123456";
    }

    @Test
    void testOtcBank4Transaction() {
        when(accountService.findCompanyAccountForIdAndCurrency(1L, "RSD")).thenReturn(accountFrom);
        when(accountService.findCompanyAccountForIdAndCurrency(2L, "RSD")).thenReturn(accountTo);

        // To allow spying on transactionService to verify the private method call
        TransactionService transactionServiceSpy = spy(paymentTransactionService);

        transactionServiceSpy.otcBank4Transaction(companyOtcTransactionDto);

        verify(accountService, times(1)).findCompanyAccountForIdAndCurrency(1L, "RSD");
        verify(accountService, times(1)).findCompanyAccountForIdAndCurrency(2L, "RSD");
    }
    @Test
    void testOtcCompanyTransaction() {
        when(accountService.findCompanyAccountForIdAndCurrency(1L, "RSD")).thenReturn(accountFrom);
        when(accountService.findCompanyAccountForIdAndCurrency(2L, "RSD")).thenReturn(accountTo);

        // To allow spying on transactionService to verify the private method call
        TransactionService transactionServiceSpy = spy(paymentTransactionService);

        transactionServiceSpy.otcCompanyTransaction(companyOtcTransactionDto);

        verify(accountService, times(1)).findCompanyAccountForIdAndCurrency(1L, "RSD");
        verify(accountService, times(1)).findCompanyAccountForIdAndCurrency(2L, "RSD");
    }
    @Test
    void testOtcUserTransaction() {
        when(accountService.findUserAccountForIdAndCurrency(1L, "RSD")).thenReturn(accountFrom);
        when(accountService.findUserAccountForIdAndCurrency(2L, "RSD")).thenReturn(accountTo);

        paymentTransactionService.otcUserTransaction(userOtcTransactionDto);

        verify(accountService, times(1)).findUserAccountForIdAndCurrency(1L, "RSD");
        verify(accountService, times(1)).findUserAccountForIdAndCurrency(2L, "RSD");
        // verify startOTCTransaction is called correctly
        // we use reflection to access this method since it's not exposed by the service

        // Assuming startOTCTransaction is a public method of TransactionService
    }


    @Test
    public void testStockSellTransaction_Uspesno() {
        when(accountService.findExchangeAccountForGivenCurrency("USD")).thenReturn(accountFrom);
        when(accountService.findBankAccountForGivenCurrency("USD")).thenReturn(accountTo);

        paymentTransactionService.stockSellTransaction(stockTransactionDto);

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }


@Test
public void testGetAllPaymentTransactions_Success() {
    // Arrange
    Transaction transaction1 = new Transaction();
    transaction1.setDate(100l);
    transaction1.setTransactionStatus(TransactionStatus.FINISHED);

    Transaction transaction2 = new Transaction();
    transaction2.setDate(100l);
    transaction2.setTransactionStatus(TransactionStatus.FINISHED);

    List<Transaction> transactions = Arrays.asList(transaction1, transaction2);
    when(transactionRepository.findByAccountFromOrAccountTo(ACCOUNT_NUMBER, ACCOUNT_NUMBER)).thenReturn(Optional.of(transactions));

    FinishedPaymentTransactionDto dto1 = new FinishedPaymentTransactionDto();
    FinishedPaymentTransactionDto dto2 = new FinishedPaymentTransactionDto();
    when(transactionMapper.transactionToFinishedPaymentTransactionDto(transaction1)).thenReturn(dto1);
    when(transactionMapper.transactionToFinishedPaymentTransactionDto(transaction2)).thenReturn(dto2);

    // Act
    List<FinishedPaymentTransactionDto> result = paymentTransactionService.getAllPaymentTransactions(ACCOUNT_NUMBER);

    // Assert
    assertEquals(2, result.size());
    assertEquals(dto2, result.get(0)); // Proverava da li je transakcija2 prva zbog sortiranja po datumu
    assertEquals(dto1, result.get(1));
}

    @Test
    public void testGetAllPaymentTransactions_TransactionsNotFound() {
        // Arrange
        when(transactionRepository.findByAccountFromOrAccountTo(ACCOUNT_NUMBER, ACCOUNT_NUMBER)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            paymentTransactionService.getAllPaymentTransactions(ACCOUNT_NUMBER);
        });

        assertEquals("Transactions not found", exception.getMessage());
    }

    @Test
    public void testStockSellTransaction_NedovoljnoSredstava() {
        accountFrom.setAvailableBalance(BigDecimal.valueOf(50));  // Nedovoljno sredstava

        when(accountService.findExchangeAccountForGivenCurrency("USD")).thenReturn(accountFrom);

        assertThrows(RuntimeException.class, () -> {
            paymentTransactionService.stockSellTransaction(stockTransactionDto);
        });

        verify(transactionRepository, never()).save(any(Transaction.class));
    }

//    @Test
//    public void testStockSellTransaction_UserAccount() {
//        stockTransactionDto.setEmployeeId(null);
//
//        when(accountService.findExchangeAccountForGivenCurrency("USD")).thenReturn(accountFrom);
//        when(accountService.findAccount(stockTransactionDto)).thenReturn(accountTo);
//
//        paymentTransactionService.stockSellTransaction(stockTransactionDto);
//
//        verify(transactionRepository, times(1)).save(any(Transaction.class));
//    }

    @Test
    public void testStockBuyTransaction_Uspesno() {
        when(accountService.findBankAccountForGivenCurrency("USD")).thenReturn(accountFrom);
        when(accountService.findExchangeAccountForGivenCurrency("USD")).thenReturn(accountTo);

        paymentTransactionService.stockBuyTransaction(stockTransactionDto);

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void testStockBuyTransaction_NedovoljnoSredstava() {
        accountFrom.setAvailableBalance(BigDecimal.valueOf(50));  // Nedovoljno sredstava

        when(accountService.findBankAccountForGivenCurrency("USD")).thenReturn(accountFrom);

        assertThrows(RuntimeException.class, () -> {
            paymentTransactionService.stockBuyTransaction(stockTransactionDto);
        });

        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testStartPaymentTransaction_InsufficientFunds() {
        PaymentTransactionDto paymentTransactionDto = new PaymentTransactionDto();
        paymentTransactionDto.setAccountFrom("fromAccountNumber");
        paymentTransactionDto.setAccountTo("toAccountNumber");
        paymentTransactionDto.setAmount(100.0);

        Account accountFrom = new Account();
        accountFrom.setAccountNumber("fromAccountNumber");
        accountFrom.setAvailableBalance(new BigDecimal(50)); // Less than the amount to transfer
        accountFrom.setCurrency(new Currency(1l, CurrencyName.DOLLAR,"USD"));

        Account accountTo = new Account();
        accountTo.setAccountNumber("toAccountNumber");
        accountTo.setAvailableBalance(new BigDecimal(300));
        accountTo.setCurrency(new Currency(2l, CurrencyName.EURO, "EUR"));

        when(accountService.extractAccountForAccountNumber("fromAccountNumber")).thenReturn(accountFrom);
        when(accountService.extractAccountForAccountNumber("toAccountNumber")).thenReturn(accountTo);
        when(accountService.checkBalance("fromAccountNumber", 100.0)).thenReturn(false); // Insufficient funds

        assertThrows(RuntimeException.class, () -> paymentTransactionService.startPaymentTransaction(paymentTransactionDto));

        verify(accountService).extractAccountForAccountNumber("fromAccountNumber");
        verify(accountService).extractAccountForAccountNumber("toAccountNumber");
        verify(accountService).checkBalance("fromAccountNumber", 100.0);

        verifyNoMoreInteractions(accountService);
    }

    @Test
    void testStartPaymentTransaction_DifferentCurrencies() {
        PaymentTransactionDto paymentTransactionDto = new PaymentTransactionDto();
        paymentTransactionDto.setAccountFrom("fromAccountNumber");
        paymentTransactionDto.setAccountTo("toAccountNumber");
        paymentTransactionDto.setAmount(100.0);

        Account accountFrom = new Account();
        accountFrom.setAccountNumber("fromAccountNumber");
        accountFrom.setAvailableBalance(new BigDecimal(200));
        accountFrom.setCurrency(new Currency(1l, CurrencyName.DOLLAR,"USD"));

        Account accountTo = new Account();
        accountTo.setAccountNumber("toAccountNumber");
        accountTo.setAvailableBalance(new BigDecimal(300));
        accountTo.setCurrency(new Currency(2l, CurrencyName.EURO, "EUR"));

        when(accountService.extractAccountForAccountNumber("fromAccountNumber")).thenReturn(accountFrom);
        when(accountService.extractAccountForAccountNumber("toAccountNumber")).thenReturn(accountTo);
        when(accountService.checkBalance("fromAccountNumber", 100.0)).thenReturn(true); // Sufficient funds

        assertThrows(RuntimeException.class, () -> paymentTransactionService.startPaymentTransaction(paymentTransactionDto));

        verify(accountService).extractAccountForAccountNumber("fromAccountNumber");
        verify(accountService).extractAccountForAccountNumber("toAccountNumber");
        verify(accountService).checkBalance("fromAccountNumber", 100.0);

        verifyNoMoreInteractions(accountService);
    }

    @Test
    void testStartPaymentTransaction_SameCurrencies() {
        PaymentTransactionDto paymentTransactionDto = new PaymentTransactionDto();
        paymentTransactionDto.setAccountFrom("fromAccountNumber");
        paymentTransactionDto.setAccountTo("toAccountNumber");
        paymentTransactionDto.setAmount(100.0);

        UserAccount accountFrom = new UserAccount();
        accountFrom.setAccountNumber("fromAccountNumber");
        accountFrom.setAvailableBalance(new BigDecimal(200));
        accountFrom.setCurrency(new Currency(1l, CurrencyName.DOLLAR,"USD"));


        Account accountTo = new Account();
        accountTo.setAccountNumber("toAccountNumber");
        accountTo.setAvailableBalance(new BigDecimal(300));
        accountTo.setCurrency(new Currency(2l, CurrencyName.DOLLAR, "USD"));

        Transaction transaction = new Transaction();
        transaction.setTransactionId(1l);
        transaction.setDate(100l);
        transaction.setType(TransactionType.PAYMENT_TRANSACTION);
        transaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        transaction.setAmount(new BigDecimal(100));
        transaction.setAccountTo("toAccountNumber");
        transaction.setAccountFrom("fromAccountNumber");
        transaction.setPozivNaBroj("189");
        transaction.setSifraPlacanja(25);

        UserEmailDto userEmailDto = new UserEmailDto();
        userEmailDto.setUserId(1L);
        userEmailDto.setEmail("test@example.com");


        when(accountService.extractAccountForAccountNumber("fromAccountNumber")).thenReturn(accountFrom);
        when(accountService.extractAccountForAccountNumber("toAccountNumber")).thenReturn(accountTo);
        when(accountService.checkBalance("fromAccountNumber", 100.0)).thenReturn(true); // Sufficient funds
        when(transactionMapper.paymentTransactionDtoToTransaction(paymentTransactionDto)).thenReturn(transaction);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(userServiceClient.getEmailByUserId(anyString())).thenReturn(userEmailDto);
        when(emailServiceClient.sendTransactionActivationEmailToEmailService(any(PaymentTransactionActivationDto.class))).thenReturn(null);


        assertDoesNotThrow(() -> paymentTransactionService.startPaymentTransaction(paymentTransactionDto));

        verify(accountService).extractAccountForAccountNumber("fromAccountNumber");
        verify(accountService).extractAccountForAccountNumber("toAccountNumber");
        verify(accountService).checkBalance("fromAccountNumber", 100.0);

        verifyNoMoreInteractions(accountService);
    }
    @Test
    void testStartPaymentTransaction_SameCurrencies1() {
        PaymentTransactionDto paymentTransactionDto = new PaymentTransactionDto();
        paymentTransactionDto.setAccountFrom("fromAccountNumber");
        paymentTransactionDto.setAccountTo("toAccountNumber");
        paymentTransactionDto.setAmount(100.0);

        CompanyAccount accountFrom = new CompanyAccount();
        accountFrom.setAccountNumber("fromAccountNumber");
        accountFrom.setAvailableBalance(new BigDecimal(200));
        accountFrom.setCurrency(new Currency(1l, CurrencyName.DOLLAR,"USD"));


        Account accountTo = new Account();
        accountTo.setAccountNumber("toAccountNumber");
        accountTo.setAvailableBalance(new BigDecimal(300));
        accountTo.setCurrency(new Currency(2l, CurrencyName.DOLLAR, "USD"));

        Transaction transaction = new Transaction();
        transaction.setTransactionId(1l);
        transaction.setDate(100l);
        transaction.setType(TransactionType.PAYMENT_TRANSACTION);
        transaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        transaction.setAmount(new BigDecimal(100));
        transaction.setAccountTo("toAccountNumber");
        transaction.setAccountFrom("fromAccountNumber");
        transaction.setPozivNaBroj("189");
        transaction.setSifraPlacanja(25);



        when(accountService.extractAccountForAccountNumber("fromAccountNumber")).thenReturn(accountFrom);
        when(accountService.extractAccountForAccountNumber("toAccountNumber")).thenReturn(accountTo);
        when(accountService.checkBalance("fromAccountNumber", 100.0)).thenReturn(true); // Sufficient funds
        when(transactionMapper.paymentTransactionDtoToTransaction(paymentTransactionDto)).thenReturn(transaction);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(userServiceClient.getEmailByCompanyId(anyString())).thenReturn("test@example.com");
        when(emailServiceClient.sendTransactionActivationEmailToEmailService(any(PaymentTransactionActivationDto.class))).thenReturn(null);


        assertDoesNotThrow(() -> paymentTransactionService.startPaymentTransaction(paymentTransactionDto));

        verify(accountService).extractAccountForAccountNumber("fromAccountNumber");
        verify(accountService).extractAccountForAccountNumber("toAccountNumber");
        verify(accountService).checkBalance("fromAccountNumber", 100.0);

        verifyNoMoreInteractions(accountService);
    }

    @Test
    public void testConfirmPaymentTransaction_PendingTransaction() {
        ConfirmPaymentTransactionDto confirmDto = new ConfirmPaymentTransactionDto();
        confirmDto.setTransactionId(1L);

        Transaction transaction = new Transaction();
        transaction.setTransactionId(1L);
        transaction.setTransactionStatus(TransactionStatus.PENDING);
        transaction.setAmount(new BigDecimal(200)); // Dodao sam iznos transakcije
        transaction.setAccountFrom("fromAccountNumber");

        when(transactionRepository.findById(1L)).thenReturn(java.util.Optional.of(transaction));

        Account accountFrom = new Account();
        accountFrom.setAccountNumber("fromAccountNumber");
        accountFrom.setAvailableBalance(new BigDecimal(300));
        accountFrom.setCurrency(new Currency(2L, CurrencyName.DOLLAR, "USD"));

        when(accountService.extractAccountForAccountNumber("fromAccountNumber")).thenReturn(accountFrom);


        doThrow(new RuntimeException()).when(accountService).reserveFunds(eq(accountFrom), any(BigDecimal.class));

        assertThrows(RuntimeException.class, () -> paymentTransactionService.confirmPaymentTransaction(confirmDto));

        verify(transactionRepository).findById(1L);
        verify(accountService).extractAccountForAccountNumber("fromAccountNumber");
        verify(accountService).reserveFunds(eq(accountFrom), any(BigDecimal.class));
    }


    @Test
    public void testConfirmPaymentTransaction_CompletedTransaction() {
        ConfirmPaymentTransactionDto confirmDto = new ConfirmPaymentTransactionDto();
        confirmDto.setTransactionId(1L);

        Transaction transaction = new Transaction();
        transaction.setTransactionId(1L);
        transaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);

        when(transactionRepository.findById(1L)).thenReturn(java.util.Optional.of(transaction));


        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            paymentTransactionService.confirmPaymentTransaction(confirmDto);
        });
        assertEquals("Transaction already completed", exception.getMessage());

        verify(transactionRepository).findById(1L);
        verifyNoMoreInteractions(transactionRepository);
    }
    @Test
    public void testStartCurrencyExchangeTransaction_InsufficientFunds() {
        MockitoAnnotations.initMocks(this);

        String accountFrom = "account123";
        String accountTo = "account456";
        double amount = 100.0;
        CurrencyExchangeDto currencyExchangeDto = new CurrencyExchangeDto(accountFrom, accountTo, amount);

        Account mockAccountFrom = new Account();
        mockAccountFrom.setAvailableBalance(BigDecimal.valueOf(50.0)); // Postavimo stanje računa na manje od traženog iznosa
        when(accountService.extractAccountForAccountNumber(accountFrom)).thenReturn(mockAccountFrom);
        when(accountService.extractAccountForAccountNumber(accountTo)).thenReturn(new Account());

        assertThrows(RuntimeException.class, () -> paymentTransactionService.startCurrencyExchangeTransaction(currencyExchangeDto));
    }

    @Test
    public void testStartCurrencyExchangeTransaction_DifferentCurrencies() {

        CurrencyExchangeDto currencyExchangeDto = new CurrencyExchangeDto();
        currencyExchangeDto.setAccountFrom("fromAccountNumber");
        currencyExchangeDto.setAccountTo("toAccountNumber");
        currencyExchangeDto.setAmount(100.0);

        Account accountFrom = new Account();
        accountFrom.setAccountNumber("fromAccountNumber");
        accountFrom.setAvailableBalance(new BigDecimal(200));
        accountFrom.setCurrency(new Currency(1l, CurrencyName.DOLLAR,"USD"));

        Account accountTo = new Account();
        accountTo.setAccountNumber("toAccountNumber");
        accountTo.setAvailableBalance(new BigDecimal(300));
        accountTo.setCurrency(new Currency(2l, CurrencyName.EURO, "EUR"));

        when(accountService.extractAccountForAccountNumber("fromAccountNumber")).thenReturn(accountFrom);
        when(accountService.extractAccountForAccountNumber("toAccountNumber")).thenReturn(accountTo);
        when(accountService.checkBalance("fromAccountNumber", 100.0)).thenReturn(true);


        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            paymentTransactionService.startCurrencyExchangeTransaction(currencyExchangeDto);
        });

        verify(accountService).checkBalance("fromAccountNumber", 100.0);
        assertEquals("Different currency transactions are not supported", exception.getMessage());
    }

    @Test
    public void testProcessTransactions_NoTransactionsToProcess() {
        MockitoAnnotations.initMocks(this);

        when(transactionRepository.findAllByTransactionStatus(TransactionStatus.ACCEPTED)).thenReturn(Optional.empty());

        paymentTransactionService.processTransactions();

        verify(transactionRepository, times(1)).findAllByTransactionStatus(TransactionStatus.ACCEPTED);
        verifyNoMoreInteractions(transactionRepository);
    }
    @Test
    void testProcessTransactions() {
        Transaction transaction1 = new Transaction();
        transaction1.setType(TransactionType.CREDIT_APPROVE_TRANSACTION);
        transaction1.setTransactionStatus(TransactionStatus.ACCEPTED);
        transaction1.setAccountFrom("123456");
        transaction1.setAccountTo("789012");
        transaction1.setAmount(new BigDecimal("100.00"));


        Transaction transaction2 = new Transaction();
        transaction2.setType(TransactionType.PAYMENT_TRANSACTION);
        transaction2.setTransactionStatus(TransactionStatus.ACCEPTED);
        transaction2.setAccountFrom("123456");
        transaction2.setAccountTo("789012");
        transaction2.setAmount(new BigDecimal("100.00"));

        Account accountFrom = new Account();
        accountFrom.setAccountNumber("123456");
        Account accountTo = new Account();
        accountTo.setAccountNumber("789012");

        when(transactionRepository.findAllByTransactionStatus(TransactionStatus.ACCEPTED))
                .thenReturn(Optional.of(Arrays.asList(transaction1, transaction2)));

        when(accountRepository.findByAccountNumber("123456")).thenReturn(Optional.of(accountFrom));
        when(accountRepository.findByAccountNumber("789012")).thenReturn(Optional.of(accountTo));

        paymentTransactionService.processTransactions();

        verify(transactionRepository).findAllByTransactionStatus(TransactionStatus.ACCEPTED);
        assertEquals(TransactionStatus.FINISHED, transaction1.getTransactionStatus());
        assertEquals(TransactionStatus.FINISHED, transaction2.getTransactionStatus());
        verify(transactionRepository, times(1)).save(transaction1);
        verify(transactionRepository, times(1)).save(transaction2);

    }
//    @Test
//    void testGetAllPaymentTransactions() {
//        Transaction transaction1 = new Transaction();
//        transaction1.setType(TransactionType.CREDIT_APPROVE_TRANSACTION);
//        transaction1.setTransactionStatus(TransactionStatus.ACCEPTED);
//        transaction1.setAccountFrom("123456");
//        transaction1.setAccountTo("789012");
//        transaction1.setAmount(new BigDecimal("100.00"));
//        transaction1.setTransactionId(1L);
//        transaction1.setPozivNaBroj("Da");
//        transaction1.setDate(100L);
//        transaction1.setSifraPlacanja(125);
//
//        Transaction transaction2 = new Transaction();
//        transaction2.setType(TransactionType.CREDIT_APPROVE_TRANSACTION);
//        transaction2.setTransactionStatus(TransactionStatus.ACCEPTED);
//        transaction2.setAccountFrom("123456");
//        transaction2.setAccountTo("789012");
//        transaction2.setAmount(new BigDecimal("100.00"));
//        transaction1.setTransactionId(2L);
//        transaction1.setPozivNaBroj("Da");
//        transaction1.setDate(100L);
//        transaction1.setSifraPlacanja(125);
//
//        List<Transaction> transactions = new ArrayList<>();
//        transactions.add(transaction1);
//        transactions.add(transaction2);
//
//        PaymentTransactionDto paymentTransactionDto1 = new PaymentTransactionDto();
//        PaymentTransactionDto paymentTransactionDto2 = new PaymentTransactionDto();
//
//
//        List<PaymentTransactionDto> expectedDtoList = List.of(paymentTransactionDto1, paymentTransactionDto2);
//
//        when(transactionRepository.findAllByType(TransactionType.CREDIT_APPROVE_TRANSACTION))
//                .thenReturn(Optional.of(transactions));
//
//        when(transactionMapper.transactionToPaymentTransactionDto(transaction1)).thenReturn(paymentTransactionDto1);
//        when(transactionMapper.transactionToPaymentTransactionDto(transaction2)).thenReturn(paymentTransactionDto2);
//
//        List<FinishedPaymentTransactionDto> actualDtoList = paymentTransactionService.getAllPaymentTransactions("123456");
//
//        assertEquals(expectedDtoList, actualDtoList);
//
//        verify(transactionRepository).findAllByType(TransactionType.CREDIT_APPROVE_TRANSACTION);
//        verify(transactionMapper).transactionToPaymentTransactionDto(transaction1);
//        verify(transactionMapper).transactionToPaymentTransactionDto(transaction2);
//    }
    @Test
    void testGetAllCreditTransactions() {
        Transaction transaction1 = new Transaction();
        transaction1.setType(TransactionType.CREDIT_APPROVE_TRANSACTION);
        transaction1.setTransactionStatus(TransactionStatus.ACCEPTED);
        transaction1.setAccountFrom("123456");
        transaction1.setAccountTo("789012");
        transaction1.setAmount(new BigDecimal("100.00"));
        transaction1.setTransactionId(1L);
        transaction1.setPozivNaBroj("Da");
        transaction1.setDate(100L);
        transaction1.setSifraPlacanja(125);

        Transaction transaction2 = new Transaction();
        transaction2.setType(TransactionType.CREDIT_APPROVE_TRANSACTION);
        transaction2.setTransactionStatus(TransactionStatus.ACCEPTED);
        transaction2.setAccountFrom("123456");
        transaction2.setAccountTo("789012");
        transaction2.setAmount(new BigDecimal("100.00"));
        transaction1.setTransactionId(2L);
        transaction1.setPozivNaBroj("Da");
        transaction1.setDate(100L);
        transaction1.setSifraPlacanja(125);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);

        CreditTransactionDto creditTransactionDto1 = new CreditTransactionDto();
        CreditTransactionDto creditTransactionDto2 = new CreditTransactionDto();


        List<CreditTransactionDto> expectedDtoList = List.of(creditTransactionDto1, creditTransactionDto2);

        when(transactionRepository.findAllByType(TransactionType.CREDIT_APPROVE_TRANSACTION))
                .thenReturn(Optional.of(transactions));

        when(transactionMapper.transactionToCreditTransactionDto(transaction1)).thenReturn(creditTransactionDto1);
        when(transactionMapper.transactionToCreditTransactionDto(transaction2)).thenReturn(creditTransactionDto2);

        List<CreditTransactionDto> actualDtoList = paymentTransactionService.getAllCreditTransactions();

        assertEquals(expectedDtoList, actualDtoList);

        verify(transactionRepository).findAllByType(TransactionType.CREDIT_APPROVE_TRANSACTION);
        verify(transactionMapper).transactionToCreditTransactionDto(transaction1);
        verify(transactionMapper).transactionToCreditTransactionDto(transaction2);
    }

    @Test
    public void testStockSellMarginTransaction_InactiveAccount() {
        // Given
        StockMarginTransactionDto dto = new StockMarginTransactionDto();
        dto.setAmount(100.0); // Set amount

        MarginAccount marginAccount = new MarginAccount();
        marginAccount.setActive(false); // Set account as inactive

        when(accountService.findMarginAccount(dto)).thenReturn(marginAccount);

        // When / Then
        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> paymentTransactionService.stockSellMarginTransaction(dto)
        );

        assertEquals("Margin account is not active", exception.getMessage());
        verify(transactionRepository, never()).save(any()); // Verify that transactionRepository.save was never called
        verify(marginAccountRepository, never()).save(any()); // Verify that marginAccountRepository.save was never called
    }
    @Test
    public void testStockSellMarginTransaction_Success() {
        // Given
        StockMarginTransactionDto dto = new StockMarginTransactionDto();
        dto.setAmount(100.0); // Set amount

        MarginAccount marginAccount = new MarginAccount();
        marginAccount.setActive(true);
        marginAccount.setBankParticipation(0.1); // Example bank participation

        Account exchangeAccount = new Account();
        exchangeAccount.setAccountNumber("EXCHANGE_ACCOUNT");

        Account bankAccount = new Account();
        bankAccount.setAccountNumber("BANK_ACCOUNT");

        when(accountService.findMarginAccount(dto)).thenReturn(marginAccount);
        when(accountService.findExchangeAccountForGivenCurrency("RSD")).thenReturn(exchangeAccount);
        when(accountService.findBankAccountForGivenCurrency("RSD")).thenReturn(bankAccount);

        // When
        paymentTransactionService.stockSellMarginTransaction(dto);

        // Then
        verify(transactionRepository, times(2)).save(any()); // Verify that transactionRepository.save was called twice
        verify(marginAccountRepository, times(1)).save(any()); // Verify that marginAccountRepository.save was called once
    }
    @Test
    public void testWithdrawFromMarginCompany_Success() {
        // Given
        WithdrawFromMarginDto dto = new WithdrawFromMarginDto();
        dto.setAmount(500.0); // Set amount

        Long companyId = 1L; // Example company ID

        CompanyMarginAccount account = new CompanyMarginAccount();
        account.setActive(true);
        account.setInitialMargin(BigDecimal.valueOf(1000.0)); // Example initial margin
        account.setMaintenanceMargin(BigDecimal.valueOf(500.0)); // Example maintenance margin

        Account companyRSDAccount = new Account();
        companyRSDAccount.setAccountNumber("COMPANY_RSD_ACCOUNT");

        when(marginAccountRepository.findCompanyMarginAccountByCompanyId(companyId)).thenReturn(Optional.of(account));
        when(accountService.findCompanyAccountForIdAndCurrency(companyId, "RSD")).thenReturn(companyRSDAccount);

        // When
        paymentTransactionService.withdrawFromMarginCompany(dto, companyId);

        // Then
        verify(transactionRepository, times(1)).save(any()); // Verify that transactionRepository.save was called once
        verify(marginAccountRepository, times(1)).save(any()); // Verify that marginAccountRepository.save was called once
    }
    @Test
    public void testWithdrawFromMarginCompany_InsufficientFunds() {
        // Given
        WithdrawFromMarginDto dto = new WithdrawFromMarginDto();
        dto.setAmount(1500.0); // Set amount greater than available initial margin

        Long companyId = 1L; // Example company ID

        CompanyMarginAccount account = new CompanyMarginAccount();
        account.setActive(true);
        account.setInitialMargin(BigDecimal.valueOf(1000.0)); // Example initial margin
        account.setMaintenanceMargin(BigDecimal.valueOf(500.0)); // Example maintenance margin

        when(marginAccountRepository.findCompanyMarginAccountByCompanyId(companyId)).thenReturn(Optional.of(account));

        // When / Then
        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> paymentTransactionService.withdrawFromMarginCompany(dto, companyId)
        );

        assertEquals("Insufficient funds", exception.getMessage());
        verify(transactionRepository, never()).save(any()); // Verify that transactionRepository.save was never called
        verify(marginAccountRepository, never()).save(any()); // Verify that marginAccountRepository.save was never called
    }

    @Test
    public void testWithdrawFromMarginCompany_InactiveAccount() {
        // Given
        WithdrawFromMarginDto dto = new WithdrawFromMarginDto();
        dto.setAmount(500.0); // Set amount

        Long companyId = 1L; // Example company ID

        CompanyMarginAccount account = new CompanyMarginAccount();
        account.setActive(false); // Set account as inactive

        when(marginAccountRepository.findCompanyMarginAccountByCompanyId(companyId)).thenReturn(Optional.of(account));

        // When / Then
        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> paymentTransactionService.withdrawFromMarginCompany(dto, companyId)
        );

        assertEquals("Margin account is not active", exception.getMessage());
        verify(transactionRepository, never()).save(any()); // Verify that transactionRepository.save was never called
        verify(marginAccountRepository, never()).save(any()); // Verify that marginAccountRepository.save was never called
    }

//    @Test
//    public void testAddToMarginCompany_Success() {
//        // Given
//        AddToMarginDto dto = new AddToMarginDto();
//        dto.setAmount(1000.0); // Set amount
//
//        Long companyId = 1L; // Example company ID
//
//        CompanyMarginAccount account = new CompanyMarginAccount();
//        account.setActive(true);
//        account.setInitialMargin(BigDecimal.valueOf(500.0)); // Example initial margin
//        account.setMaintenanceMargin(BigDecimal.valueOf(1000.0)); // Example maintenance margin
//
//        Account companyRSDAccount = new Account();
//        companyRSDAccount.setAccountNumber("COMPANY_RSD_ACCOUNT");
//
//        when(accountService.checkBalanceCompany(companyId, dto.getAmount())).thenReturn(false); // Mock insufficient funds check
//        when(marginAccountRepository.findCompanyMarginAccountByCompanyId(companyId)).thenReturn(Optional.of(account));
//        when(accountService.findCompanyAccountForIdAndCurrency(companyId, "RSD")).thenReturn(companyRSDAccount);
//
//        // When
//        paymentTransactionService.addToMarginCompany(dto, companyId);
//
//        // Then
//        verify(transactionRepository, times(1)).save(any()); // Verify that transactionRepository.save was called once
//        verify(marginAccountRepository, times(1)).save(any()); // Verify that marginAccountRepository.save was called once
//    }
    @Test
    public void testAddToMarginCompany_InsufficientFunds() {
        // Given
        AddToMarginDto dto = new AddToMarginDto();
        dto.setAmount(2000.0); // Set amount greater than available balance

        Long companyId = 1L; // Example company ID

        when(accountService.checkBalanceCompany(companyId, dto.getAmount())).thenReturn(false); // Mock insufficient funds check

        // When / Then
        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> paymentTransactionService.addToMarginCompany(dto, companyId)
        );

        assertEquals("Insufficient funds", exception.getMessage());
        verify(transactionRepository, never()).save(any()); // Verify that transactionRepository.save was never called
        verify(marginAccountRepository, never()).save(any()); // Verify that marginAccountRepository.save was never called
    }
    @Test
    public void testWithdrawFromMarginUser_Success() {
        // Given
        WithdrawFromMarginDto dto = new WithdrawFromMarginDto();
        dto.setAmount(500.0); // Set amount

        Long userId = 1L; // Example user ID

        UserMarginAccount account = new UserMarginAccount();
        account.setActive(true);
        account.setInitialMargin(BigDecimal.valueOf(1000.0)); // Example initial margin
        account.setMaintenanceMargin(BigDecimal.valueOf(500.0)); // Example maintenance margin

        Account userRSDAccount = new Account();
        userRSDAccount.setAccountNumber("USER_RSD_ACCOUNT");

        when(marginAccountRepository.findUserMarginAccountByUserId(userId)).thenReturn(Optional.of(account));
        when(accountService.findUserAccountForIdAndCurrency(userId, "RSD")).thenReturn(userRSDAccount);

        // When
        paymentTransactionService.withdrawFromMarginUser(dto, userId);

        // Then
        verify(transactionRepository, times(1)).save(any()); // Verify that transactionRepository.save was called once
        verify(marginAccountRepository, times(1)).save(any()); // Verify that marginAccountRepository.save was called once
    }
    @Test
    public void testWithdrawFromMarginUser_InsufficientFunds() {
        // Given
        WithdrawFromMarginDto dto = new WithdrawFromMarginDto();
        dto.setAmount(1500.0); // Set amount greater than available initial margin

        Long userId = 1L; // Example user ID

        UserMarginAccount account = new UserMarginAccount();
        account.setActive(true);
        account.setInitialMargin(BigDecimal.valueOf(1000.0)); // Example initial margin
        account.setMaintenanceMargin(BigDecimal.valueOf(500.0)); // Example maintenance margin

        when(marginAccountRepository.findUserMarginAccountByUserId(userId)).thenReturn(Optional.of(account));

        // When / Then
        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> paymentTransactionService.withdrawFromMarginUser(dto, userId)
        );

        assertEquals("Insufficient funds", exception.getMessage());
        verify(transactionRepository, never()).save(any()); // Verify that transactionRepository.save was never called
        verify(marginAccountRepository, never()).save(any()); // Verify that marginAccountRepository.save was never called
    }
    @Test
    public void testStockBuyMarginTransaction_Success() {
        // Given
        StockMarginTransactionDto dto = new StockMarginTransactionDto();
        dto.setAmount(1000.0); // Set amount

        MarginAccount marginAccount = new MarginAccount();
        marginAccount.setActive(true);
        marginAccount.setInitialMargin(BigDecimal.valueOf(2000.0)); // Example initial margin
        marginAccount.setMaintenanceMargin(BigDecimal.valueOf(1000.0)); // Example maintenance margin
        marginAccount.setBankParticipation(0.1); // Example bank participation

        Account exchangeAccount = new Account();
        exchangeAccount.setAccountNumber("EXCHANGE_RSD_ACCOUNT");

        Account bankAccount = new Account();
        bankAccount.setAccountNumber("BANK_RSD_ACCOUNT");

        when(accountService.findMarginAccount(dto)).thenReturn(marginAccount);
        when(accountService.findExchangeAccountForGivenCurrency("RSD")).thenReturn(exchangeAccount);
        when(accountService.findBankAccountForGivenCurrency("RSD")).thenReturn(bankAccount);

        // When
        paymentTransactionService.stockBuyMarginTransaction(dto);

        // Then
        verify(transactionRepository, times(2)).save(any()); // Verify that transactionRepository.save was called twice
        verify(marginAccountRepository, times(1)).save(any()); // Verify that marginAccountRepository.save was called once
    }
    @Test
    void testAddToMarginUser_InsufficientFunds() {
        AddToMarginDto dto = new AddToMarginDto();
        dto.setAmount(1000.0);

        when(accountService.checkBalanceUser(anyLong(), anyDouble())).thenReturn(false);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            paymentTransactionService.addToMarginUser(dto, 1L);
        });

        assertEquals("Insufficient funds", thrown.getMessage());
    }

    @Test
    void testAddToMarginUser_MarginAccountNotFound() {
        AddToMarginDto dto = new AddToMarginDto();
        dto.setAmount(1000.0);

        when(accountService.checkBalanceUser(anyLong(), anyDouble())).thenReturn(true);
        when(marginAccountRepository.findUserMarginAccountByUserId(anyLong())).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            paymentTransactionService.addToMarginUser(dto, 1L);
        });

        assertEquals("Margin account not found", thrown.getMessage());
    }

    @Test
    void testAddToMarginUser_Success() {
        AddToMarginDto dto = new AddToMarginDto();
        dto.setAmount(1000.0);

        UserMarginAccount marginAccount = new UserMarginAccount();
        marginAccount.setActive(false);
        marginAccount.setInitialMargin(BigDecimal.valueOf(500));
        marginAccount.setMaintenanceMargin(BigDecimal.valueOf(1200));

        when(accountService.checkBalanceUser(anyLong(), anyDouble())).thenReturn(true);
        when(marginAccountRepository.findUserMarginAccountByUserId(anyLong())).thenReturn(Optional.of(marginAccount));

        Account rsdAccount = new Account();
        rsdAccount.setAccountNumber("12345");

        when(accountService.findUserAccountForIdAndCurrency(anyLong(), eq("RSD"))).thenReturn(rsdAccount);

        paymentTransactionService.addToMarginUser(dto, 1L);

        assertTrue(marginAccount.isActive());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(marginAccountRepository, times(1)).save(marginAccount);
    }

    @Test
    void testGetAllMarginTransactions_TransactionsNotFound() {
        when(transactionRepository.findByAccountFromOrAccountToAndType(anyString(), anyString(), eq(TransactionType.MARGIN_TRANSACTION)))
                .thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            paymentTransactionService.getAllMarginTransactions("12345");
        });

        assertEquals("Transactions not found", thrown.getMessage());
    }

    @Test
    void testGetAllMarginTransactions_Success() {
        String accountNumber = "12345";
        List<Transaction> transactions = new ArrayList<>();

        Transaction transaction1 = new Transaction();
        transaction1.setAccountFrom("12345");
        transaction1.setAccountTo("67890");
        transaction1.setAmount(BigDecimal.valueOf(1000));
        transaction1.setType(TransactionType.MARGIN_TRANSACTION);
        transaction1.setDate(System.currentTimeMillis());
        transactions.add(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setAccountFrom("12345");
        transaction2.setAccountTo("67891");
        transaction2.setAmount(BigDecimal.valueOf(2000));
        transaction2.setType(TransactionType.MARGIN_TRANSACTION);
        transaction2.setDate(System.currentTimeMillis() - 1000);
        transactions.add(transaction2);

        when(transactionRepository.findByAccountFromOrAccountToAndType(accountNumber, accountNumber, TransactionType.MARGIN_TRANSACTION))
                .thenReturn(Optional.of(transactions));

        MarginTransactionDto dto1 = new MarginTransactionDto(transaction1.getAccountFrom(), transaction1.getAccountTo(), transaction1.getAmount().doubleValue(), transaction1.getType().name(), transaction1.getDate());
        MarginTransactionDto dto2 = new MarginTransactionDto(transaction2.getAccountFrom(), transaction2.getAccountTo(), transaction2.getAmount().doubleValue(), transaction2.getType().name(), transaction2.getDate());

        when(transactionMapper.transactionToMarginTransactionDto(transaction1)).thenReturn(dto1);
        when(transactionMapper.transactionToMarginTransactionDto(transaction2)).thenReturn(dto2);

        List<MarginTransactionDto> result = paymentTransactionService.getAllMarginTransactions(accountNumber);

        assertEquals(2, result.size());
        assertSame(dto1, result.get(0));
        assertSame(dto2, result.get(1));
    }
}