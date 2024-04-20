package com.example.bankservice.service;

import com.example.bankservice.domain.dto.account.AccountCreateDto;
import com.example.bankservice.domain.dto.account.AccountDto;
import com.example.bankservice.domain.dto.companyaccount.CompanyAccountCreateDto;
import com.example.bankservice.domain.dto.companyaccount.CompanyAccountDto;
import com.example.bankservice.domain.mapper.AccountMapper;
import com.example.bankservice.domain.mapper.CompanyAccountMapper;
import com.example.bankservice.domain.model.Account;
import com.example.bankservice.domain.model.CompanyAccount;
import com.example.bankservice.repository.CompanyAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyAccountService {

    private final CompanyAccountMapper companyAccountMapper;
    private final CompanyAccountRepository companyAccountRepository;


    public List<CompanyAccountDto> findAll() {
        return companyAccountRepository.findAll().stream().filter(CompanyAccount::isActive)
                .map(companyAccountMapper::companyaccountToCompanyAccountDto)
                .collect(Collectors.toList());
    }

    public List<CompanyAccountDto> findByCompanyId(Long companyId) {
        return companyAccountRepository.findByCompanyId(companyId).orElseThrow().stream()
                .map(companyAccountMapper::companyaccountToCompanyAccountDto)
                .collect(Collectors.toList());
    }

    public CompanyAccountDto createCompanyAccount(CompanyAccountCreateDto companyAccountCreateDto) {

        CompanyAccount account = companyAccountMapper.companyAccountCreateDtoToCompanyAccount(companyAccountCreateDto);
        account.setAccountNumber(String.valueOf(new BigInteger(53, new Random())));
        account.setCreationDate(System.currentTimeMillis());
        account.setExpireDate(System.currentTimeMillis() + 31556952000L);
        account.setReservedAmount(new BigDecimal(0));
        account.setActive(true);
        return companyAccountMapper.companyaccountToCompanyAccountDto(companyAccountRepository.save(account));
    }

    public void deleteCompanyAccount(Long id) {
        CompanyAccount account = companyAccountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        account.setActive(false);
    }
}
