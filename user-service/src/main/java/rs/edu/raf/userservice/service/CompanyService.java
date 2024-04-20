package rs.edu.raf.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.userservice.domain.dto.company.CompanyDto;
import rs.edu.raf.userservice.domain.dto.company.CompanyCreateDto;
import rs.edu.raf.userservice.domain.mapper.CompanyMapper;
import rs.edu.raf.userservice.domain.model.Company;
import rs.edu.raf.userservice.repository.CompanyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

    public List<CompanyDto> findAll() {
        return companyRepository.findAll().stream().map(CompanyMapper.INSTANCE::companyToCompanyDto)
                .collect(Collectors.toList());
    }

    public CompanyDto findById(Long id){
        Company company = companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        return CompanyMapper.INSTANCE.companyToCompanyDto(company);
    }

    public CompanyDto create(CompanyCreateDto companyCreateDto) {
        Company company = CompanyMapper.INSTANCE.createCompanyDtoToCompany(companyCreateDto);
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
