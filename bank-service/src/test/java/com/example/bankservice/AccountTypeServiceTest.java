package com.example.bankservice;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountTypeServiceTest {

//    @Mock
//    private AccountTypeRepository accountTypeRepository;
//
//    @InjectMocks
//    private AccountTypeService accountTypeService;
//
//    @Test
//    public void findAllTest() {
//        AccountType accountType = new AccountType();
//        accountType.setAccountTypeId(1L);
//        accountType.setAccountType(AccountTypeName.ZA_MLADE);
//
//        List<AccountType> accountTypes = new ArrayList<>();
//        accountTypes.add(accountType);
//
//        when(accountTypeRepository.findAll()).thenReturn(accountTypes);
//
//        // Act
//        List<AccountType> returnedAccountTypes = accountTypeService.findAll();
//
//        // Assert
//        verify(accountTypeRepository, times(1)).findAll();
//        assertEquals(accountTypes.size(), returnedAccountTypes.size());
//        assertEquals(accountTypes.get(0).getAccountTypeId(), returnedAccountTypes.get(0).getAccountTypeId());
//        assertEquals(accountTypes.get(0).getAccountType(), returnedAccountTypes.get(0).getAccountType());
//
//    }
}