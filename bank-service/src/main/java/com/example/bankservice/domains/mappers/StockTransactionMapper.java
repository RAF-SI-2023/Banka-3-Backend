package com.example.bankservice.domains.mappers;

import com.example.bankservice.domains.dto.StockTransactionDto;
import com.example.bankservice.domains.model.StockTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StockTransactionMapper {
    StockTransactionMapper INSTANCE = Mappers.getMapper(StockTransactionMapper.class);

    StockTransaction stockTransactionDtoToStockTransaction(StockTransactionDto stockTransactionDto);
}
