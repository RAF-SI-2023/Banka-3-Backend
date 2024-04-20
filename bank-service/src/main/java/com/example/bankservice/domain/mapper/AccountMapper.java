package com.example.bankservice.domain.mapper;

import com.example.bankservice.domain.dto.account.AccountDto;
import com.example.bankservice.domain.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    AccountDto accountToAccountDto(Account account);

//    @Mapping(target = "currency", ignore = true)
//    @Mapping(target = "accountType", ignore = true)
//    Account accountCreateDtoToAccount(rs.edu.raf.userservice.domains.dto.account.AccountCreateDto accountCreateDto);
}
