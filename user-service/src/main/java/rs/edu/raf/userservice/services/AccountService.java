package rs.edu.raf.userservice.services;

import org.springframework.stereotype.Service;
import rs.edu.raf.userservice.domains.dto.account.AccountCreateDto;
import rs.edu.raf.userservice.domains.dto.account.AccountDto;
import rs.edu.raf.userservice.domains.mappers.AccountMapper;
import rs.edu.raf.userservice.domains.model.Account;
import rs.edu.raf.userservice.domains.model.AccountType;
import rs.edu.raf.userservice.domains.model.Currency;
import rs.edu.raf.userservice.domains.model.enums.AccountTypeName;
import rs.edu.raf.userservice.domains.model.enums.CurrencyName;
import rs.edu.raf.userservice.repositories.*;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final AccountTypeRepository accountTypeRepository;

    private final CurrencyRepository currencyRepository;

    private final UserRepository userRepository;

    private final EmployeeRepository employeeRepository;

    public AccountService(AccountRepository accountRepository,
                          EmployeeRepository employeeRepository,
                          UserRepository userRepository, AccountTypeRepository accountTypeRepository, CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.accountTypeRepository = accountTypeRepository;
        this.employeeRepository = employeeRepository;
    }

    private String randAccNumber(){ //generise broj racuna
        String fixedPart = "5053791";
        StringBuilder builder = new StringBuilder(fixedPart);
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int randomNumber = random.nextInt(10); // Generate a random number between 0-9
            builder.append(randomNumber);
        }
        return builder.toString();
    }

    //ostaje jos da iz jwt-a se doda employeeId
    public AccountDto create(AccountCreateDto accountCreateDto, Long userId) {
        Account account = AccountMapper.INSTANCE.accountCreateDtoToAccount(accountCreateDto);
        account.setAccountNumber(randAccNumber());
        account.setUser(userRepository.findById(accountCreateDto.getUserId()).get());
        account.setEmployee(employeeRepository.findById(accountCreateDto.getEmployeeId()).get());
        account.setCreationDate(System.currentTimeMillis());
        account.setExpireDate(System.currentTimeMillis() + 31556952000L);
        account.setActive(true);// 1 year
        CurrencyName currencyName = CurrencyName.valueOf(accountCreateDto.getCurrency());
        account.setCurrency(currencyRepository.findByName(currencyName).orElseThrow()); //problematicno moze li da bude long jebo me dan, isti problem ispod
        AccountTypeName accountTypeName = AccountTypeName.valueOf(accountCreateDto.getAccountType());
        account.setAvailableBalance(accountCreateDto.getBalance());
        account.setAccountType(accountTypeRepository.findByAccountType(accountTypeName).orElseThrow());
        account = accountRepository.save(account);
        return AccountMapper.INSTANCE.accountToAccountDto(account);
    }

    public void deactivate(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow();
        account.setActive(false);
        accountRepository.save(account);
    }


    public AccountDto findByAccountNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow();
        return AccountMapper.INSTANCE.accountToAccountDto(account);
    }

    public List<AccountDto> findAll() {
        return accountRepository.findAll().stream().map(AccountMapper.INSTANCE::accountToAccountDto).collect(Collectors.toList());
    }

    public List<AccountDto> findByUser(Long userId) {
        List<Account> accounts = accountRepository.findByUser_UserId(userId).orElseThrow();
        return accounts.stream().map(AccountMapper.INSTANCE::accountToAccountDto).collect(Collectors.toList());
    }
}
