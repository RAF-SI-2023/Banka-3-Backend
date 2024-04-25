package com.example.bankservice;

import com.example.bankservice.client.UserServiceClient;
import com.example.bankservice.domain.dto.currencyExchange.CurrencyExchangeDto;
import com.example.bankservice.domain.model.Currency;
import com.example.bankservice.domain.model.CurrencyExchange;
import com.example.bankservice.domain.model.accounts.Account;
import com.example.bankservice.domain.model.enums.CurrencyName;
import com.example.bankservice.repository.AccountRepository;
import com.example.bankservice.repository.CurrencyExchangeRepository;
import com.example.bankservice.service.AccountService;
import com.example.bankservice.service.CurrencyExchangeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;

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
        assertEquals(new BigDecimal("388.81550"), accountTo.getAvailableBalance());
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
}