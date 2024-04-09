package com.example.bankservice.domains.mappers;


import com.example.bankservice.domains.dto.TransactionDto;
import com.example.bankservice.domains.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    TransactionDto transactionToTransactionDto(Transaction transaction);

    Transaction transactionDtoToTransaction(TransactionDto transactionDto);
}
