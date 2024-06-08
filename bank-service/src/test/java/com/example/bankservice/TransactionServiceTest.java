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
import com.example.bankservice.repository.AccountRepository;
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

import java.math.BigDecimal;
import java.util.*;


import static com.example.bankservice.domain.model.enums.CurrencyMark.RSD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private AccountRepository accountRepository;
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

//    @Test
//    void testGetAllPaymentTransactions_NoTransactionsFound() {
//        when(transactionRepository.findByAccountFromOrAccountToAndType(accountNumber, accountNumber, TransactionType.PAYMENT_TRANSACTION))
//                .thenReturn(Optional.empty());
//
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            paymentTransactionService.getAllPaymentTransactions(accountNumber);
//        });
//
//        assertEquals("Transactions not found", exception.getMessage());
//
//        verify(transactionRepository, times(1)).findByAccountFromOrAccountToAndType(accountNumber, accountNumber, TransactionType.PAYMENT_TRANSACTION);
//        verifyNoInteractions(transactionMapper);
//    }

    @Test
    public void testStockSellTransaction_Uspesno() {
        when(accountService.findExchangeAccountForGivenCurrency("USD")).thenReturn(accountFrom);
        when(accountService.findBankAccountForGivenCurrency("USD")).thenReturn(accountTo);

        paymentTransactionService.stockSellTransaction(stockTransactionDto);

        verify(transactionRepository, times(1)).save(any(Transaction.class));
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

    @Test
    public void testStockSellTransaction_UserAccount() {
        stockTransactionDto.setEmployeeId(null);

        when(accountService.findExchangeAccountForGivenCurrency("USD")).thenReturn(accountFrom);
        when(accountService.findAccount(stockTransactionDto)).thenReturn(accountTo);

        paymentTransactionService.stockSellTransaction(stockTransactionDto);

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

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
}