package rs.edu.raf.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.edu.raf.userservice.domain.dto.company.CompanyCreateDto;
import rs.edu.raf.userservice.domain.dto.company.CompanyDto;
import rs.edu.raf.userservice.domain.exception.ForbiddenException;
import rs.edu.raf.userservice.domain.exception.NotFoundException;
import rs.edu.raf.userservice.domain.mapper.CompanyMapper;
import rs.edu.raf.userservice.domain.model.Company;
import rs.edu.raf.userservice.repository.CompanyRepository;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService implements UserDetailsService {
    private final Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    private final PasswordEncoder passwordEncoder;
    private final CompanyRepository companyRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Company company = companyRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("company not found"));

        if (company == null){
            throw new UsernameNotFoundException("Company with the email: " + email + " not found");
        }
        if (!company.isActive()) {
            throw new ForbiddenException("company not active");
        }

        return new User(company.getEmail(), company.getPassword(), new ArrayList<>());
    }

    public CompanyDto getCompanyByEmail(String email) {
        Optional<Company> company = companyRepository.findByEmail(email);
        return company.map(CompanyMapper.INSTANCE::companyToCompanyDto).orElseThrow(() -> new NotFoundException("company with" + email + " not found"));
    }

    public List<CompanyDto> findAll() {
        return companyRepository.findAll().stream().map(CompanyMapper.INSTANCE::companyToCompanyDto)
                .collect(Collectors.toList());
    }

    public CompanyDto findById(Long id){
        Company company = companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        return CompanyMapper.INSTANCE.companyToCompanyDto(company);
    }

    public CompanyDto create(CompanyCreateDto companyCreateDto) {
        if (!emailPattern.matcher(companyCreateDto.getEmail()).matches()) {
            throw new ValidationException("invalid email");
        }
        Company company = CompanyMapper.INSTANCE.createCompanyDtoToCompany(companyCreateDto);
        company.setActive(true);
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
