package rs.edu.raf.userservice.domains.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import rs.edu.raf.userservice.domains.dto.foreignaccount.ForeignAccountDto;
import rs.edu.raf.userservice.domains.model.ForeignAccount;

@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface ForeignAccountMapper {
    ForeignAccountMapper INSTANCE = Mappers.getMapper(ForeignAccountMapper.class);

    ForeignAccountDto foreignAccountToForeignAccountDto(rs.edu.raf.userservice.domains.model.ForeignAccount foreignAccount);

    @Mapping(target = "currency", ignore = true)
    @Mapping(target = "accountType", ignore = true)
    ForeignAccount foreignAccountCreateDtoToForeignAccount(rs.edu.raf.userservice.domains.dto.foreignaccount.ForeignAccountCreateDto foreignAccountCreateDto);
}
