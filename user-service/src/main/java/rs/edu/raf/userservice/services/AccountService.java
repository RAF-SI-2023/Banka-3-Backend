package rs.edu.raf.userservice.services;

import feign.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rs.edu.raf.userservice.domains.dto.account.AccountCreateDto;
import rs.edu.raf.userservice.domains.dto.account.AccountDto;
import rs.edu.raf.userservice.domains.dto.account.CheckEnoughBalanceDto;
import rs.edu.raf.userservice.domains.dto.account.RebalanceAccountDto;
import rs.edu.raf.userservice.domains.mappers.AccountMapper;
import rs.edu.raf.userservice.domains.model.Account;
import rs.edu.raf.userservice.domains.model.AccountType;
import rs.edu.raf.userservice.domains.model.Currency;
import rs.edu.raf.userservice.domains.model.enums.AccountTypeName;
import rs.edu.raf.userservice.domains.model.enums.CurrencyName;
import rs.edu.raf.userservice.repositories.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

    //TODO dodati u test

    public ResponseEntity<String>addMoneyToAccount(RebalanceAccountDto dto){
        Optional<Account> optionalAccount=accountRepository.findByAccountNumber(dto.getAccountNumber());
        if(!optionalAccount.isPresent()) return ResponseEntity.badRequest().build();
        Account account=optionalAccount.get();
        account.setBalance(account.getBalance()+dto.getAmount());
        accountRepository.save(account);
        return ResponseEntity.ok().build();

    }

    //TODO dodati u test
    public ResponseEntity<String>takeMoneyFromAccount(RebalanceAccountDto dto){
        Optional<Account> optionalAccount=accountRepository.findByAccountNumber(dto.getAccountNumber());
        if(!optionalAccount.isPresent()) return ResponseEntity.badRequest().build();
        Account account=optionalAccount.get();
        account.setBalance(account.getBalance()-dto.getAmount());
        accountRepository.save(account);
        return ResponseEntity.ok().build();

    }
    public String getEmailByAccountNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow();
        return account.getUser().getEmail();
    }
    public ResponseEntity<String>reserveMoney(RebalanceAccountDto dto){


        Optional<Account> optionalAccount=accountRepository.findByAccountNumber(dto.getAccountNumber());
        if(!optionalAccount.isPresent()) return ResponseEntity.badRequest().build();
        Account account=optionalAccount.get();

        if(!account.getCurrency().getMark().equalsIgnoreCase(dto.getCurrencyMark())){
            Double convertedAmount = convertCurrency(dto.getCurrencyMark(), account.getCurrency().getMark(), dto.getAmount());
            if(account.getBalance()<convertedAmount) return ResponseEntity.badRequest().build();
            account.setBalance(account.getBalance()-convertedAmount);
            account.setReservedAmount(account.getReservedAmount()+convertedAmount);
            accountRepository.save(account);
            return ResponseEntity.ok().build();
        }

        if(account.getBalance()<dto.getAmount()) return ResponseEntity.badRequest().build();
        account.setBalance(account.getBalance()-dto.getAmount());
        account.setReservedAmount(account.getReservedAmount()+dto.getAmount());
        accountRepository.save(account);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<String>unreserveMoney(RebalanceAccountDto dto){
        Optional<Account> optionalAccount=accountRepository.findByAccountNumber(dto.getAccountNumber());
        if(!optionalAccount.isPresent()) return ResponseEntity.badRequest().build();
        Account account=optionalAccount.get();
        if(account.getReservedAmount()<dto.getAmount()) return ResponseEntity.badRequest().build();
        account.setBalance(account.getBalance()+dto.getAmount());
        account.setReservedAmount(account.getReservedAmount()-dto.getAmount());
        accountRepository.save(account);
        return ResponseEntity.ok().build();
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



    private static final String API_KEY = "96aa86545baf8162d6ecbe21";
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/pair/";


    //EUR,USD
    private Double convertCurrency(String fromCurrency, String toCurrency, Double amount) {
        fromCurrency=fromCurrency.toUpperCase();
        toCurrency=toCurrency.toUpperCase();
        try {

            String urlStr = API_URL + fromCurrency + "/" + toCurrency + "/" + amount;
            URL url = new URL(urlStr);


            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");


            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            String jsonResponse = response.toString();
            Double conversionRate = parseJsonResponse(jsonResponse);


            Double convertedAmount = amount * conversionRate;

            return convertedAmount;
        } catch (IOException e) {
            e.printStackTrace();

        }
        return null;
    }

    // Method to parse JSON response and extract conversion rate
    private Double parseJsonResponse(String jsonResponse) {

        String conversionRateKey = "\"conversion_rate\":";
        int startIndex = jsonResponse.indexOf(conversionRateKey);
        if (startIndex != -1) {
            int endIndex = jsonResponse.indexOf(",", startIndex + conversionRateKey.length());
            if (endIndex != -1) {
                String rateString = jsonResponse.substring(startIndex + conversionRateKey.length(), endIndex);
                return Double.parseDouble(rateString.trim());
            }
        }
        return null;
    }



    //TODO ubaciti u test
    public ResponseEntity<String>checkEnoughBalance(CheckEnoughBalanceDto dto){
        Optional<Account> optionalAccount=accountRepository.findByAccountNumber(dto.getAccountNumber());

        if(!optionalAccount.isPresent()) return ResponseEntity.badRequest().build();

        Account account=optionalAccount.get();

        //use conversion
        if(!account.getCurrency().getMark().equalsIgnoreCase(dto.getCurrencyMark())){
            Double convertedAmount = convertCurrency(dto.getCurrencyMark(), account.getCurrency().getMark(), dto.getAmount());
            if(account.getBalance()>=convertedAmount){
                return ResponseEntity.ok().build();
            }else{
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Not enough money on balance!");
            }
        }else{
            if(account.getBalance()>=dto.getAmount()){
                return ResponseEntity.ok().build();
            }else{
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Not enough money on balance!");
            }
        }
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
