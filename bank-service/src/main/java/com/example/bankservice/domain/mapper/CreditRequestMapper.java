package com.example.bankservice.domain.mapper;

import com.example.bankservice.domain.dto.creditRequest.CreditRequestCreateDto;
import com.example.bankservice.domain.dto.creditRequest.CreditRequestDto;
import com.example.bankservice.domain.model.CreditRequest;
import org.mapstruct.Mapper;

@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE,
        componentModel = "spring")
public abstract class CreditRequestMapper {
    public abstract CreditRequestDto creditRequestToCreditRequestDto(CreditRequest creditRequest);
    public abstract CreditRequest creditRequestCreateDtoToCreditRequest(CreditRequestCreateDto creditRequestCreateDto);
}
