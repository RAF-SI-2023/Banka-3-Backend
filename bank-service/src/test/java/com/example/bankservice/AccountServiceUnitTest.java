package com.example.bankservice;

import com.example.bankservice.client.EmailServiceClient;
import com.example.bankservice.client.UserServiceClient;
import com.example.bankservice.domain.dto.account.UserAccountCreateDto;
import com.example.bankservice.domain.dto.account.UserAccountDto;
import com.example.bankservice.domain.dto.account.UserMarginAccountCreateDto;
import com.example.bankservice.domain.dto.account.UserMarginAccountDto;
import com.example.bankservice.domain.dto.companyaccount.CompanyAccountCreateDto;
import com.example.bankservice.domain.dto.companyaccount.CompanyAccountDto;
import com.example.bankservice.domain.dto.companyaccount.CompanyMarginAccountCreateDto;
import com.example.bankservice.domain.dto.companyaccount.CompanyMarginAccountDto;
import com.example.bankservice.domain.dto.emailService.TransactionFinishedDto;
import com.example.bankservice.domain.dto.transaction.StockMarginTransactionDto;
import com.example.bankservice.domain.dto.transaction.StockTransactionDto;
import com.example.bankservice.domain.dto.userService.UserEmailDto;
import com.example.bankservice.domain.mapper.CompanyAccountMapper;
import com.example.bankservice.domain.mapper.UserAccountMapper;
import com.example.bankservice.domain.model.Currency;
import com.example.bankservice.domain.model.accounts.Account;
import com.example.bankservice.domain.model.accounts.CompanyAccount;
import com.example.bankservice.domain.model.accounts.UserAccount;
import com.example.bankservice.domain.model.enums.CurrencyName;
import com.example.bankservice.domain.model.marginAccounts.CompanyMarginAccount;
import com.example.bankservice.domain.model.marginAccounts.MarginAccount;
import com.example.bankservice.domain.model.marginAccounts.UserMarginAccount;
import com.example.bankservice.repository.*;
import com.example.bankservice.service.AccountService;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
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
    private MarginAccountRepository marginAccountRepository;
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
    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private CompositeMeterRegistry meterRegistry;
    @InjectMocks
    private AccountService accountService;

    private Account accountFrom;
    private Account accountTo;
    private BigDecimal amount;

    private StockTransactionDto userStockTransactionDto;
    private StockTransactionDto companyStockTransactionDto;
    private Long userId;
    private Long companyId;
    private String currencyMark;
    private Currency currency;
    private UserAccount userAccount;
    private CompanyAccount companyAccount;

    private static final String exchangeAccountRSD = "1234567891231231";
    private static final String exchangeAccountEUR = "9876543219876543";
    private static final String exchangeAccountUSD = "1098765432101234";
    private static final String exchangeAccountGBP = "9988776655443322";

    private static final Long USER_ID = 1L;
    private static final Long COMPANY_ID = 1L;

    private static final Double AMOUNT = 100.0;

    @BeforeEach
    void setUp() {
        accountFrom = new Account();
        accountFrom.setAccountNumber("123456789");
        accountFrom.setAvailableBalance(BigDecimal.valueOf(1000));

        accountTo = new Account();
        accountTo.setAccountNumber("987654321");
        accountTo.setAvailableBalance(BigDecimal.valueOf(500));

        amount = BigDecimal.valueOf(100);

        userId = 1L;
        companyId = 1L;
        currencyMark = "RSD";

        currency = new Currency();
        currency.setMark(currencyMark);

        userAccount = new UserAccount();
        companyAccount = new CompanyAccount();

        userStockTransactionDto = new StockTransactionDto();
        userStockTransactionDto.setUserId(userId);
        userStockTransactionDto.setCurrencyMark(currencyMark);

        companyStockTransactionDto = new StockTransactionDto();
        companyStockTransactionDto.setCompanyId(companyId);
        companyStockTransactionDto.setCurrencyMark(currencyMark);


    }

    @Test
    void testFindExchangeAccountForGivenCurrency_RSD() {
        // Arrange
        String currencyMark = "RSD";
        Account expectedAccount = new Account();
        when(accountRepository.findByAccountNumber(exchangeAccountRSD)).thenReturn(Optional.of(expectedAccount));

        // Act
        Account result = accountService.findExchangeAccountForGivenCurrency(currencyMark);

        // Assert
        assertEquals(expectedAccount, result);
    }

    @Test
    void testFindExchangeAccountForGivenCurrency_EUR() {
        // Arrange
        String currencyMark = "EUR";
        Account expectedAccount = new Account();
        when(accountRepository.findByAccountNumber(exchangeAccountEUR)).thenReturn(Optional.of(expectedAccount));

        // Act
        Account result = accountService.findExchangeAccountForGivenCurrency(currencyMark);

        // Assert
        assertEquals(expectedAccount, result);
    }
    @Test
    void testFindExchangeAccountForGivenCurrency_USD() {
        // Arrange
        String currencyMark = "USD";
        Account expectedAccount = new Account();
        when(accountRepository.findByAccountNumber(exchangeAccountUSD)).thenReturn(Optional.of(expectedAccount));

        // Act
        Account result = accountService.findExchangeAccountForGivenCurrency(currencyMark);

        // Assert
        assertEquals(expectedAccount, result);
    }

    @Test
    void testFindExchangeAccountForGivenCurrency_GBP() {
        // Arrange
        String currencyMark = "GBP";
        Account expectedAccount = new Account();
        when(accountRepository.findByAccountNumber(exchangeAccountGBP)).thenReturn(Optional.of(expectedAccount));

        // Act
        Account result = accountService.findExchangeAccountForGivenCurrency(currencyMark);

        // Assert
        assertEquals(expectedAccount, result);
    }
    @Test
    void testFindAccount_User() {
        // Mocking currencyRepository.findByMark(...)
        when(currencyRepository.findByMark(currencyMark)).thenReturn(Optional.of(currency));

        // Mocking userAccountRepository.findByUserIdAndCurrency(...)
        when(userAccountRepository.findByUserIdAndCurrency(userId, currency)).thenReturn(userAccount);

        // Calling the method under test for user transaction
        Account result = accountService.findAccount(userStockTransactionDto);

        // Verifying that the methods were called with the correct arguments
        verify(currencyRepository, times(1)).findByMark(currencyMark);
        verify(userAccountRepository, times(1)).findByUserIdAndCurrency(userId, currency);

        // Asserting the result
        assertNotNull(result);
        // Add additional assertions as needed based on the behavior of your method
    }

    @Test
    void testFindAccount_Company() {
        // Mocking currencyRepository.findByMark(...)
        when(currencyRepository.findByMark(currencyMark)).thenReturn(Optional.of(currency));

        // Mocking companyAccountRepository.findByCompanyIdAndCurrency(...)
        when(companyAccountRepository.findByCompanyIdAndCurrency(companyId, currency)).thenReturn(companyAccount);

        // Calling the method under test for company transaction
        Account result = accountService.findAccount(companyStockTransactionDto);

        // Verifying that the methods were called with the correct arguments
        verify(currencyRepository, times(1)).findByMark(currencyMark);
        verify(companyAccountRepository, times(1)).findByCompanyIdAndCurrency(companyId, currency);

        // Asserting the result
        assertNotNull(result);
        // Add additional assertions as needed based on the behavior of your method
    }

    @Test
    void testFindCompanyAccountForIdAndCurrency() {
        // Mocking currencyRepository.findByMark(...)
        when(currencyRepository.findByMark(currencyMark)).thenReturn(Optional.of(currency));

        // Mocking companyAccountRepository.findByCompanyIdAndCurrency(...)
        when(companyAccountRepository.findByCompanyIdAndCurrency(companyId, currency)).thenReturn(companyAccount);

        // Calling the method under test
        Account result = accountService.findCompanyAccountForIdAndCurrency(companyId, currencyMark);

        // Verifying that the methods were called with the correct arguments
        verify(currencyRepository, times(1)).findByMark(currencyMark);
        verify(companyAccountRepository, times(1)).findByCompanyIdAndCurrency(companyId, currency);

        // Asserting the result
        assertNotNull(result);
        // Add additional assertions as needed based on the behavior of your method
    }
    @Test
    void testFindUserAccountForIdAndCurrency() {
        // Mocking currencyRepository.findByMark(...)
        when(currencyRepository.findByMark(currencyMark)).thenReturn(Optional.of(currency));

        // Mocking userAccountRepository.findByUserIdAndCurrency(...)
        when(userAccountRepository.findByUserIdAndCurrency(userId, currency)).thenReturn(userAccount);

        // Calling the method under test
        Account result = accountService.findUserAccountForIdAndCurrency(userId, currencyMark);

        // Verifying that the methods were called with the correct arguments
        verify(currencyRepository, times(1)).findByMark(currencyMark);
        verify(userAccountRepository, times(1)).findByUserIdAndCurrency(userId, currency);

        // Asserting the result
        assertNotNull(result);
        // Add additional assertions as needed based on the behavior of your method
    }
    @Test
    void testTransferStockFunds() {
        accountService.transferStockFunds(accountFrom, accountTo, amount);

        assertEquals(BigDecimal.valueOf(900), accountFrom.getAvailableBalance());
        assertEquals(BigDecimal.valueOf(600), accountTo.getAvailableBalance());

        verify(accountRepository, times(1)).save(accountFrom);
        verify(accountRepository, times(1)).save(accountTo);
    }

    @Test
    void testTransferOtcFunds() {
        accountService.transferOtcFunds(accountFrom, accountTo, amount);

        assertEquals(BigDecimal.valueOf(900), accountFrom.getAvailableBalance());
        assertEquals(BigDecimal.valueOf(600), accountTo.getAvailableBalance());

        verify(accountRepository, times(1)).save(accountFrom);
        verify(accountRepository, times(1)).save(accountTo);
    }

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
    void testCheckBalance_AccountNotFound() {
        // Arrange
        String accountNumber = "non_existing_account_number";
        given(accountRepository.findByAccountNumber(accountNumber)).willReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> accountService.checkBalance(accountNumber, 500.0));
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

    @Test
    public void testCheckBalanceUser_AccountNotFound() {
        // Arrange
        when(accountRepository.findUserAccountByUserId(USER_ID)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            accountService.checkBalanceUser(USER_ID, AMOUNT);
        });

        assertEquals("Account not found", exception.getMessage());
    }

    @Test
    public void testCheckBalanceUser_NoRSDAccount() {
        // Arrange
        UserAccount account = new UserAccount();
        account.setCurrency(new Currency(1l, CurrencyName.DINAR, "Serbian Dinar"));
        account.setAvailableBalance(new BigDecimal("200.0"));

        List<UserAccount> accounts = Arrays.asList(account);
        when(accountRepository.findUserAccountByUserId(USER_ID)).thenReturn(Optional.of(accounts));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            accountService.checkBalanceUser(USER_ID, AMOUNT);
        });

        assertEquals("Account not found", exception.getMessage());
    }

    @Test
    public void testCheckBalanceCompany_AccountNotFound() {
        // Arrange
        when(accountRepository.findCompanyAccountByCompanyId(COMPANY_ID)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            accountService.checkBalanceCompany(COMPANY_ID, AMOUNT);
        });

        assertEquals("Account not found", exception.getMessage());
    }

    @Test
    public void testCheckBalanceCompany_NoRSDAccount() {
        // Arrange
        CompanyAccount account = new CompanyAccount();
        account.setCurrency(new Currency(1l, CurrencyName.DINAR, "Serbian Dinar"));
        account.setAvailableBalance(new BigDecimal("200.0"));

        List<CompanyAccount> accounts = Arrays.asList(account);
        when(accountRepository.findCompanyAccountByCompanyId(COMPANY_ID)).thenReturn(Optional.of(accounts));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            accountService.checkBalanceCompany(COMPANY_ID, AMOUNT);
        });

        assertEquals("Account not found", exception.getMessage());
    }

    @Test
    public void testFindMarginAccountByUserId() {
        // Given
        StockMarginTransactionDto dto = new StockMarginTransactionDto();
        dto.setUserId(1L);

        UserMarginAccount expectedAccount = new UserMarginAccount();
        expectedAccount.setMarginAccountId(1L);

        when(marginAccountRepository.findUserMarginAccountByUserId(1L)).thenReturn(Optional.of(expectedAccount));

        // When
        MarginAccount result = accountService.findMarginAccount(dto);

        // Then
        assertEquals(expectedAccount, result);
    }

    @Test
    public void testFindMarginAccountByCompanyId() {
        // Given
        StockMarginTransactionDto dto = new StockMarginTransactionDto();
        dto.setCompanyId(1L);

        CompanyMarginAccount expectedAccount = new CompanyMarginAccount();
        expectedAccount.setMarginAccountId(2L);

        when(marginAccountRepository.findCompanyMarginAccountByCompanyId(1L)).thenReturn(Optional.of(expectedAccount));

        // When
        MarginAccount result = accountService.findMarginAccount(dto);

        // Then
        assertEquals(expectedAccount, result);
    }

    @Test
    public void testTransferToMarginFunds() {
        // Given
        Account accountFrom = new Account();
        accountFrom.setAvailableBalance(new BigDecimal("1000.00"));

        MarginAccount accountTo = new MarginAccount();
        accountTo.setInitialMargin(new BigDecimal("200.00"));

        BigDecimal amount = new BigDecimal("100.00");

        // When
        accountService.transferToMarginFunds(accountFrom, accountTo, amount);

        // Then
        assertEquals(new BigDecimal("900.00"), accountFrom.getAvailableBalance());
        assertEquals(new BigDecimal("300.00"), accountTo.getInitialMargin());

        verify(accountRepository, times(1)).save(accountFrom);
        verify(marginAccountRepository, times(1)).save(accountTo);
    }

    @Test
    public void testTransferFromMarginFunds() {
        // Given
        MarginAccount accountFrom = new MarginAccount();
        accountFrom.setInitialMargin(new BigDecimal("300.00"));

        Account accountTo = new Account();
        accountTo.setAvailableBalance(new BigDecimal("1000.00"));

        BigDecimal amount = new BigDecimal("100.00");

        // When
        accountService.transferFromMarginFunds(accountFrom, accountTo, amount);

        // Then
        assertEquals(new BigDecimal("200.00"), accountFrom.getInitialMargin());
        assertEquals(new BigDecimal("1100.00"), accountTo.getAvailableBalance());

        verify(marginAccountRepository, times(1)).save(accountFrom);
        verify(accountRepository, times(1)).save(accountTo);
    }

    @Test
    public void testCreateCompanyMarginAccount() {
        // Given
        CompanyMarginAccountCreateDto createDto = new CompanyMarginAccountCreateDto();
        CompanyMarginAccount companyMarginAccount = new CompanyMarginAccount();
        Currency currency = new Currency();
        currency.setMark("RSD");

        CompanyMarginAccountDto expectedDto = new CompanyMarginAccountDto();

        when(companyAccountMapper.companyMarginAccountCreateDtoToCompanyMarginAccount(createDto)).thenReturn(companyMarginAccount);
        when(currencyRepository.findByMark("RSD")).thenReturn(Optional.of(currency));
        when(companyAccountMapper.companyMarginAccountToCompanyMarginAccountDto(companyMarginAccount)).thenReturn(expectedDto);

        // When
        CompanyMarginAccountDto result = accountService.createCompanyMarginAccount(createDto);

        // Then
        verify(marginAccountRepository, times(1)).save(companyMarginAccount);
        assertEquals(currency, companyMarginAccount.getCurrency());
        assertEquals(BigDecimal.ZERO, companyMarginAccount.getLoanValue());
        assertEquals(true, companyMarginAccount.isActive());
        assertEquals(expectedDto, result);
    }

    @Test
    public void testCreateCompanyMarginAccountCurrencyNotFound() {
        // Given
        CompanyMarginAccountCreateDto createDto = new CompanyMarginAccountCreateDto();

        when(currencyRepository.findByMark("RSD")).thenReturn(Optional.empty());

        // When/Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.createCompanyMarginAccount(createDto);
        });

        assertEquals("Invalid currency mark: DINAR", exception.getMessage());
        verify(marginAccountRepository, never()).save(any());
    }

    @Test
    public void testCreateMarginAccount() {
        // Given
        UserMarginAccountCreateDto createDto = new UserMarginAccountCreateDto();
        UserMarginAccount userMarginAccount = new UserMarginAccount();
        Currency currency = new Currency();
        currency.setMark("RSD");

        UserMarginAccountDto expectedDto = new UserMarginAccountDto();

        when(userAccountMapper.userMarginAccountCreateDtoToUserMarginAccount(createDto)).thenReturn(userMarginAccount);
        when(currencyRepository.findByMark("RSD")).thenReturn(Optional.of(currency));
        when(userAccountMapper.userMarginAccountToUserMarginAccountDto(userMarginAccount)).thenReturn(expectedDto);

        // When
        UserMarginAccountDto result = accountService.createMarginAccount(createDto);

        // Then
        verify(marginAccountRepository, times(1)).save(userMarginAccount);
        assertEquals(currency, userMarginAccount.getCurrency());
        assertEquals(BigDecimal.ZERO, userMarginAccount.getLoanValue());
        assertEquals(true, userMarginAccount.isActive());
        //assertEquals(53, new BigInteger(userMarginAccount.getAccountNumber()).bitLength());
        assertEquals(expectedDto, result);
    }

    @Test
    public void testCreateMarginAccountCurrencyNotFound() {
        // Given
        UserMarginAccountCreateDto createDto = new UserMarginAccountCreateDto();

        when(currencyRepository.findByMark("RSD")).thenReturn(Optional.empty());

        // When/Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.createMarginAccount(createDto);
        });

        assertEquals("Invalid currency mark: DINAR", exception.getMessage());
        verify(marginAccountRepository, never()).save(any());
    }

    @Test
    public void testFindCompanyMarginAccountByCompany() {
        // Given
        Long companyId = 1L;
        CompanyMarginAccount companyMarginAccount = new CompanyMarginAccount();
        CompanyMarginAccountDto expectedDto = new CompanyMarginAccountDto();

        when(marginAccountRepository.findCompanyMarginAccountByCompanyId(companyId)).thenReturn(Optional.of(companyMarginAccount));
        when(companyAccountMapper.companyMarginAccountToCompanyMarginAccountDto(companyMarginAccount)).thenReturn(expectedDto);

        // When
        CompanyMarginAccountDto result = accountService.findCompanyMarginAccountByCompany(companyId);

        // Then
        assertEquals(expectedDto, result);
    }

    @Test
    public void testFindCompanyMarginAccountByCompanyNotFound() {
        // Given
        Long companyId = 1L;

        when(marginAccountRepository.findCompanyMarginAccountByCompanyId(companyId)).thenReturn(Optional.empty());

        // When/Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            accountService.findCompanyMarginAccountByCompany(companyId);
        });

        assertEquals("Company margin account not found for company ID: " + companyId, exception.getMessage());
    }

    @Test
    public void testFindUserMarginAccountByUser() {
        // Given
        Long userId = 1L;
        UserMarginAccount userMarginAccount = new UserMarginAccount();
        UserMarginAccountDto expectedDto = new UserMarginAccountDto();

        when(marginAccountRepository.findUserMarginAccountByUserId(userId)).thenReturn(Optional.of(userMarginAccount));
        when(userAccountMapper.userMarginAccountToUserMarginAccountDto(userMarginAccount)).thenReturn(expectedDto);

        // When
        UserMarginAccountDto result = accountService.findUserMarginAccountByUser(userId);

        // Then
        assertEquals(expectedDto, result);
    }

    @Test
    public void testFindUserMarginAccountByUserNotFound() {
        // Given
        Long userId = 1L;

        when(marginAccountRepository.findUserMarginAccountByUserId(userId)).thenReturn(Optional.empty());

        // When/Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            accountService.findUserMarginAccountByUser(userId);
        });

        assertEquals("User margin account not found for user ID: " + userId, exception.getMessage());
    }
}
