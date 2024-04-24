package com.example.bankservice;

import com.example.bankservice.domain.dto.account.UserAccountCreateDto;
import com.example.bankservice.domain.dto.account.UserAccountDto;
import com.example.bankservice.domain.dto.companyaccount.CompanyAccountCreateDto;
import com.example.bankservice.domain.dto.companyaccount.CompanyAccountDto;
import com.example.bankservice.domain.model.accounts.Account;
import com.example.bankservice.domain.model.accounts.CompanyAccount;
import com.example.bankservice.domain.model.accounts.UserAccount;
import com.example.bankservice.repository.AccountRepository;
import com.example.bankservice.repository.CardRepository;
import com.example.bankservice.repository.CompanyAccountRepository;
import com.example.bankservice.repository.UserAccountRepository;
import com.example.bankservice.service.AccountService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AccountServiceUnitTest {
    @InjectMocks
    private AccountService accountService;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private UserAccountRepository userAccountRepository;
    @Mock
    private CompanyAccountRepository companyAccountRepository;
    @Mock
    private AccountRepository accountRepository;


    private UserAccount createDummyUserAccount(String accountNumber){
        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(1L);
        userAccount.setActive(true);
        userAccount.setEmployeeId(1L);
        userAccount.setAvailableBalance(BigDecimal.valueOf(1000));
        userAccount.setReservedAmount(BigDecimal.valueOf(0));
        userAccount.setCreationDate(System.currentTimeMillis());
        userAccount.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        userAccount.setAccountNumber(accountNumber);
        return userAccount;
    }

    private CompanyAccount createDummyCompanyAccount(String accountNumber){
        CompanyAccount companyAccount = new CompanyAccount();
        companyAccount.setCompanyId(1L);
        companyAccount.setActive(true);
        companyAccount.setEmployeeId(1L);
        companyAccount.setAvailableBalance(BigDecimal.valueOf(1000));
        companyAccount.setReservedAmount(BigDecimal.valueOf(0));
        companyAccount.setCreationDate(System.currentTimeMillis());
        companyAccount.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        companyAccount.setAccountNumber(accountNumber);
        return companyAccount;
    }

    private UserAccountCreateDto createDummyUserAccountCreateDto(){
        UserAccountCreateDto userAccountCreateDto = new UserAccountCreateDto();
        userAccountCreateDto.setUserId(1L);
        userAccountCreateDto.setEmployeeId(1L);
        userAccountCreateDto.setAvailableBalance(1000.0);
        userAccountCreateDto.setAccountType("ZA_MLADE");
        userAccountCreateDto.setCurrencyMark("RSD");
        return userAccountCreateDto;
    }

    private CompanyAccountCreateDto createDummyCompanyAccountCreateDto(){
        CompanyAccountCreateDto companyAccountCreateDto = new CompanyAccountCreateDto();
        companyAccountCreateDto.setCompanyId(1L);
        companyAccountCreateDto.setEmployeeId(1L);
        companyAccountCreateDto.setBalance(1000.0);
        companyAccountCreateDto.setAccountType("ZA_MLADE");
        companyAccountCreateDto.setCurrencyMark("RSD");
        return companyAccountCreateDto;
    }

    @BeforeAll
    public static void init(){
        MockitoAnnotations.initMocks(AccountServiceUnitTest.class);
    }



    @Test
    public void findAllUserAccountsTest(){
        UserAccount u1 = createDummyUserAccount("1581231231231888");
        UserAccount u2 = createDummyUserAccount("1581231231231999");

        List<UserAccount> accs = List.of(u1,u2);

        given(userAccountRepository.findAll()).willReturn(accs);

        List<UserAccountDto> result = accountService.findAllUserAccounts();

        for(UserAccountDto usdto: result){
            boolean found = false;
            for(UserAccount u: accs){
                if(usdto.getAccountNumber().equals(u.getAccountNumber())){
                    found = true;
                    break;
                }
            }
            if(!found){
                fail("User account not found");
            }
        }
    }

    @Test
    public void findAllComapnyAccountsTest(){
        CompanyAccount c1 = createDummyCompanyAccount("1581231231231888");
        CompanyAccount c2 = createDummyCompanyAccount("1581231231231999");

        List<CompanyAccount> accs = List.of(c1,c2);

        given(companyAccountRepository.findAll()).willReturn(accs);

        List<CompanyAccountDto> result = accountService.findAllCompanyAccounts();

        for(CompanyAccountDto cmdto: result){
            boolean found = false;
            for(CompanyAccount c: accs){
                if(cmdto.getAccountNumber().equals(c.getAccountNumber())){
                    found = true;
                    break;
                }
            }
            if(!found){
                fail("Company account not found");
            }
        }
    }

    @Test
    public void findUserAccountByUserTest(){
        UserAccount u1 = createDummyUserAccount("1581231231231888");
        UserAccount u2 = createDummyUserAccount("1581231231231999");

        List<UserAccount> accs = List.of(u1,u2);

        given(userAccountRepository.findAllByUserId(1L)).willReturn(Optional.of(accs));

        List<UserAccountDto> result = accountService.findUserAccountByUser(1L);

        for(UserAccountDto usdto: result){
            boolean found = false;
            for(UserAccount u: accs){
                if(usdto.getUserId().equals(u.getUserId())){
                    found = true;
                    break;
                }
            }
            if(!found){
                fail("User account not found");
            }
        }
    }

    @Test
    public void findCompanyAccountByCompanyTest(){
        CompanyAccount c1 = createDummyCompanyAccount("1581231231231888");
        CompanyAccount c2 = createDummyCompanyAccount("1581231231231999");

        List<CompanyAccount> accs = List.of(c1,c2);

        given(companyAccountRepository.findAllByCompanyId(1L)).willReturn(Optional.of(accs));

        List<CompanyAccountDto> result = accountService.findAllCompanyAccounts();

        for(CompanyAccountDto cmdto: result){
            boolean found = false;
            for(CompanyAccount c: accs){
                if(cmdto.getCompanyId().equals(c.getCompanyId())){
                    found = true;
                    break;
                }
            }
            if(!found){
                fail("Company account not found");
            }
        }

    }

    @Test
    public void findUserAccountByAccountNumberTest(){
        UserAccount u1 = createDummyUserAccount("1581231231231888");

        given(accountRepository.findByAccountNumber("1581231231231888")).willReturn(Optional.of(u1));

        UserAccountDto result = accountService.findUserAccountByAccountNumber("1581231231231888");
        assertEquals(result.getAccountNumber(), "1581231231231888");
    }
    @Test
    public void findUserAccountByAccountNumber_NotFoundTest(){
        UserAccount u1 = createDummyUserAccount("1581231231231888");

        given(accountRepository.findByAccountNumber("1581231231231888")).willReturn(null);
        assertThrows(RuntimeException.class, () -> accountService.findUserAccountByAccountNumber("1581231231231888"));
    }

    @Test
    public void createUserAccountTest(){
        UserAccountCreateDto createDto = createDummyUserAccountCreateDto();
        UserAccount user = createDummyUserAccount("1581231231231888");
        given(userAccountRepository.save(any(UserAccount.class))).willReturn(user);

        UserAccountDto result = accountService.createUserAccount(createDto);

        assertEquals(result.getUserId(), user.getUserId());
        assertEquals(result.getEmployeeId(), user.getEmployeeId());
    }

    @Test
    public void createCompanyAccountTest(){
        CompanyAccountCreateDto createDto = createDummyCompanyAccountCreateDto();
        CompanyAccount cmp = createDummyCompanyAccount("1581231231231888");
        given(companyAccountRepository.save(any(CompanyAccount.class))).willReturn(cmp);

        CompanyAccountDto result = accountService.createCompanyAccount(createDto);

        assertEquals(result.getCompanyId(), cmp.getCompanyId());
        assertEquals(result.getEmployeeId(), cmp.getEmployeeId());
    }

    @Test
    public void reserveFundsTest() {
        UserAccount u1 = createDummyUserAccount("1581231231231888");
        BigDecimal amount = BigDecimal.valueOf(100);
        given(accountRepository.findByAccountNumber("1581231231231888")).willReturn(Optional.of(u1));

        accountService.reserveFunds(u1, amount);

        assertEquals(u1.getReservedAmount(), BigDecimal.valueOf(100));
        assertEquals(u1.getAvailableBalance(), BigDecimal.valueOf(900));
    }

    @Test
    public void transferFundsTest(){
        UserAccount u1 = createDummyUserAccount("1581231231231888");
        UserAccount u2 = createDummyUserAccount("1581231231231999");
        BigDecimal amount = BigDecimal.valueOf(100);
        given(accountRepository.findByAccountNumber("1581231231231888")).willReturn(Optional.of(u1));
        given(accountRepository.findByAccountNumber("1581231231231999")).willReturn(Optional.of(u2));

        accountService.transferFunds(u1, u2, amount);

        assertEquals(u1.getAvailableBalance(), BigDecimal.valueOf(900));
        assertEquals(u2.getAvailableBalance(), BigDecimal.valueOf(1100));
    }

    @Test
    public void deleteAccountTest(){
        UserAccount u1 = createDummyUserAccount("1581231231231888");
        given(accountRepository.findById(1L)).willReturn(Optional.of(u1));

        accountService.deleteAccount(1L);

        assertFalse(u1.isActive());
    }

    @Test
    public void checkBalanceTest(){
        UserAccount u1 = createDummyUserAccount("1581231231231888");
        given(accountRepository.findByAccountNumber("1581231231231888")).willReturn(Optional.of(u1));

        boolean result = accountService.checkBalance("1581231231231888", 500.0);

        assertEquals(result, true);
    }

    @Test
    public void extractAccountForAccountNumberTest() {
        UserAccount u1 = createDummyUserAccount("1581231231231888");
        given(accountRepository.findByAccountNumber("1581231231231888")).willReturn(Optional.of(u1));
        Account result = accountService.extractAccountForAccountNumber("1581231231231888");

        assertEquals(result.getAccountNumber(), "1581231231231888");
    }

    @Test
    public void extractAccountForAccountNumber_NotFoundTest() {
        UserAccount u1 = createDummyUserAccount("1581231231231888");
        given(accountRepository.findByAccountNumber("1581231231231888")).willReturn(Optional.empty());
        //Account result = accountService.extractAccountForAccountNumber("1581231231231888");

        assertThrows(RuntimeException.class, () -> accountService.extractAccountForAccountNumber("1581231231231888"));
    }

}
