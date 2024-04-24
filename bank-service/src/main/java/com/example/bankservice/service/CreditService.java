package com.example.bankservice.service;

import com.example.bankservice.domain.dto.credit.CreditDto;
import com.example.bankservice.domain.mapper.CreditMapper;
import com.example.bankservice.domain.model.Credit;
import com.example.bankservice.domain.model.Transaction;
import com.example.bankservice.domain.model.accounts.Account;
import com.example.bankservice.domain.model.enums.TransactionStatus;
import com.example.bankservice.domain.model.enums.TransactionType;
import com.example.bankservice.repository.CreditRepository;
import com.example.bankservice.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CreditService {

    private final CreditRepository creditRepository;
    private final CreditMapper creditMapper;
    private final AccountService accountService;
    private final TransactionRepository transactionRepository;

    public List<CreditDto> findAllCreditsByUserId(Long userId) {
        List<Credit> credits = creditRepository.findAllByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Credits not found"));

        return credits.stream().map(creditMapper::creditToCreditDto).toList();
    }

    public List<CreditDto> findAll() {
        List<Credit> credits = creditRepository.findAll();
        return credits.stream().map(creditMapper::creditToCreditDto).toList();
    }

    public void creditPayout(Credit credit) {

        Account bankAccount = accountService.findBankAccountForGivenCurrency(credit.getCurrencyMark());

        if (bankAccount.getAvailableBalance().compareTo(credit.getAmount()) <= 0) {
            throw new RuntimeException("Bank insufficient funds");
        }

        Transaction transaction = new Transaction();
        transaction.setAccountFrom(bankAccount.getAccountNumber());
        transaction.setAccountTo(credit.getAccountNumber());
        transaction.setAmount(credit.getAmount());
        transaction.setType(TransactionType.CREDIT_APPROVE_TRANSACTION);
        transaction.setTransactionStatus(TransactionStatus.ACCEPTED);
        transaction.setDate(credit.getStartDate());

        transactionRepository.save(transaction);
    }
}
