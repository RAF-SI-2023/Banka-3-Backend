package com.example.bankservice.service;

import com.example.bankservice.domain.dto.transaction.PaymentTransactionDto;
import com.example.bankservice.repository.AccountRepository;
import com.example.bankservice.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;
    private TransactionRepository transactionRepository;
    private AccountService accountService;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void startPaymentTransaction(PaymentTransactionDto paymentTransactionDto) {

    }
}
