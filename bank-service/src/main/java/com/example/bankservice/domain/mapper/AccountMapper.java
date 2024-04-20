package com.example.bankservice.domain.mapper;

import com.example.bankservice.domain.dto.account.AccountCreateDto;
import com.example.bankservice.domain.dto.account.AccountDto;
import com.example.bankservice.domain.model.Account;
import com.example.bankservice.domain.model.Currency;
import com.example.bankservice.repository.CurrencyRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE,
        componentModel = "spring")
public abstract class AccountMapper {

    @Autowired
    private CurrencyRepository currencyRepository;

    public abstract AccountDto accountToAccountDto(Account account);

    @Mapping(target = "currency", source = "currencyMark", qualifiedByName = "stringToCurrency")
    public abstract Account accountCreateDtoToAccount(AccountCreateDto accountCreateDto);

    @Named("stringToCurrency")
    protected Currency stringToCurrency(String currencyMark) {
        return currencyRepository.findByMark(currencyMark)
                .orElseThrow(() -> new IllegalArgumentException("Invalid currency mark: " + currencyMark));
    }

}
