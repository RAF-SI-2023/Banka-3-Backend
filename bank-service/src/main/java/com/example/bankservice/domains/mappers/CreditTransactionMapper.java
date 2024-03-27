package com.example.bankservice.domains.mappers;

import com.example.bankservice.domains.dto.CreditTransactionDto;
import com.example.bankservice.domains.model.CreditTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface CreditTransactionMapper {

    CreditTransactionMapper INSTANCE = Mappers.getMapper(CreditTransactionMapper.class);

    CreditTransaction creditTransactionDtoToCreditTransaction(CreditTransactionDto creditTransactionDto);
}
