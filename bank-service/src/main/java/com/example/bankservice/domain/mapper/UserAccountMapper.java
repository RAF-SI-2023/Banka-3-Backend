package com.example.bankservice.domain.mapper;

import com.example.bankservice.domain.dto.account.UserAccountCreateDto;
import com.example.bankservice.domain.dto.account.UserAccountDto;
import com.example.bankservice.domain.model.accounts.UserAccount;
import com.example.bankservice.domain.model.Currency;
import com.example.bankservice.repository.CurrencyRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE,
        componentModel = "spring")
public abstract class UserAccountMapper {

    @Autowired
    private CurrencyRepository currencyRepository;

    public abstract UserAccountDto userAccountToUserAccountDto(UserAccount userAccount);

    @Mapping(target = "currency", source = "currencyMark", qualifiedByName = "stringToCurrency")
    public abstract UserAccount userAccountCreateDtoToUserAccount(UserAccountCreateDto userAccountCreateDto);

    @Named("stringToCurrency")
    protected Currency stringToCurrency(String currencyMark) {
        return currencyRepository.findByMark(currencyMark)
                .orElseThrow(() -> new IllegalArgumentException("Invalid currency mark: " + currencyMark));
    }

}
