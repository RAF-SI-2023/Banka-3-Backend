package rs.edu.raf.userservice.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import rs.edu.raf.userservice.domain.dto.company.CompanyCreateDto;
import rs.edu.raf.userservice.domain.dto.company.CompanyDto;
import rs.edu.raf.userservice.domain.dto.user.IsUserActiveDto;
import rs.edu.raf.userservice.domain.dto.user.UserDto;
import rs.edu.raf.userservice.domain.dto.user.UserSetPasswordDto;
import rs.edu.raf.userservice.domain.model.Company;
import rs.edu.raf.userservice.domain.model.User;
import rs.edu.raf.userservice.repository.CompanyRepository;
import rs.edu.raf.userservice.service.CompanyService;
import rs.edu.raf.userservice.util.client.EmailServiceClient;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private EmailServiceClient emailServiceClient;

    @InjectMocks
    private CompanyService companyService;

    @Test
    public void loadUserByUsername() {
        Company company = new Company();
        company.setEmail("company@gmail.com");
        company.setPassword("password");
        company.setActive(true);

        when(companyRepository.findByEmail("company@gmail.com")).thenReturn(Optional.of(company));

        UserDetails userDetails = companyService.loadUserByUsername("company@gmail.com");

        assertEquals(userDetails.getUsername(), company.getEmail());
        assertEquals(userDetails.getPassword(), company.getPassword());
    }

    @Test
    public void loadUserByUsernameNotFound() {
        when(companyRepository.findByEmail("company@gmail.com")).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> companyService.loadUserByUsername("company@gmail.com"));
    }

    @Test
    public void loadUserByUsernameNotActive() {
        Company company = new Company();
        company.setEmail("company@gmail.com");
        company.setPassword("password");
        company.setActive(false);

        when(companyRepository.findByEmail("company@gmail.com")).thenReturn(Optional.of(company));

        assertThrows(Exception.class, () -> companyService.loadUserByUsername("company@gmail.com"));
    }

    @Test
    public void getCompanyByEmail() {
        Company company = new Company();
        company.setEmail("company@gmail.com");

        when(companyRepository.findByEmail("company@gmail.com")).thenReturn(Optional.of(company));

        CompanyDto companyDto = companyService.getCompanyByEmail("company@gmail.com");

        assertEquals(company.getEmail(), companyDto.getEmail());
    }

    @Test
    public void findAll() {
        Company company1 = new Company();
        company1.setEmail("company@gmail.com");

        Company company2 = new Company();
        company2.setEmail("company2@gmail.com");

        List<Company> companies = List.of(company1, company2);

        when(companyRepository.findAll()).thenReturn(companies);

        List<CompanyDto> companyDtos = companyService.findAll();

        for (CompanyDto cdto : companyDtos) {
            boolean found = false;
            for (Company c : companies) {
                if (cdto.getEmail().equals(c.getEmail())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                fail("User not found");
            }
        }
    }

    @Test
    public void findById() {
        Company company1 = new Company();
        company1.setEmail("company@gmail.com");
        company1.setCompanyId(1L);

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company1));

        CompanyDto companyDto = companyService.findById(1L);

        assertEquals(company1.getEmail(), companyDto.getEmail());
    }

    @Test
    public void create() {
        CompanyCreateDto companyCreateDto = new CompanyCreateDto();
        companyCreateDto.setEmail("company@gmail.com");
        companyCreateDto.setPib(123456);
        companyCreateDto.setMaticniBroj(123456);
        companyCreateDto.setSifraDelatnosti(12);
        companyCreateDto.setTitle("Company");

        Company company = new Company();
        company.setEmail("company@gmail.com");
        company.setPIB(123456);
        company.setMaticniBroj(123456);
        company.setSifraDelatnosti(12);
        company.setTitle("Company");

        when(companyRepository.save(any(Company.class))).thenReturn(company);

        CompanyDto companyDto = companyService.create(companyCreateDto);

        assertEquals(company.getEmail(), companyDto.getEmail());
        assertEquals(company.getMaticniBroj(), companyDto.getMaticniBroj());
    }

    @Test
    public void createInvalidEmail() {
        CompanyCreateDto companyCreateDto = new CompanyCreateDto();
        companyCreateDto.setEmail("company");

        assertThrows(Exception.class, () -> companyService.create(companyCreateDto));
    }

    @Test
    public void changeCompanyActive() {
        Company company = new Company();
        company.setActive(false);

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

        assertTrue(companyService.changeCompanyActive(1L, true));
        assertTrue(company.isActive());
    }

    @Test
    public void changeCompanyActiveFalse() {
        Company company = new Company();
        company.setActive(true);

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

        assertTrue(companyService.changeCompanyActive(1L, false));
    }

    @Test
    public void changeCompanyActiveNotFound() {
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        assertFalse(companyService.changeCompanyActive(1L, true));
    }

    @Test
    public void isCompanyActive() {
        Company company = new Company();
        company.setEmail("company@gmail.com");
        company.setCodeActive(true);

        when(companyRepository.findByEmail("company@gmail.com")).thenReturn(Optional.of(company));

        IsUserActiveDto isUserActiveDto = companyService.isCompanyActive("company@gmail.com");

        assertEquals(company.getEmail(), isUserActiveDto.getEmail());
        assertTrue(company.isCodeActive());
    }

    @Test
    public void isCompanyActiveFalse() {
        Company company = new Company();
        company.setEmail("company@gmail.com");
        company.setCodeActive(false);

        when(companyRepository.findByEmail("company@gmail.com")).thenReturn(Optional.of(company));
        when(emailServiceClient.sendCompanyActivationEmailToEmailService("company@gmail.com")).thenReturn(null);

        IsUserActiveDto isUserActiveDto = companyService.isCompanyActive("company@gmail.com");

        assertEquals(company.getEmail(), isUserActiveDto.getEmail());
        assertFalse(company.isCodeActive());
    }

    @Test
    public void setPassword(){
        UserSetPasswordDto userSetPasswordDto = new UserSetPasswordDto();
        userSetPasswordDto.setEmail("company@gmail.com");
        userSetPasswordDto.setPassword("password");

        Company company = new Company();
        company.setEmail("company@gmail.com");
        company.setPassword("password");

        when(companyRepository.findByEmail("company@gmail.com")).thenReturn(Optional.of(company));
        when(passwordEncoder.encode("password")).thenReturn("password");
        when(companyRepository.save(any(Company.class))).thenReturn(company);

        companyService.setPassword(userSetPasswordDto);

        assertEquals(company.getPassword(), "password");
        assertTrue(company.isActive());
        assertTrue(company.isCodeActive());

    }

}