package com.example.bankservice.service;

import com.example.bankservice.client.EmailServiceClient;
import com.example.bankservice.client.UserServiceClient;
import com.example.bankservice.domain.dto.transaction.PaymentTransactionActivationDto;
import com.example.bankservice.domain.dto.transaction.PaymentTransactionDto;
import com.example.bankservice.domain.mapper.TransactionMapper;
import com.example.bankservice.domain.model.Transaction;
import com.example.bankservice.domain.model.accounts.Account;
import com.example.bankservice.domain.model.accounts.CompanyAccount;
import com.example.bankservice.domain.model.accounts.UserAccount;
import com.example.bankservice.domain.model.enums.TransactionStatus;
import com.example.bankservice.repository.AccountRepository;
import com.example.bankservice.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TransactionService {

    private final UserServiceClient userServiceClient;
    private final EmailServiceClient emailServiceClient;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;
    private TransactionRepository transactionRepository;
    private AccountService accountService;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void startPaymentTransaction(PaymentTransactionDto paymentTransactionDto) {
        //TODO: implementirati menjacnicu ako su razlicite valute
        Account accountFrom = accountRepository.findByAccountNumber(paymentTransactionDto.getAccountFrom())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        Account accountTo = accountRepository.findByAccountNumber(paymentTransactionDto.getAccountTo())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!accountService.checkBalance(paymentTransactionDto.getAccountFrom(), paymentTransactionDto.getAmount())) {
            throw new RuntimeException("Insufficient funds");
        }

        if (accountFrom.getCurrency().getMark().equals(accountTo.getCurrency().getMark())) {
            startSameCurrencyTransaction(paymentTransactionDto, accountFrom, accountTo);
        } else {
            startDifferentCurrencyTransaction(paymentTransactionDto);
        }
    }

    private void startSameCurrencyTransaction(PaymentTransactionDto paymentTransactionDto,
                                              Account accountFrom,
                                              Account accountTo) {
        Transaction transaction = transactionMapper.paymentTransactionDtoToTransaction(paymentTransactionDto);
        transaction.setDate(System.currentTimeMillis());
        transaction.setStatus(TransactionStatus.PENDING);
        transaction = transactionRepository.save(transaction);

        String email = (accountFrom instanceof UserAccount) ?
                userServiceClient.getEmailByUserId(String.valueOf(((UserAccount) accountFrom).getUserId())).getEmail() :
                userServiceClient.getEmailByCompanyId(String.valueOf(((CompanyAccount) accountFrom).getCompanyId()));

        emailServiceClient.sendTransactionActivationEmailToEmailService(new PaymentTransactionActivationDto(email,
                transaction.getTransactionId()));
    }

    private void startDifferentCurrencyTransaction(PaymentTransactionDto paymentTransactionDto) {
        //
    }
}
