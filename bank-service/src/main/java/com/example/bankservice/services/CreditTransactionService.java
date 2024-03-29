package com.example.bankservice.services;

import com.example.bankservice.domains.dto.CreditTransactionDto;
import com.example.bankservice.domains.mappers.CreditTransactionMapper;
import com.example.bankservice.domains.model.CreditTransaction;
import com.example.bankservice.repositories.CreditTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreditTransactionService {

    private CreditTransactionRepository creditTransactionRepository;

    private CreditTransactionMapper creditTransactionMapper;

    public CreditTransactionService(CreditTransactionRepository creditTransactionRepository) {
        this.creditTransactionRepository = creditTransactionRepository;
    }

    public void createCreditTransactions(List<CreditTransactionDto> transactionCreditDtos) {
        List<CreditTransaction> transactions =
                transactionCreditDtos.stream().map(CreditTransactionMapper.INSTANCE::creditTransactionDtoToCreditTransaction)
                .collect(Collectors.toList());
        creditTransactionRepository.saveAll(transactions);
    }
}
