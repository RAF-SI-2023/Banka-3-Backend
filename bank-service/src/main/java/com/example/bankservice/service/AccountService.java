package com.example.bankservice.service;

import com.example.bankservice.domain.dto.account.AccountCreateDto;
import com.example.bankservice.domain.dto.account.AccountDto;
import com.example.bankservice.domain.mapper.AccountMapper;
import com.example.bankservice.domain.model.Account;
import com.example.bankservice.domain.model.Card;
import com.example.bankservice.repository.AccountRepository;
import com.example.bankservice.repository.CardRepository;
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
    private final CardRepository cardRepository;

    public List<AccountDto> findAll() {
        return accountRepository.findAll().stream().filter(Account::isActive).map(accountMapper::accountToAccountDto)
                .collect(Collectors.toList());
    }

    public List<AccountDto> findByUser(Long userId) {
        return accountRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Not found")).stream()
                .map(accountMapper::accountToAccountDto).collect(Collectors.toList());
    }

    public AccountDto createAccount(AccountCreateDto accountCreateDto) {

        Account account = accountMapper.accountCreateDtoToAccount(accountCreateDto);
        account.setAccountNumber(String.valueOf(new BigInteger(53, new Random())));
        account.setCreationDate(System.currentTimeMillis());
        account.setExpireDate(System.currentTimeMillis() + 31556952000L);
        account.setReservedAmount(new BigDecimal(0));
        account.setActive(true);

        createCard(account);
        return accountMapper.accountToAccountDto(accountRepository.save(account));
    }

    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        account.setActive(false);
    }

    private void createCard(Account account) {
        Card card = new Card();
        card.setAccountNumber(account.getAccountNumber());
        card.setCardNumber(String.valueOf(new BigInteger(53, new Random())));
        card.setCardName(account.getAccountType());
        card.setCVV(String.valueOf(new Random().nextInt(999)));
        card.setCreationDate(System.currentTimeMillis());
        card.setExpireDate(System.currentTimeMillis() + 31556952000L);
        card.setActive(true);

        cardRepository.save(card);
    }
}
