package com.example.bankservice;

import com.example.bankservice.domain.dto.currencyExchange.CommissionByMarkDto;
import com.example.bankservice.domain.dto.currencyExchange.CommissionDto;
import com.example.bankservice.domain.dto.currencyExchange.CurrencyExchangeDto;
import com.example.bankservice.domain.model.CommissionFromCurrencyExchange;
import com.example.bankservice.domain.model.Currency;
import com.example.bankservice.domain.model.CurrencyExchange;
import com.example.bankservice.domain.model.accounts.Account;
import com.example.bankservice.domain.model.enums.CurrencyName;
import com.example.bankservice.repository.AccountRepository;
import com.example.bankservice.repository.CommissionFromCurrencyExhangeRepository;
import com.example.bankservice.repository.CurrencyExchangeRepository;
import com.example.bankservice.service.AccountService;
import com.example.bankservice.service.CurrencyExchangeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyExchangeServiceTest {
    @Mock
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CurrencyExchangeRepository currencyExchangeRepository;
    @Mock
    private CommissionFromCurrencyExhangeRepository commissionFromCurrencyExhangeRepository;
    @InjectMocks
    private CurrencyExchangeService currencyExchangeService;

    @Test
    void testStartCurrencyExchangeTransaction() {
        CurrencyExchangeDto currencyExchangeDto = new CurrencyExchangeDto();
        currencyExchangeDto.setAccountFrom("5555555555555555");
        currencyExchangeDto.setAccountTo("4444444444444444");
        currencyExchangeDto.setAmount(100.0);

        Account accountFrom = new Account();
        accountFrom.setAccountNumber("5555555555555555");
        accountFrom.setAvailableBalance(new BigDecimal(200));
        accountFrom.setCurrency(new Currency(1l, CurrencyName.DOLLAR,"USD"));

        Account accountTo = new Account();
        accountTo.setAccountNumber("4444444444444444");
        accountTo.setAvailableBalance(new BigDecimal(300));
        accountTo.setCurrency(new Currency(2l, CurrencyName.EURO, "EUR"));

        Account bankAccountUSD = new Account();
        bankAccountUSD.setAccountNumber("5555555555555555"); // Postavi odgovarajući broj računa za USD
        bankAccountUSD.setCurrency(new Currency(1L, CurrencyName.DOLLAR, "USD"));
        bankAccountUSD.setAvailableBalance(new BigDecimal(1000)); // Postavi odgovarajući početni saldo

        Account bankAccountEUR = new Account();
        bankAccountEUR.setAccountNumber("4444444444444444"); // Postavi odgovarajući broj računa za EUR
        bankAccountEUR.setCurrency(new Currency(2L, CurrencyName.EURO, "EUR"));
        bankAccountEUR.setAvailableBalance(new BigDecimal(2000)); // Postavi odgovarajući početni saldo

        when(accountService.extractAccountForAccountNumber("5555555555555555")).thenReturn(accountFrom);
        when(accountService.extractAccountForAccountNumber("4444444444444444")).thenReturn(accountTo);
        when(accountService.checkBalance("5555555555555555", 100.0)).thenReturn(true);
        when(accountService.findBankAccountForGivenCurrency("USD")).thenReturn(bankAccountUSD); // Vraća bankovni račun za USD
        when(accountService.findBankAccountForGivenCurrency("EUR")).thenReturn(bankAccountEUR); // Vraća bankovni račun za EUR

        currencyExchangeService.startCurrencyExchangeTransaction(currencyExchangeDto);


        verify(accountService, times(2)).extractAccountForAccountNumber(anyString());
        verify(accountService).checkBalance("5555555555555555", 100.0);
        verify(accountRepository, times(1)).saveAll(any(List.class));
        verify(currencyExchangeRepository).save(any(CurrencyExchange.class));

        assertEquals(new BigDecimal("100.0"), accountFrom.getAvailableBalance());
//        assertEquals(new BigDecimal("388.81550"), accountTo.getAvailableBalance());
    }

    @Test
    void testStartCurrencyExchangeTransaction_InsufficientFunds() {
        CurrencyExchangeDto currencyExchangeDto = new CurrencyExchangeDto();
        currencyExchangeDto.setAccountFrom("fromAccountNumber");
        currencyExchangeDto.setAccountTo("toAccountNumber");
        currencyExchangeDto.setAmount(100.0);

        when(accountService.extractAccountForAccountNumber(anyString())).thenReturn(new Account());

        when(accountService.checkBalance("fromAccountNumber", 100.0)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> currencyExchangeService.startCurrencyExchangeTransaction(currencyExchangeDto));

        verifyNoInteractions(accountRepository);
        verifyNoInteractions(currencyExchangeRepository);
    }
    @Test
    void testGetMoneyMadeOnExchangeTransactions_NoData() {
        // Arrange
        when(commissionFromCurrencyExhangeRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        BigDecimal result = currencyExchangeService.getMoneyMadeOnExchangeTransactions();

        // Assert
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void testGetMoneyMadeOnExchangeTransactions_WithData() {
        // Arrange
        BigDecimal expectedAmount = new BigDecimal("100.00");
        List<CommissionFromCurrencyExchange> commissions = new ArrayList<>();
        commissions.add(new CommissionFromCurrencyExchange(1l,expectedAmount,5));
        when(commissionFromCurrencyExhangeRepository.findAll()).thenReturn(commissions);

        // Act
        BigDecimal result = currencyExchangeService.getMoneyMadeOnExchangeTransactions();

        // Assert
        assertEquals(expectedAmount, result);
    }
    @Test
    public void testGetCommissions() {
        // Given
        CurrencyExchange exchange1 = new CurrencyExchange(1L, "Account1", "Account2", new BigDecimal("100.00"), new BigDecimal("10.00"), "USD", 20230601L);
        CurrencyExchange exchange2 = new CurrencyExchange(2L, "Account2", "Account3", new BigDecimal("200.00"), new BigDecimal("20.00"), "EUR", 20230602L);
        CurrencyExchange exchange3 = new CurrencyExchange(3L, "Account3", "Account4", new BigDecimal("300.00"), new BigDecimal("30.00"), "GBP", 20230603L);

        List<CurrencyExchange> currencyExchanges = Arrays.asList(exchange1, exchange2, exchange3);
        when(currencyExchangeRepository.findAll()).thenReturn(currencyExchanges);

        // When
        List<CommissionDto> result = currencyExchangeService.getCommissons();

        // Then
        assertEquals(3, result.size());

        assertEquals("Account3", result.get(0).getAccountFrom());
        assertEquals(new BigDecimal("30.00"), result.get(0).getCommission());
        assertEquals("GBP", result.get(0).getCurrencyMark());
        assertEquals(20230603L, result.get(0).getDate());

        assertEquals("Account2", result.get(1).getAccountFrom());
        assertEquals(new BigDecimal("20.00"), result.get(1).getCommission());
        assertEquals("EUR", result.get(1).getCurrencyMark());
        assertEquals(20230602L, result.get(1).getDate());

        assertEquals("Account1", result.get(2).getAccountFrom());
        assertEquals(new BigDecimal("10.00"), result.get(2).getCommission());
        assertEquals("USD", result.get(2).getCurrencyMark());
        assertEquals(20230601L, result.get(2).getDate());

    }
    @Test
    public void testGetCommissionByMark() {
        // Given
        CurrencyExchange exchange1 = new CurrencyExchange(1L, "Account1", "Account2", new BigDecimal("100.00"), new BigDecimal("10.00"), "EUR", 20230601L);
        CurrencyExchange exchange2 = new CurrencyExchange(2L, "Account2", "Account3", new BigDecimal("200.00"), new BigDecimal("20.00"), "USD", 20230602L);
        CurrencyExchange exchange3 = new CurrencyExchange(3L, "Account3", "Account4", new BigDecimal("300.00"), new BigDecimal("30.00"), "RSD", 20230603L);
        CurrencyExchange exchange4 = new CurrencyExchange(4L, "Account4", "Account5", new BigDecimal("400.00"), new BigDecimal("40.00"), "EUR", 20230604L);

        List<CurrencyExchange> currencyExchanges = Arrays.asList(exchange1, exchange2, exchange3, exchange4);
        when(currencyExchangeRepository.findAll()).thenReturn(currencyExchanges);

        // When
        List<CommissionByMarkDto> result = currencyExchangeService.getCommissionByMark();

        // Then
        assertEquals(3, result.size());

        CommissionByMarkDto eurCommission = result.stream()
                .filter(dto -> dto.getCurrencyMark().equals("EUR"))
                .findFirst()
                .orElse(null);
        assertEquals(new BigDecimal("50.00"), eurCommission.getCommission());

        CommissionByMarkDto usdCommission = result.stream()
                .filter(dto -> dto.getCurrencyMark().equals("USD"))
                .findFirst()
                .orElse(null);
        assertEquals(new BigDecimal("20.00"), usdCommission.getCommission());

        CommissionByMarkDto rsdCommission = result.stream()
                .filter(dto -> dto.getCurrencyMark().equals("RSD"))
                .findFirst()
                .orElse(null);
        assertEquals(new BigDecimal("30.00"), rsdCommission.getCommission());
    }
}