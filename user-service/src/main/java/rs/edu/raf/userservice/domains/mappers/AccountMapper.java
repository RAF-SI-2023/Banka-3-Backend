package rs.edu.raf.userservice.domains.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import rs.edu.raf.userservice.domains.dto.account.AccountDto;
import rs.edu.raf.userservice.domains.model.Account;

@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    AccountDto accountToAccountDto(rs.edu.raf.userservice.domains.model.Account account);

    @Mapping(target = "currency", ignore = true)
    @Mapping(target = "accountType", ignore = true)
    Account accountCreateDtoToAccount(rs.edu.raf.userservice.domains.dto.account.AccountCreateDto accountCreateDto);
}
