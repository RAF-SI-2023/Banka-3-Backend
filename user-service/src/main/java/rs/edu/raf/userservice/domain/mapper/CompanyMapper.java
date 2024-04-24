package rs.edu.raf.userservice.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import rs.edu.raf.userservice.domain.dto.user.IsUserActiveDto;
import rs.edu.raf.userservice.domain.model.Company;
import rs.edu.raf.userservice.domain.dto.company.CompanyDto;
import rs.edu.raf.userservice.domain.dto.company.CompanyCreateDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CompanyMapper {

    CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

    CompanyDto companyToCompanyDto(Company company);

    Company createCompanyDtoToCompany(CompanyCreateDto companyCreateDto);

    IsUserActiveDto companyToIsAUserActiveDTO(Company company);
}
