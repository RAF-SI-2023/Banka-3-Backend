package rs.edu.raf.userservice.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.userservice.domains.dto.companyaccount.CompanyAccountCreateDto;
import rs.edu.raf.userservice.domains.model.Company;
import rs.edu.raf.userservice.domains.model.CompanyAccount;
import rs.edu.raf.userservice.domains.model.Currency;
import rs.edu.raf.userservice.domains.model.Employee;
import rs.edu.raf.userservice.domains.model.enums.CurrencyName;
import rs.edu.raf.userservice.repositories.CompanyAccountRepository;
import rs.edu.raf.userservice.repositories.CompanyRepository;
import rs.edu.raf.userservice.repositories.CurrencyRepository;
import rs.edu.raf.userservice.repositories.EmployeeRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyAccountServiceTest {

    @Mock
    CompanyAccountRepository companyAccountRepository;

    @Mock
    CurrencyRepository currencyRepository;

    @Mock
    CompanyRepository companyRepository;

    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    CompanyAccountService companyAccountService;


    @Test
    public void createTest() {
        CompanyAccountCreateDto companyAccountCreateDto = new CompanyAccountCreateDto(1L, new BigDecimal(100.0), "DINAR", "ZA_MLADE", 1L);
        Currency currency = new Currency(1L, CurrencyName.DINAR, "RSD");
        Company company = new Company();
        Employee employee = new Employee();
        when(currencyRepository.findByName(CurrencyName.DINAR)).thenReturn(java.util.Optional.of(currency));
        when(companyRepository.findById(1L)).thenReturn(java.util.Optional.of(company));
        when(employeeRepository.findById(1L)).thenReturn(java.util.Optional.of(employee));
        companyAccountService.create(companyAccountCreateDto, 1L);
        verify(companyAccountRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void deactivateTest() {
        CompanyAccount companyAccount = new CompanyAccount();
        companyAccount.setActive(true);
        when(companyAccountRepository.findById(1L)).thenReturn(java.util.Optional.of(companyAccount));
        companyAccountService.deactivate(1L);
        assertFalse(companyAccount.isActive());

    }

    @Test
    public void deactivateTestNotFound() {
        when(companyAccountRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        assertNull(companyAccountService.deactivate(1L));
    }

    @Test
    public void findAllTest() {
        when(companyAccountRepository.findAll()).thenReturn(java.util.List.of(new CompanyAccount(), new CompanyAccount()));
        assertEquals(2, companyAccountService.findAll().size());
    }

    @Test
    public void findByCompany(){
        Company company = new Company();
        company.setCompanyId(1L);

        CompanyAccount companyAccount = new CompanyAccount();
        companyAccount.setCompany(company);

        CompanyAccount  companyAccount2 = new CompanyAccount();
        companyAccount2.setCompany(company);

        List<CompanyAccount> companyAccounts = List.of(companyAccount, companyAccount2);

        when(companyAccountRepository.findByCompany_CompanyId(1L)).thenReturn(Optional.of(companyAccounts));

        assertEquals(2, companyAccountService.findByCompany(1L).size());

    }

    @Test
    public void findByAccountNumber(){
        CompanyAccount companyAccount = new CompanyAccount();
        companyAccount.setAccountNumber("1234567890");
        when(companyAccountRepository.findByAccountNumber("1234567890")).thenReturn(companyAccount);
        assertEquals(companyAccount.getAccountNumber(), companyAccountService.findByAccountNumber("1234567890").getAccountNumber());
    }

}