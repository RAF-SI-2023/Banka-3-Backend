package rs.edu.raf.userservice.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rs.edu.raf.userservice.domain.dto.account.AccountCreateDto;
import rs.edu.raf.userservice.domain.dto.account.AccountDto;
import rs.edu.raf.userservice.domain.dto.account.CheckEnoughBalanceDto;
import rs.edu.raf.userservice.domain.dto.account.RebalanceAccountDto;
import rs.edu.raf.userservice.domain.model.*;
import rs.edu.raf.userservice.domain.model.enums.AccountTypeName;
import rs.edu.raf.userservice.domain.model.enums.CurrencyName;
import rs.edu.raf.userservice.repositories.*;
import rs.edu.raf.userservice.services.AccountService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AccountServiceUnitTests {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    AccountTypeRepository accountTypeRepository;
    @Mock
    private CardRepository cardRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addMoneyToAccountShouldReturnOkWhenAccountExists() {
        RebalanceAccountDto dto = new RebalanceAccountDto();
        dto.setCurrencyMark("RSD");
        dto.setAccountNumber("5053791123456789");
        dto.setAmount(100.0);

        Account account = new Account();
        account.setAccountNumber("5053791123456789");
        account.setAvailableBalance(BigDecimal.valueOf(200.0));
        Currency currency = new Currency();
        currency.setName(CurrencyName.DINAR);
        currency.setMark("RSD");

        account.setCurrency(currency);

        when(accountRepository.findByAccountNumber(dto.getAccountNumber())).thenReturn(Optional.of(account));

        ResponseEntity<String> response = accountService.addMoneyToAccount(dto);

        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    public void addMoneyToAccountShouldReturnBadRequestWhenAccountDoesNotExist() {
        RebalanceAccountDto dto = new RebalanceAccountDto();
        dto.setAccountNumber("5053791123456789");
        dto.setAmount(100.0);

        when(accountRepository.findByAccountNumber(dto.getAccountNumber())).thenReturn(Optional.empty());

        ResponseEntity<String> response = accountService.addMoneyToAccount(dto);

        assertEquals(ResponseEntity.badRequest().build(), response);
    }

    @Test
    public void takeMoneyFromAccountShouldReturnOkWhenAccountExistsAndHasEnoughBalance() {
        RebalanceAccountDto dto = new RebalanceAccountDto();
        dto.setAccountNumber("5053791123456789");
        dto.setAmount(100.0);

        Account account = new Account();
        account.setAccountNumber("5053791123456789");
        account.setAvailableBalance(BigDecimal.valueOf(200.0));

        when(accountRepository.findByAccountNumber(dto.getAccountNumber())).thenReturn(Optional.of(account));

        ResponseEntity<String> response = accountService.takeMoneyFromAccount(dto);

        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    public void takeMoneyFromAccountShouldReturnBadRequestWhenAccountDoesNotExist() {
        RebalanceAccountDto dto = new RebalanceAccountDto();
        dto.setAccountNumber("5053791123456789");
        dto.setAmount(100.0);

        when(accountRepository.findByAccountNumber(dto.getAccountNumber())).thenReturn(Optional.empty());

        ResponseEntity<String> response = accountService.takeMoneyFromAccount(dto);

        assertEquals(ResponseEntity.badRequest().build(), response);
    }

//    @Test
//    public void takeMoneyFromAccountShouldReturnBadRequestWhenAccountDoesNotHaveEnoughBalance() {
//        RebalanceAccountDto dto = new RebalanceAccountDto();
//        dto.setAccountNumber("5053791123456789");
//        dto.setAmount(300.0);
//
//        Account account = new Account();
//        account.setAccountNumber("5053791123456789");
//        account.setBalance(200.0);
//
//        when(accountRepository.findByAccountNumber(dto.getAccountNumber())).thenReturn(Optional.of(account));
//
//        ResponseEntity<String> response = accountService.takeMoneyFromAccount(dto);
//
//        assertEquals(ResponseEntity.badRequest().build(), response);
//    }

    @Test
    public void getEmailByAccountNumber(){
        String accountNumber = "5053791123456789";
        Account account = new Account();
        User user = createDummyUser("pera1234@gmail.com");
        account.setUser(user);
        account.setAccountNumber(accountNumber);
        account.setReservedAmount(BigDecimal.valueOf(100.0));
        account.setAvailableBalance(BigDecimal.valueOf(100.0));
        account.setActive(true);
        account.setCreationDate(123456789L);
        account.setExpireDate(123456789L);
        account.setCurrency(null);
        account.setAccountType(null);

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

        String email = accountService.getEmailByAccountNumber(accountNumber);

        assertEquals(user.getEmail(), email);
    }

    @Test
    public void reserveMoneyTest(){
        RebalanceAccountDto dto = new RebalanceAccountDto();
        dto.setAccountNumber("5053791123456789");
        dto.setAmount(100.0);
        dto.setCurrencyMark("RSD");

        Account account = new Account();
        account.setAccountNumber("5053791123456789");
        account.setReservedAmount(BigDecimal.valueOf(0.0));
        account.setAvailableBalance(BigDecimal.valueOf(200.0));
        Currency currency = new Currency();
        currency.setMark("RSD");
        account.setCurrency(currency);

        when(accountRepository.findByAccountNumber(dto.getAccountNumber())).thenReturn(Optional.of(account));

        ResponseEntity<String> response = accountService.reserveMoney(dto);

        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    public void reserveMoneyTestFail(){
        RebalanceAccountDto dto = new RebalanceAccountDto();
        dto.setAccountNumber("5053791123456789");
        dto.setAmount(100.0);
        dto.setCurrencyMark("RSD");

        Account account = new Account();
        account.setAccountNumber("5053791123456789");
        account.setReservedAmount(BigDecimal.valueOf(100.0));
        account.setAvailableBalance(BigDecimal.valueOf(100.0));
        Currency currency = new Currency();
        currency.setMark("EUR");
        account.setCurrency(currency);

        when(accountRepository.findByAccountNumber(dto.getAccountNumber())).thenReturn(Optional.of(account));

        ResponseEntity<String> response = accountService.reserveMoney(dto);

        assertEquals(ResponseEntity.badRequest().build(), response);
    }

    @Test
    public void unreserveMoneyTest(){
        RebalanceAccountDto dto = new RebalanceAccountDto();
        dto.setAccountNumber("5053791123456789");
        dto.setAmount(100.0);
        dto.setCurrencyMark("RSD");

        Account account = new Account();
        account.setAccountNumber("5053791123456789");
        account.setReservedAmount(BigDecimal.valueOf(100.0));
        account.setAvailableBalance(BigDecimal.valueOf(100.0));
        Currency currency = new Currency();
        currency.setMark("RSD");
        account.setCurrency(currency);

        when(accountRepository.findByAccountNumber(dto.getAccountNumber())).thenReturn(Optional.of(account));

        ResponseEntity<String> response = accountService.unreserveMoney(dto);

        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    public void createTest(){
        AccountCreateDto accountCreateDto = new AccountCreateDto();
        accountCreateDto.setUserId(1L);
        accountCreateDto.setEmployeeId(1L);
        accountCreateDto.setBalance(200.0);
        accountCreateDto.setCurrency("DINAR");
        accountCreateDto.setAccountType("ZA_MLADE");

        Account account = new Account();
        User user = createDummyUser("pera1234@gmail.com");
        Employee employee = createDummyEmployee("mika1234@gmail.com");

        when(userRepository.findById(accountCreateDto.getUserId())).thenReturn(Optional.of(user));
        when(employeeRepository.findById(accountCreateDto.getEmployeeId())).thenReturn(Optional.of(employee));

        Currency currency = new Currency();
        currency.setMark("RSD");
        currency.setName(CurrencyName.DINAR);

        when(currencyRepository.findByName(currency.getName())).thenReturn(Optional.of(currency));

        AccountType accountType = new AccountType();
        accountType.setAccountType(AccountTypeName.ZA_MLADE);

        when(accountTypeRepository.findByAccountType(AccountTypeName.ZA_MLADE)).thenReturn(Optional.of(accountType) );

        account.setAvailableBalance(BigDecimal.valueOf(accountCreateDto.getBalance()));
        account.setCurrency(currency);
        account.setAccountType(accountType);
        account.setUser(user);
        account.setEmployee(employee);


        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Card card = new Card();
        card.setUserId(accountCreateDto.getUserId());
        card.setCardNumber("12345678");
        card.setAccountNumber(account.getAccountNumber());
        card.setCardName("no_name_card");
        card.setCreationDate(System.currentTimeMillis());
        card.setExpireDate(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000);
        card.setCvv("123");
        card.setStatus(true);

        when(cardRepository.save(any(Card.class))).thenReturn(card);
        AccountDto account1 = accountService.create(accountCreateDto);

        assertEquals(account1.getAvailableBalance(), account.getAvailableBalance());
        assertEquals(account1.getCurrency(), account.getCurrency());
        assertEquals(account1.getUser(), account.getUser());
        assertEquals(account1.getEmployee(), account.getEmployee());
    }

    @Test
    public void deactiveTest(){
        Account account = new Account();
        account.setAccountNumber("5053791123456789");
        account.setReservedAmount(BigDecimal.valueOf(100.0));
        account.setAvailableBalance(BigDecimal.valueOf(100.0));
        account.setActive(true);
        account.setCreationDate(123456789L);
        account.setExpireDate(123456789L);
        account.setCurrency(null);
        account.setAccountType(null);

        when(accountRepository.findById(account.getAccountId())).thenReturn(Optional.of(account));

        accountService.deactivate(account.getAccountId());

        assertEquals(false, account.isActive());
    }

    @Test
    public void checkEnoughBalanceTest(){
        Account account = new Account();
        account.setAccountNumber("5053791123456789");
        account.setReservedAmount(BigDecimal.valueOf(100.0));
        account.setAvailableBalance(BigDecimal.valueOf(400.0));
        account.setActive(true);
        account.setCreationDate(123456789L);
        account.setExpireDate(123456789L);
        Currency currency = new Currency();
        currency.setMark("RSD");
        account.setCurrency(currency);
        AccountType accountType = new AccountType();
        accountType.setAccountType(AccountTypeName.ZA_MLADE);

        CheckEnoughBalanceDto checkEnoughBalanceDto = new CheckEnoughBalanceDto();
        checkEnoughBalanceDto.setAccountNumber("5053791123456789");
        checkEnoughBalanceDto.setAmount(100.0);
        checkEnoughBalanceDto.setCurrencyMark("RSD");


        when(accountRepository.findByAccountNumber(checkEnoughBalanceDto.getAccountNumber())).thenReturn(Optional.of(account));

        ResponseEntity<String> response = accountService.checkEnoughBalance(checkEnoughBalanceDto);

        assertEquals(ResponseEntity.ok().build(), response);

    }

    @Test
    public void checkEnoughBalanceTest_NotEnought(){
        Account account = new Account();
        account.setAccountNumber("5053791123456789");
        account.setReservedAmount(BigDecimal.valueOf(100.0));
        account.setAvailableBalance(BigDecimal.valueOf(100.0));
        account.setActive(true);
        account.setCreationDate(123456789L);
        account.setExpireDate(123456789L);
        Currency currency = new Currency();
        currency.setMark("RSD");
        account.setCurrency(currency);
        AccountType accountType = new AccountType();
        accountType.setAccountType(AccountTypeName.ZA_MLADE);

        CheckEnoughBalanceDto checkEnoughBalanceDto = new CheckEnoughBalanceDto();
        checkEnoughBalanceDto.setAccountNumber("5053791123456789");
        checkEnoughBalanceDto.setAmount(300.0);
        checkEnoughBalanceDto.setCurrencyMark("RSD");


        when(accountRepository.findByAccountNumber(checkEnoughBalanceDto.getAccountNumber())).thenReturn(Optional.of(account));

        ResponseEntity<String> response = accountService.checkEnoughBalance(checkEnoughBalanceDto);

        assertEquals(ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Not enough money on balance!"), response);
    }

    @Test
    public void checkEnoughBalanceTest_NotEqualMark(){
        Account account = new Account();
        account.setAccountNumber("5053791123456789");
        account.setReservedAmount(BigDecimal.valueOf(100.0));
        account.setAvailableBalance(BigDecimal.valueOf(100.0));
        account.setActive(true);
        account.setCreationDate(123456789L);
        account.setExpireDate(123456789L);
        Currency currency = new Currency();
        currency.setMark("EUR");
        account.setCurrency(currency);
        AccountType accountType = new AccountType();
        accountType.setAccountType(AccountTypeName.ZA_MLADE);

        CheckEnoughBalanceDto checkEnoughBalanceDto = new CheckEnoughBalanceDto();
        checkEnoughBalanceDto.setAccountNumber("5053791123456789");
        checkEnoughBalanceDto.setAmount(100.0);
        checkEnoughBalanceDto.setCurrencyMark("RSD");


        when(accountRepository.findByAccountNumber(checkEnoughBalanceDto.getAccountNumber())).thenReturn(Optional.of(account));

        ResponseEntity<String> response = accountService.checkEnoughBalance(checkEnoughBalanceDto);

        assertEquals(ResponseEntity.ok().build(), response);

    }

    @Test
    public void checkEnoughBalanceTest_NotEnought_NotEqualMark(){
        Account account = new Account();
        account.setAccountNumber("5053791123456789");
        account.setReservedAmount(BigDecimal.valueOf(100.0));
        account.setAvailableBalance(BigDecimal.valueOf(100.0));
        account.setActive(true);
        account.setCreationDate(123456789L);
        account.setExpireDate(123456789L);
        Currency currency = new Currency();
        currency.setMark("RSD");
        account.setCurrency(currency);
        AccountType accountType = new AccountType();
        accountType.setAccountType(AccountTypeName.ZA_MLADE);

        CheckEnoughBalanceDto checkEnoughBalanceDto = new CheckEnoughBalanceDto();
        checkEnoughBalanceDto.setAccountNumber("5053791123456789");
        checkEnoughBalanceDto.setAmount(300.0);
        checkEnoughBalanceDto.setCurrencyMark("EUR");


        when(accountRepository.findByAccountNumber(checkEnoughBalanceDto.getAccountNumber())).thenReturn(Optional.of(account));

        ResponseEntity<String> response = accountService.checkEnoughBalance(checkEnoughBalanceDto);

        assertEquals(ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Not enough money on balance!"), response);
    }


    @Test
    public void findByAccountNumber(){
        String accountNumber = "5053791123456789";
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setReservedAmount(BigDecimal.valueOf(100.0));
        account.setAvailableBalance(BigDecimal.valueOf(100.0));
        account.setActive(true);
        account.setCreationDate(123456789L);
        account.setExpireDate(123456789L);
        account.setCurrency(null);
        account.setAccountType(null);

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

        AccountDto accountDto = accountService.findByAccountNumber(accountNumber);

        assertEquals(account.getAvailableBalance(), accountDto.getAvailableBalance());
        assertEquals(account.getAvailableBalance(), accountDto.getAvailableBalance());
        assertEquals(account.isActive(), accountDto.isActive());
        assertEquals(account.getCreationDate(), accountDto.getCreationDate());
        assertEquals(account.getExpireDate(), accountDto.getExpireDate());
    }

    @Test
    public void findAll(){
        Account account = new Account();
        account.setAccountNumber("5053791123456789");
        account.setReservedAmount(BigDecimal.valueOf(100.0));
        account.setAvailableBalance(BigDecimal.valueOf(100.0));
        account.setActive(true);
        account.setCreationDate(123456789L);
        account.setExpireDate(123456789L);
        account.setCurrency(null);
        account.setAccountType(null);

        ArrayList<Account> accounts = new ArrayList<>();
        accounts.add(account);

        when(accountRepository.findAll()).thenReturn(accounts);

        ArrayList<AccountDto> accountDtos = (ArrayList<AccountDto>) accountService.findAll();

        assertEquals(accounts.size(), accountDtos.size());
        assertEquals(accounts.get(0).getAvailableBalance(), accountDtos.get(0).getAvailableBalance());
        assertEquals(accounts.get(0).isActive(), accountDtos.get(0).isActive());
        assertEquals(accounts.get(0).getCreationDate(), accountDtos.get(0).getCreationDate());
        assertEquals(accounts.get(0).getExpireDate(), accountDtos.get(0).getExpireDate());
    }

    @Test
    public void findByUser(){
        User user = createDummyUser("pera1234@gmail.com");
        Account account = new Account();
        account.setAccountNumber("5053791123456789");
        account.setReservedAmount(BigDecimal.valueOf(100.0));
        account.setAvailableBalance(BigDecimal.valueOf(100.0));
        account.setActive(true);
        account.setCreationDate(123456789L);
        account.setExpireDate(123456789L);
        account.setCurrency(null);
        account.setAccountType(null);
        account.setUser(user);

        ArrayList<Account> accounts = new ArrayList<>();
        accounts.add(account);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(accountRepository.findByUser_UserId(user.getUserId())).thenReturn(Optional.of(accounts));

        ArrayList<AccountDto> accountDtos = (ArrayList<AccountDto>) accountService.findByUser(user.getUserId());

        assertEquals(accounts.size(), accountDtos.size());
        assertEquals(accounts.get(0).getAvailableBalance(), accountDtos.get(0).getAvailableBalance());
        assertEquals(accounts.get(0).isActive(), accountDtos.get(0).isActive());
        assertEquals(accounts.get(0).getCreationDate(), accountDtos.get(0).getCreationDate());
        assertEquals(accounts.get(0).getExpireDate(), accountDtos.get(0).getExpireDate());
    }



    private User createDummyUser(String email) {
        User user = new User();
        user.setUserId(1L);
        user.setFirstName("Pera");
        user.setLastName("Peric");
        user.setJmbg("1234567890123");
        user.setDateOfBirth(123L);
        user.setGender("M");
        user.setPhoneNumber("+3123214254");
        user.setAddress("Mika Mikic 13");
        user.setEmail(email);
        user.setPassword("pera1234");
        user.setActive(true);

        return user;
    }

    private Employee createDummyEmployee(String email) {
        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setFirstName("Pera");
        employee.setLastName("Peric");
        employee.setUsername("perica");
        employee.setJmbg("1234567890123");
        employee.setDateOfBirth(123L);
        employee.setGender("M");
        employee.setPhoneNumber("+3123214254");
        employee.setEmail(email);
        employee.setPassword("pera1234");
        employee.setIsActive(true);
        employee.setAddress("Mika Mikic 13");
        employee.setPermissions(new ArrayList<>());

        return employee;
    }
}