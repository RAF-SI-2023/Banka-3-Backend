package com.example.bankservice.service;

import com.example.bankservice.domain.dto.account.AccountCreateDto;
import com.example.bankservice.domain.dto.account.AccountDto;
import com.example.bankservice.domain.mapper.AccountMapper;
import com.example.bankservice.domain.model.Account;
import com.example.bankservice.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public List<AccountDto> findAll() {
        return accountRepository.findAll().stream().filter(Account::isActive).map(accountMapper::accountToAccountDto)
                .collect(Collectors.toList());
    }

    public List<AccountDto> findByUser(Long userId) {
        return accountRepository.findByUserId(userId).orElseThrow().stream()
                .map(accountMapper::accountToAccountDto).collect(Collectors.toList());
    }

    public AccountDto createAccount(AccountCreateDto accountCreateDto) {

        Account account = accountMapper.accountCreateDtoToAccount(accountCreateDto);
        account.setAccountNumber(String.valueOf(new BigInteger(53, new Random())));
        account.setCreationDate(System.currentTimeMillis());
        account.setExpireDate(System.currentTimeMillis() + 31556952000L);
        account.setReservedAmount(new BigDecimal(0));
        account.setActive(true);
        return accountMapper.accountToAccountDto(accountRepository.save(account));
    }

    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        account.setActive(false);
    }
}
