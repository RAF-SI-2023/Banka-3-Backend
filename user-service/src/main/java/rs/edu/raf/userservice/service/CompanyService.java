package rs.edu.raf.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.userservice.domain.dto.company.CompanyDto;
import rs.edu.raf.userservice.domain.dto.company.CreateCompanyDto;
import rs.edu.raf.userservice.domain.mapper.CompanyMapper;
import rs.edu.raf.userservice.domain.model.Company;
import rs.edu.raf.userservice.repository.CompanyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyDto findById(Long id){
        Company company = companyRepository.findById(id).orElse(null);
        return CompanyMapper.INSTANCE.companyToCompanyDto(company);
    }

    public List<CompanyDto> findAll() {
        return companyRepository.findAll().stream().map(CompanyMapper.INSTANCE::companyToCompanyDto)
                .collect(Collectors.toList());
    }

    public CompanyDto create(CreateCompanyDto createCompanyDto) {
        Company company = CompanyMapper.INSTANCE.createCompanyDtoToCompany(createCompanyDto);
        companyRepository.save(company);
        return CompanyMapper.INSTANCE.companyToCompanyDto(company);
    }

    public boolean changeCompanyActive(Long id, boolean active){
        if (!companyRepository.findById(id).isPresent()){
            return false;
        }
        Company company = companyRepository.findById(id).get();
        if (active){
            company.setActive(true);
            companyRepository.save(company);
        }else {
            company.setActive(false);
            companyRepository.save(company);
        }

        return true;
    }
}
