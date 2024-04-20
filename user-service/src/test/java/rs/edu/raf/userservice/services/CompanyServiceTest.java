package rs.edu.raf.userservice.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.userservice.domain.dto.company.CompanyCreateDto;
import rs.edu.raf.userservice.domain.dto.company.CompanyDto;
import rs.edu.raf.userservice.domain.mappers.CompanyMapper;
import rs.edu.raf.userservice.domain.model.Company;
import rs.edu.raf.userservice.repositories.CompanyRepository;

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
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyService companyService;

    @Test
    public void testCreate() {
        CompanyCreateDto companyCreateDto = new CompanyCreateDto();
        Company company = new Company();
        CompanyDto expectedDto = new CompanyDto();

        // Podešavanje ponašanja mock CompanyRepository-ja
        when(companyRepository.save(any(Company.class))).thenReturn(company);

        // Izvršavanje metode koju testiramo
        CompanyDto result = companyService.create(companyCreateDto);

        // Provera rezultata
        assertEquals(expectedDto, result);
        // Provera da li je save metoda pozvana tačno jednom
        verify(companyRepository, times(1)).save(any(Company.class));
    }
    @Test
    public void testFindAll() {
        List<Company> companies = Arrays.asList(
                new Company(),
                new Company()
        );
        List<CompanyDto> expectedDtos = companies.stream()
                .map(CompanyMapper.INSTANCE::companyToCompanyDto)
                .collect(Collectors.toList());

        // Podešavanje ponašanja mock CompanyRepository-ja
        when(companyRepository.findAll()).thenReturn(companies);

        // Izvršavanje metode koju testiramo
        List<CompanyDto> result = companyService.findAll();

        // Provera rezultata
        assertEquals(expectedDtos.size(), result.size());
        for (int i = 0; i < expectedDtos.size(); i++) {
            assertEquals(expectedDtos.get(i), result.get(i));
        }
        // Provera da li je findAll metoda pozvana tačno jednom
        verify(companyRepository, times(1)).findAll();
    }

    @Test
    public void testFindById_CompanyFound() {
        // Priprema podataka za test
        Company company = new Company();
        company.setCompanyId(1l);
        CompanyDto expectedDto = CompanyMapper.INSTANCE.companyToCompanyDto(company);

        // Podešavanje ponašanja mock CompanyRepository-ja
        when(companyRepository.findById(company.getCompanyId())).thenReturn(Optional.of(company));

        // Izvršavanje metode koju testiramo
        CompanyDto result = companyService.findById(company.getCompanyId());

        // Provera rezultata
        assertEquals(expectedDto, result);
        // Provera da li je findById metoda pozvana tačno jednom sa zadatim companyId
        verify(companyRepository, times(1)).findById(company.getCompanyId());
    }
    @Test
    public void testFindById_CompanyNotFound() {
        // Priprema podataka za test
        Long companyId = 1L;

        // Podešavanje ponašanja mock CompanyRepository-ja
        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        // Izvršavanje metode koju testiramo
        CompanyDto result = companyService.findById(companyId);

        // Provera rezultata
        assertEquals(null, result);
        // Provera da li je findById metoda pozvana tačno jednom sa zadatim companyId
        verify(companyRepository, times(1)).findById(companyId);
    }

}