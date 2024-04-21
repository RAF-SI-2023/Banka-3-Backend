package com.example.bankservice.service;

import com.example.bankservice.domain.dto.card.*;
import com.example.bankservice.domain.mapper.CardMapper;
import com.example.bankservice.domain.model.Card;
import com.example.bankservice.domain.model.accounts.Account;
import com.example.bankservice.domain.model.accounts.CompanyAccount;
import com.example.bankservice.domain.model.accounts.UserAccount;
import com.example.bankservice.repository.AccountRepository;
import com.example.bankservice.repository.CardRepository;
import com.example.bankservice.repository.CompanyAccountRepository;
import com.example.bankservice.repository.UserAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final UserAccountRepository userAccountRepository;
    private final CompanyAccountRepository companyAccountRepository;
    private final AccountRepository accountRepository;

    public List<CardDto> findAll() {
        return cardRepository.findAll().stream().filter(Card::isActive).map(cardMapper::cardToCardDto).toList();
    }

    public List<CardDto> findAllByAccountNumber(String accountNumber) {
        return cardRepository.findAllByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Unable to find cards for account number"))
                .stream()
                .filter(Card::isActive).map(cardMapper::cardToCardDto).toList();
    }

    public List<CardDto> findAllByUserId(Long userId) {

        List<UserAccount> userAccount = userAccountRepository.findAllByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Unable to find user account"));

        List<Card> userCards = new ArrayList<>();

        for (UserAccount account : userAccount) {
            userCards.add(cardRepository.findByAccountNumber(account.getAccountNumber())
                    .orElseThrow(() -> new RuntimeException("Unable to find cards for user")));
        }

        return userCards.stream().filter(Card::isActive).map(cardMapper::cardToCardDto).toList();
    }

    public List<CardDto> findAllByCompanyId(Long companyId) {

        List<CompanyAccount> companyAccounts = companyAccountRepository.findAllByCompanyId(companyId)
                .orElseThrow(() -> new RuntimeException("Unable to find company account"));

        List<Card> companyCards = new ArrayList<>();

        for (CompanyAccount account : companyAccounts) {
            companyCards.add(cardRepository.findByAccountNumber(account.getAccountNumber())
                    .orElseThrow(() -> new RuntimeException("Unable to find cards for company")));
        }

        return companyCards.stream().filter(Card::isActive).map(cardMapper::cardToCardDto).toList();
    }

    public CardResponseDto cardLogin(CardLoginDto cardLoginDto) {
        Card card = cardRepository.findByCardNumber(cardLoginDto.getCardNumber())
                .orElseThrow(() -> new RuntimeException("Unable to find card"));

        if (!card.isActive()) {
            throw new RuntimeException("Card is not active");
        }
        if (!card.getCVV().equals(cardLoginDto.getCVV())) {
            throw new RuntimeException("Invalid cvv");
        }

        return new CardResponseDto(card.getAccountNumber());
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void withdraw(WithdrawFundsDto withdrawFundsDto) {
        Account account = accountRepository.findByAccountNumber(withdrawFundsDto.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Unable to find card"));

        if (!account.isActive()) {
            throw new RuntimeException("Account is not active");
        }

        if (account.getAvailableBalance().compareTo(BigDecimal.valueOf(withdrawFundsDto.getAmount())) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        account.setAvailableBalance(account.getAvailableBalance().subtract(BigDecimal.valueOf(withdrawFundsDto.getAmount())));
        accountRepository.save(account);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deposit(DepositFundsDto depositFundsDto) {
        Account account = accountRepository.findByAccountNumber(depositFundsDto.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Unable to find card"));

        if (!account.isActive()) {
            throw new RuntimeException("Account is not active");
        }

        account.setAvailableBalance(account.getAvailableBalance().add(BigDecimal.valueOf(depositFundsDto.getAmount())));
        accountRepository.save(account);
    }

    public void deactivateCard(Long cardId) {
        cardRepository.findById(cardId).ifPresent(card -> {
            card.setActive(false);
            cardRepository.save(card);
        });
    }

    public void activateCard(Long cardId) {
        cardRepository.findById(cardId).ifPresent(card -> {
            card.setActive(true);
            cardRepository.save(card);
        });
    }
}
