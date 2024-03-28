package rs.edu.raf.userservice.services;

import org.springframework.stereotype.Service;
import rs.edu.raf.userservice.domains.dto.company.CompanyCreateDto;
import rs.edu.raf.userservice.domains.dto.company.CompanyDto;
import rs.edu.raf.userservice.domains.mappers.CompanyMapper;
import rs.edu.raf.userservice.domains.model.Company;
import rs.edu.raf.userservice.repositories.CompanyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public CompanyDto create(CompanyCreateDto companyCreateDto) {
        Company company = CompanyMapper.INSTANCE.companyCreateDtoToCompany(companyCreateDto);

        companyRepository.save(company);
        return CompanyMapper.INSTANCE.companyToCompanyDto(company);
    }

    public List<CompanyDto> findAll() {
        return companyRepository.findAll().stream().map(CompanyMapper.INSTANCE::companyToCompanyDto)
                .collect(Collectors.toList());
    }

    public CompanyDto findById(Long id) {
        Company company = companyRepository.findById(id).orElse(null);
        return CompanyMapper.INSTANCE.companyToCompanyDto(company);
    }


}
