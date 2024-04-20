package rs.edu.raf.userservice.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.userservice.domain.model.AccountType;
import rs.edu.raf.userservice.domain.model.enums.AccountTypeName;
import rs.edu.raf.userservice.repositories.AccountTypeRepository;
import rs.edu.raf.userservice.services.AccountTypeService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountTypeServiceTest {

    @Mock
    private AccountTypeRepository accountTypeRepository;

    @InjectMocks
    private AccountTypeService accountTypeService;

    @Test
    public void findAllTest() {
        AccountType accountType = new AccountType();
        accountType.setAccountTypeId(1L);
        accountType.setAccountType(AccountTypeName.ZA_MLADE);

        List<AccountType> accountTypes = new ArrayList<>();
        accountTypes.add(accountType);

        when(accountTypeRepository.findAll()).thenReturn(accountTypes);

        // Act
        List<AccountType> returnedAccountTypes = accountTypeService.findAll();

        // Assert
        verify(accountTypeRepository, times(1)).findAll();
        assertEquals(accountTypes.size(), returnedAccountTypes.size());
        assertEquals(accountTypes.get(0).getAccountTypeId(), returnedAccountTypes.get(0).getAccountTypeId());
        assertEquals(accountTypes.get(0).getAccountType(), returnedAccountTypes.get(0).getAccountType());

    }
}