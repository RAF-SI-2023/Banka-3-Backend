package rs.edu.raf.userservice.domains.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import rs.edu.raf.userservice.domains.dto.companyaccount.CompanyAccountDto;
import rs.edu.raf.userservice.domains.model.CompanyAccount;

@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface CompanyAccountMapper {
    CompanyAccountMapper INSTANCE = Mappers.getMapper(CompanyAccountMapper.class);

    CompanyAccountDto companyAccountToCompanyAccountDto(rs.edu.raf.userservice.domains.model.CompanyAccount companyAccount);
    @Mapping(target = "currency", ignore = true)
    CompanyAccount companyAccountCreateDtoToCompanyAccount(rs.edu.raf.userservice.domains.dto.companyaccount.CompanyAccountCreateDto companyAccountCreateDto);
}
