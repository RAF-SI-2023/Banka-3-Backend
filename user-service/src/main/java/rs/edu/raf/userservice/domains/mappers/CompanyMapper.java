package rs.edu.raf.userservice.domains.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import rs.edu.raf.userservice.domains.dto.company.CompanyCreateDto;
import rs.edu.raf.userservice.domains.dto.company.CompanyDto;
import rs.edu.raf.userservice.domains.model.Company;

@Mapper
public interface CompanyMapper {

    CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

    CompanyDto companyToCompanyDto(Company company);

//    Company companyCreateDtoToCompany(CompanyCreateDto companyCreateDto);

    //void updateCompanyFromCompanyUpdateDto(@MappingTarget Company company, CompanyUpdateDto companyUpdateDto);
}
