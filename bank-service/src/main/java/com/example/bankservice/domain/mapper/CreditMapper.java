package com.example.bankservice.domain.mapper;

import com.example.bankservice.domain.dto.credit.CreditDto;
import com.example.bankservice.domain.model.Credit;
import org.mapstruct.Mapper;

@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE,
        componentModel = "spring")
public abstract class CreditMapper {
    public abstract CreditDto creditToCreditDto(Credit credit);
//    public abstract Credit creditCreateDtoToCredit(CreditCreateDto creditCreateDto);
}
