package com.example.bankservice;

import com.example.bankservice.client.EmailServiceClient;
import com.example.bankservice.client.UserServiceClient;
import com.example.bankservice.domain.dto.account.UserAccountCreateDto;
import com.example.bankservice.domain.dto.account.UserAccountDto;
import com.example.bankservice.domain.dto.companyaccount.CompanyAccountCreateDto;
import com.example.bankservice.domain.dto.companyaccount.CompanyAccountDto;
import com.example.bankservice.domain.dto.emailService.TransactionFinishedDto;
import com.example.bankservice.domain.dto.userService.UserEmailDto;
import com.example.bankservice.domain.mapper.CompanyAccountMapper;
import com.example.bankservice.domain.mapper.UserAccountMapper;
import com.example.bankservice.domain.model.Currency;
import com.example.bankservice.domain.model.accounts.Account;
import com.example.bankservice.domain.model.accounts.CompanyAccount;
import com.example.bankservice.domain.model.accounts.UserAccount;
import com.example.bankservice.domain.model.enums.CurrencyName;
import com.example.bankservice.repository.AccountRepository;
import com.example.bankservice.repository.CardRepository;
import com.example.bankservice.repository.CompanyAccountRepository;
import com.example.bankservice.repository.UserAccountRepository;
import com.example.bankservice.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceUnitTest {

    @Mock
    private CardRepository cardRepository;
    @Mock
    private UserAccountRepository userAccountRepository;
    @Mock
    private CompanyAccountRepository companyAccountRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private UserAccountMapper userAccountMapper;
    @Mock
    private CompanyAccountMapper companyAccountMapper;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private EmailServiceClient emailServiceClient;
    @Mock
    private Random mockRandom;
    @InjectMocks
    private AccountService accountService;

    private UserAccount createDummyUserAccount(String accountNumber){
        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(1L);
        userAccount.setCurrency(new Currency(1L,CurrencyName.DINAR, "RSD"));
        userAccount.setAccountType("ZA_MLADE");
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
        companyAccount.setCurrency(new Currency(1L,CurrencyName.DINAR, "RSD"));
        companyAccount.setActive(true);
        companyAccount.setEmployeeId(1L);
        companyAccount.setAvailableBalance(BigDecimal.valueOf(1000));
        companyAccount.setReservedAmount(BigDecimal.valueOf(0));
        companyAccount.setCreationDate(System.currentTimeMillis());
        companyAccount.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        //companyAccount.setAccountNumber(accountNumber);
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
        //companyAccountCreateDto.setCompanyId(1L);
        companyAccountCreateDto.setEmployeeId(1L);
        companyAccountCreateDto.setBalance(1000.0);
        companyAccountCreateDto.setAccountType("ZA_MLADE");
        companyAccountCreateDto.setCurrencyMark("RSD");
        return companyAccountCreateDto;
    }

    private TransactionFinishedDto createDummyTransactionFinishedDto(){
        TransactionFinishedDto transactionFinishedDto = new TransactionFinishedDto();
        transactionFinishedDto.setAmount(BigDecimal.valueOf(100.0));
        return transactionFinishedDto;
    }

    private CompanyAccountDto createDummyCompanyAccountDto(){
        CompanyAccountDto companyAccountDto = new CompanyAccountDto();
        companyAccountDto.setCompanyId(1L);
        companyAccountDto.setEmployeeId(1L);
        companyAccountDto.setAvailableBalance(BigDecimal.valueOf(1000));
        companyAccountDto.setReservedAmount(BigDecimal.valueOf(0));
        companyAccountDto.setCreationDate(System.currentTimeMillis());
        companyAccountDto.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        //companyAccountDto.setAccountNumber("1581231231231888");
        companyAccountDto.setCurrency(new Currency(1L,CurrencyName.DINAR, "RSD"));
        return companyAccountDto;

    }

    private UserEmailDto createDummyUserEmailDto(){
        UserEmailDto userEmailDto = new UserEmailDto();
        userEmailDto.setUserId(1L);
        userEmailDto.setEmail("dpopovic10720rn@raf.rs");
        return userEmailDto;
    }

    private UserAccountDto createDummyUserAccountDto(){
        UserAccountDto userAccountDto = new UserAccountDto();
        userAccountDto.setUserId(1L);
        userAccountDto.setEmployeeId(1L);
        userAccountDto.setAvailableBalance(BigDecimal.valueOf(1000));
        userAccountDto.setReservedAmount(BigDecimal.valueOf(0));
        userAccountDto.setCreationDate(System.currentTimeMillis());
        userAccountDto.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        userAccountDto.setAccountNumber("1581231231231888");
        userAccountDto.setCurrency(new Currency(1L,CurrencyName.DINAR, "RSD"));
        userAccountDto.setAccountType("ZA_MLADE");
        return userAccountDto;

    }



//    @BeforeAll
//    //public static void init(){
//        MockitoAnnotations.initMocks(AccountServiceUnitTest.class);
//    }



    @Test
    public void findAllUserAccountsTest(){
        UserAccount u1 = createDummyUserAccount("1581231231231888");
        UserAccount u2 = createDummyUserAccount("1581231231231999");
        UserAccountDto dto2 = createDummyUserAccountDto();
        dto2.setAccountNumber("1581231231231999");

        List<UserAccount> accs = List.of(u1,u2);
        given(userAccountMapper.userAccountToUserAccountDto(u1)).willReturn(createDummyUserAccountDto());
        given(userAccountMapper.userAccountToUserAccountDto(u2)).willReturn(dto2);
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
        c1.setAccountNumber("1581231231231888");
        CompanyAccount c2 = createDummyCompanyAccount("1581231231231999");
        c2.setAccountNumber("1581231231231999");
        CompanyAccountDto dto2 = createDummyCompanyAccountDto();
        dto2.setAccountNumber("1581231231231999");
        List<CompanyAccount> accs = List.of(c1,c2);

        given(companyAccountMapper.companyAccountToCompanyAccountDto(c1)).willReturn(createDummyCompanyAccountDto());
        given(companyAccountMapper.companyAccountToCompanyAccountDto(c2)).willReturn(dto2);
        given(companyAccountRepository.findAll()).willReturn(accs);

        List<CompanyAccountDto> result = accountService.findAllCompanyAccounts();

        for(CompanyAccountDto cmdto: result){
            boolean found = false;
            for(CompanyAccount c: accs){
                System.out.println(cmdto.getAccountNumber() + " " + c.getAccountNumber());
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
        UserAccountDto dto2 = createDummyUserAccountDto();
        dto2.setAccountNumber("1581231231231999");

        List<UserAccount> accs = List.of(u1,u2);

        given(accountRepository.findUserAccountByUserId(1L)).willReturn(Optional.of(accs));
        given(userAccountMapper.userAccountToUserAccountDto(u1)).willReturn(createDummyUserAccountDto());
        given(userAccountMapper.userAccountToUserAccountDto(u2)).willReturn(dto2);

        List<UserAccountDto> result = accountService.findUserAccountByUser(1L);
        System.out.println(result.toString());

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
    public void findBankAccountForGivenCurrencyTest(){
        UserAccount u1 = createDummyUserAccount("1581231231231888");
        UserAccountDto dto2 = createDummyUserAccountDto();
        dto2.setAccountNumber("1581231231231999");

        List<String> currencies = List.of("RSD", "EUR","USD","CHF","GBP","JPY");

        for(String currency: currencies) {
            u1.setCurrency(new Currency(1L, CurrencyName.DINAR, currency));
            given(accountRepository.findByAccountNumber(anyString())).willReturn(Optional.of(u1));
            Account result = accountService.findBankAccountForGivenCurrency(currency);
        }

    }

    @Test
    public void findCompanyAccountByCompanyTest(){
        CompanyAccount c1 = createDummyCompanyAccount("1581231231231888");
        CompanyAccount c2 = createDummyCompanyAccount("1581231231231999");
        CompanyAccountDto dto = createDummyCompanyAccountDto();
        List<CompanyAccount> accs = List.of(c1,c2);
        given(companyAccountMapper.companyAccountToCompanyAccountDto(c1)).willReturn(createDummyCompanyAccountDto());
        given(accountRepository.findCompanyAccountByCompanyId(1L)).willReturn(Optional.of(accs));

        List<CompanyAccountDto> result = accountService.findCompanyAccountByCompany(1L);

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

        given(userAccountMapper.userAccountToUserAccountDto(u1)).willReturn(createDummyUserAccountDto());
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
        //given(userAccountRepository.save(any(UserAccount.class))).willReturn(user);

        given(userAccountMapper.userAccountCreateDtoToUserAccount(createDto)).willReturn(user);
        given(userAccountMapper.userAccountToUserAccountDto(any())).willReturn(createDummyUserAccountDto());
        UserAccountDto result = accountService.createUserAccount(createDto);

        assertEquals(result.getUserId(), user.getUserId());
        assertEquals(result.getEmployeeId(), user.getEmployeeId());
    }

    @Test
    public void createCompanyAccountTest(){
        CompanyAccountCreateDto createDto = createDummyCompanyAccountCreateDto();
        CompanyAccount cmp = createDummyCompanyAccount("1581231231231888");
        given(companyAccountMapper.companyAccountCreateDtoToCompanyAccount(createDto)).willReturn(cmp);
        //given(companyAccountRepository.save(any(CompanyAccount.class))).willReturn(cmp);
        given(companyAccountMapper.companyAccountToCompanyAccountDto(any())).willReturn(createDummyCompanyAccountDto());

        CompanyAccountDto result = accountService.createCompanyAccount(createDto);

        assertEquals(result.getCompanyId(), cmp.getCompanyId());
        assertEquals(result.getEmployeeId(), cmp.getEmployeeId());
    }

    @Test
    public void reserveFundsTest() {
        UserAccount u1 = createDummyUserAccount("1581231231231888");
        BigDecimal amount = BigDecimal.valueOf(100);
        //given(accountRepository.findByAccountNumber("1581231231231888")).willReturn(Optional.of(u1));

        accountService.reserveFunds(u1, amount);

        assertEquals(u1.getReservedAmount(), BigDecimal.valueOf(100));
        assertEquals(u1.getAvailableBalance(), BigDecimal.valueOf(900));
    }

    @Test
    public void transferFundsTest(){
        UserAccount u1 = createDummyUserAccount("1581231231231888");
        UserAccount u2 = createDummyUserAccount("1581231231231999");
        UserEmailDto email = createDummyUserEmailDto();

        TransactionFinishedDto tfdto = createDummyTransactionFinishedDto();
        tfdto.setCurrencyMark("RSD");
        tfdto.setEmail("dpopovic10720rn@raf.rs");
        BigDecimal amount = BigDecimal.valueOf(100.0);

        //given(accountRepository.findByAccountNumber("1581231231231888")).willReturn(Optional.of(u1));
        //given(accountRepository.findByAccountNumber("1581231231231999")).willReturn(Optional.of(u2));
        given(userServiceClient.getEmailByUserId(anyString())).willReturn(email);
        given(emailServiceClient.sendTransactionFinishedEmailToEmailService(tfdto)).willReturn(any());
        accountService.transferFunds(u1, u2, amount);

        assertEquals(u1.getAvailableBalance(), BigDecimal.valueOf(1000));
        assertEquals(u2.getAvailableBalance(), BigDecimal.valueOf(1100.0));
    }

    @Test
    public void transferCreditFundsTest(){
        UserAccount u1 = createDummyUserAccount("1581231231231888");
        UserAccount u2 = createDummyUserAccount("1581231231231999");
        UserEmailDto email = createDummyUserEmailDto();

        TransactionFinishedDto tfdto = createDummyTransactionFinishedDto();
        tfdto.setCurrencyMark("RSD");
        tfdto.setEmail("dpopovic10720rn@raf.rs");
        BigDecimal amount = BigDecimal.valueOf(100.0);

        //given(accountRepository.findByAccountNumber("1581231231231888")).willReturn(Optional.of(u1));
        //given(accountRepository.findByAccountNumber("1581231231231999")).willReturn(Optional.of(u2));
        given(userServiceClient.getEmailByUserId(anyString())).willReturn(email);
        given(emailServiceClient.sendTransactionFinishedEmailToEmailService(tfdto)).willReturn(any());
        accountService.transferCreditFunds(u1, u2, amount);

        assertEquals(u1.getAvailableBalance(), BigDecimal.valueOf(900.0));
        assertEquals(u2.getAvailableBalance(), BigDecimal.valueOf(1100.0));
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
