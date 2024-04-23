package com.example.bankservice.domain.mapper;

import com.example.bankservice.domain.dto.transaction.CreditTransactionDto;
import com.example.bankservice.domain.dto.transaction.PaymentTransactionDto;
import com.example.bankservice.domain.model.Transaction;
import org.mapstruct.Mapper;

@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE,
        componentModel = "spring")
public abstract class TransactionMapper {

    public abstract Transaction paymentTransactionDtoToTransaction(PaymentTransactionDto paymentTransactionDto);

    public abstract CreditTransactionDto transactionToCreditTransactionDto(Transaction transaction);
}
