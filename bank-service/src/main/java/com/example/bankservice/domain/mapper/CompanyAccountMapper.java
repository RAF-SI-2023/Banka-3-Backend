package com.example.bankservice.domain.mapper;

import com.example.bankservice.domain.dto.companyaccount.CompanyAccountCreateDto;
import com.example.bankservice.domain.dto.companyaccount.CompanyAccountDto;
import com.example.bankservice.domain.model.accounts.CompanyAccount;
import com.example.bankservice.domain.model.Currency;
import com.example.bankservice.repository.CurrencyRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE,
        componentModel = "spring")
public abstract class CompanyAccountMapper {

    @Autowired
    private CurrencyRepository currencyRepository;

    public abstract CompanyAccountDto companyAccountToCompanyAccountDto(CompanyAccount companyAccount);

    @Mapping(target = "currency", source = "currencyMark", qualifiedByName = "stringToCurrency")
    public abstract CompanyAccount companyAccountCreateDtoToCompanyAccount(CompanyAccountCreateDto companyAccountCreateDto);

    @Named("stringToCurrency")
    protected Currency stringToCurrency(String currencyMark) {
        return currencyRepository.findByMark(currencyMark)
                .orElseThrow(() -> new IllegalArgumentException("Invalid currency mark: " + currencyMark));
    }

}
