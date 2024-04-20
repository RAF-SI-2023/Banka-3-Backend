package com.example.bankservice.service;

import com.example.bankservice.domain.dto.account.AccountDto;
import com.example.bankservice.domain.mapper.AccountMapper;
import com.example.bankservice.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public List<AccountDto> findAll() {
        return accountRepository.findAll().stream().map(AccountMapper.INSTANCE::accountToAccountDto)
                .collect(Collectors.toList());
    }
}
