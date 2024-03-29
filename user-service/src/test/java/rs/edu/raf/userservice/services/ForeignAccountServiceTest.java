package rs.edu.raf.userservice.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.userservice.domains.dto.companyaccount.CompanyAccountCreateDto;
import rs.edu.raf.userservice.domains.dto.foreignaccount.ForeignAccountCreateDto;
import rs.edu.raf.userservice.domains.dto.foreignaccount.ForeignAccountDto;
import rs.edu.raf.userservice.domains.model.*;
import rs.edu.raf.userservice.domains.model.enums.AccountTypeName;
import rs.edu.raf.userservice.domains.model.enums.CurrencyName;
import rs.edu.raf.userservice.repositories.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ForeignAccountServiceTest {

    @Mock
    private ForeignAccountRepository foreignAccountRepository;

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    AccountTypeRepository accountTypeRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    CurrencyRepository currencyRepository;

    @InjectMocks
    private ForeignAccountService foreignAccountService;


    @Test
    public void testCreate() {
        ForeignAccountCreateDto foreignAccountCreateDto = new ForeignAccountCreateDto(1L, 1L, 100.0, "DINAR", "ZA_MLADE");
        Currency currency = new Currency(1L, CurrencyName.DINAR, "RSD");
        Company company = new Company();
        Employee employee = new Employee();
        when(currencyRepository.findByName(CurrencyName.DINAR)).thenReturn(java.util.Optional.of(currency));
        when(accountTypeRepository.findByAccountType(AccountTypeName.ZA_MLADE)).thenReturn(java.util.Optional.of(new AccountType()));
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(new User()));
        when(employeeRepository.findById(1L)).thenReturn(java.util.Optional.of(employee));

        foreignAccountService.create(foreignAccountCreateDto, 1L);

        verify(foreignAccountRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void testDeactivate() {
        ForeignAccount foreignAccount = new ForeignAccount();
        foreignAccount.setActive(true);

        when(foreignAccountRepository.findById(1L)).thenReturn(Optional.of(foreignAccount));

        foreignAccountService.deactivate(1L);

        assertFalse(foreignAccount.isActive());
    }

    @Test
    public void testFindByAccountNumber() {
        ForeignAccount foreignAccount = new ForeignAccount();
        foreignAccount.setAccountNumber("1234567890");

        when(foreignAccountRepository.findByAccountNumber("1234567890")).thenReturn(Optional.of(foreignAccount));

        ForeignAccountDto foreignAccountDto = foreignAccountService.findByAccountNumber("1234567890");

        assertEquals(foreignAccount.getAccountNumber(), foreignAccountDto.getAccountNumber());
    }

    @Test
    public void testFindByUser(){
        User user = new User();
        user.setUserId(1L);

        ForeignAccount foreignAccount = new ForeignAccount();
        foreignAccount.setUser(user);

        ForeignAccount foreignAccount1 = new ForeignAccount();
        foreignAccount1.setUser(user);

        List<ForeignAccount> foreignAccountList = List.of(foreignAccount, foreignAccount1);

        when(foreignAccountRepository.findByUser_UserId(user.getUserId())).thenReturn(Optional.of(foreignAccountList));


        List<ForeignAccountDto> foreignAccounts = foreignAccountService.findByUser(user.getUserId());

        assertEquals(2, foreignAccounts.size());
    }

    @Test
    public void findAllTest() {
        ForeignAccount foreignAccount = new ForeignAccount();
        foreignAccount.setAccountNumber("123456789");

        List<ForeignAccount> foreignAccounts = List.of(foreignAccount);

        when(foreignAccountRepository.findAll()).thenReturn(foreignAccounts);

        List<ForeignAccountDto> returnedForeignAccounts = foreignAccountService.findAll();

        Mockito.verify(foreignAccountRepository, Mockito.times(1)).findAll();
        assertEquals(foreignAccounts.size(), returnedForeignAccounts.size());
        assertEquals(foreignAccounts.get(0).getAccountNumber(), returnedForeignAccounts.get(0).getAccountNumber());

    }

}