package com.example.bankservice;

import com.example.bankservice.domain.dto.credit.CreditDto;
import com.example.bankservice.domain.mapper.CreditMapper;
import com.example.bankservice.domain.model.Credit;
import com.example.bankservice.domain.model.accounts.Account;
import com.example.bankservice.repository.CreditRepository;
import com.example.bankservice.repository.TransactionRepository;
import com.example.bankservice.service.AccountService;
import com.example.bankservice.service.CreditService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditServiceTest {
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AccountService accountService;
    @Mock
    private CreditRepository creditRepository;

    @Mock
    private CreditMapper creditMapper;

    @InjectMocks
    private CreditService creditService;

    @Test
    public void testFindAllCreditsByUserId() {
        Long userId = 123L;
        Credit credit1 = new Credit();
        Credit credit2 = new Credit();

        credit1.setUserId(userId);
        credit1.setId(1L);
        credit1.setAmount(BigDecimal.valueOf(100));
        credit1.setAccountNumber("fromAccount");
        credit1.setEmployeeId(1L);

        credit2.setUserId(userId);
        credit2.setId(1L);
        credit2.setAmount(BigDecimal.valueOf(1000));
        credit2.setAccountNumber("toAccount");
        credit2.setEmployeeId(1L);

        List<Credit> mockCredits = new ArrayList<>();
        mockCredits.add(credit1);
        mockCredits.add(credit2);

        CreditDto creditDto1 = new CreditDto();
        CreditDto creditDto2 = new CreditDto();

        creditDto1.setUserId(userId);
        creditDto1.setAmount(100.0);
        creditDto1.setAccountNumber("fromAccount");
        creditDto1.setEmployeeId(1L);

        creditDto2.setUserId(userId);
        creditDto2.setAmount(100.0);
        creditDto2.setAccountNumber("fromAccount");
        creditDto2.setEmployeeId(1L);

        List<CreditDto> expectedCreditDtos = new ArrayList<>();
        expectedCreditDtos.add(creditDto1);
        expectedCreditDtos.add(creditDto2);

        when(creditRepository.findAllByUserId(userId)).thenReturn(Optional.of(mockCredits));

        when(creditMapper.creditToCreditDto(credit1)).thenReturn(creditDto1);
        when(creditMapper.creditToCreditDto(credit2)).thenReturn(creditDto2);

        List<CreditDto> actualCreditDtos = creditService.findAllCreditsByUserId(userId);

        assertEquals(expectedCreditDtos, actualCreditDtos);
    }
    @Test
    public void testFindAllCreditsByUserId_NoCreditsFound() {
        Long userId = 123L;

        // Postavljamo da ne postoji nijedan kredit za datog korisnika
        when(creditRepository.findAllByUserId(userId)).thenReturn(Optional.empty());

        // Provjeravamo da li se RuntimeException baca kada nema kredita za datog korisnika
        assertThrows(RuntimeException.class, () -> creditService.findAllCreditsByUserId(userId), "Credits not found");
    }
    @Test
    public void testFindAll() {
        Long userId = 123L;

        Credit credit1 = new Credit();
        Credit credit2 = new Credit();

        credit1.setUserId(userId);
        credit1.setId(1L);
        credit1.setAmount(BigDecimal.valueOf(100));
        credit1.setAccountNumber("fromAccount");
        credit1.setEmployeeId(1L);

        credit2.setUserId(userId);
        credit2.setId(1L);
        credit2.setAmount(BigDecimal.valueOf(1000));
        credit2.setAccountNumber("toAccount");
        credit2.setEmployeeId(1L);

        List<Credit> mockCredits = new ArrayList<>();
        mockCredits.add(credit1);
        mockCredits.add(credit2);

        CreditDto creditDto1 = new CreditDto();
        CreditDto creditDto2 = new CreditDto();

        creditDto1.setUserId(userId);
        creditDto1.setAmount(100.0);
        creditDto1.setAccountNumber("fromAccount");
        creditDto1.setEmployeeId(1L);

        creditDto2.setUserId(userId);
        creditDto2.setAmount(100.0);
        creditDto2.setAccountNumber("fromAccount");
        creditDto2.setEmployeeId(1L);

        List<CreditDto> expectedCreditDtos = new ArrayList<>();
        expectedCreditDtos.add(creditDto1);
        expectedCreditDtos.add(creditDto2);

        when(creditRepository.findAll()).thenReturn(mockCredits);

        when(creditMapper.creditToCreditDto(credit1)).thenReturn(creditDto1);
        when(creditMapper.creditToCreditDto(credit2)).thenReturn(creditDto2);

        List<CreditDto> actualCreditDtos = creditService.findAll();

        assertEquals(expectedCreditDtos, actualCreditDtos);
    }
    @Test
    public void testCreditPayout_InsufficientFunds() {
        Credit credit = new Credit();
        credit.setCurrencyMark("USD");
        credit.setAmount(BigDecimal.valueOf(1000));
        Account bankAccount = new Account();
        bankAccount.setAvailableBalance(BigDecimal.valueOf(500));

        when(accountService.findBankAccountForGivenCurrency("USD")).thenReturn(bankAccount);

        assertThrows(RuntimeException.class, () -> creditService.creditPayout(credit), "Bank insufficient funds");
    }
    @Test
    public void testCreditPayout_SufficientFunds() {
        Credit credit = new Credit();
        credit.setCurrencyMark("USD");
        credit.setAmount(BigDecimal.valueOf(500));
        Account bankAccount = new Account();
        bankAccount.setAccountNumber("bankAccountNumber");
        bankAccount.setAvailableBalance(BigDecimal.valueOf(1000));

        when(accountService.findBankAccountForGivenCurrency("USD")).thenReturn(bankAccount);

        creditService.creditPayout(credit);

        verify(transactionRepository, times(1)).save(any());
    }
}