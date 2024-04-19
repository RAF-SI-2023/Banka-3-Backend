package rs.edu.raf.userservice.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rs.edu.raf.userservice.domains.dto.account.CheckEnoughBalanceDto;
import rs.edu.raf.userservice.domains.dto.account.RebalanceAccountDto;
import rs.edu.raf.userservice.domains.dto.companyaccount.CompanyAccountCreateDto;
import rs.edu.raf.userservice.domains.dto.companyaccount.CompanyAccountDto;
import rs.edu.raf.userservice.domains.mappers.CompanyAccountMapper;
import rs.edu.raf.userservice.domains.model.Account;
import rs.edu.raf.userservice.domains.model.CompanyAccount;
import rs.edu.raf.userservice.domains.model.Currency;
import rs.edu.raf.userservice.domains.model.enums.CurrencyName;
import rs.edu.raf.userservice.repositories.CompanyAccountRepository;
import rs.edu.raf.userservice.repositories.CompanyRepository;
import rs.edu.raf.userservice.repositories.CurrencyRepository;
import rs.edu.raf.userservice.repositories.EmployeeRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class CompanyAccountService {

    private final CompanyAccountRepository companyAccountRepository;
    private final CurrencyRepository currencyRepository;
    private final CompanyRepository companyRepository;

    private final EmployeeRepository employeeRepository;

    public CompanyAccountService(CompanyAccountRepository companyAccountRepository,
                                 EmployeeRepository employeeRepository,
                                 CurrencyRepository currencyRepository, CompanyRepository companyRepository) {
        this.companyAccountRepository = companyAccountRepository;
        this.employeeRepository = employeeRepository;
        this.currencyRepository = currencyRepository;
        this.companyRepository = companyRepository;
    }


    public CompanyAccountDto create(CompanyAccountCreateDto companyAccountCreateDto, Long companyId) {
        CurrencyName currencyName = CurrencyName.valueOf(companyAccountCreateDto.getCurrency());
        Currency c = currencyRepository.findByName(currencyName).orElseThrow();

        CompanyAccount companyAccount =
                CompanyAccountMapper.INSTANCE.companyAccountCreateDtoToCompanyAccount(companyAccountCreateDto);
        companyAccount.setCompany(companyRepository.findById(companyId).orElse(null));
        companyAccount.setEmployee(employeeRepository.findById(companyAccountCreateDto.getEmployeeId()).orElse(null));
        companyAccount.setCurrency(c);
        companyAccount.setAccountNumber(randAccNumber(c.getMark().equals("RSD")));
        companyAccount.setCreationDate(System.currentTimeMillis());
        companyAccount.setExpireDate(System.currentTimeMillis() + 31556952000L);
        companyAccount.setActive(true);
        companyAccount.setBalance(companyAccountCreateDto.getBalance());
        companyAccount.setAvailableBalance(companyAccountCreateDto.getBalance());
        companyAccountRepository.save(companyAccount);
        return CompanyAccountMapper.INSTANCE.companyAccountToCompanyAccountDto(companyAccount);
    }

    private String randAccNumber(Boolean rsd) { //generise broj racuna
        String fixedPart = rsd ? "5053791" : "5054791";
        StringBuilder builder = new StringBuilder(fixedPart);
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int randomNumber = random.nextInt(10); // Generate a random number between 0-9
            builder.append(randomNumber);
        }
        return builder.toString();
    }

    public CompanyAccountDto deactivate(Long id) {
        CompanyAccount companyAccount = companyAccountRepository.findById(id).orElse(null);
        if (companyAccount != null) {
            companyAccount.setActive(false);
            companyAccountRepository.save(companyAccount);
            return CompanyAccountMapper.INSTANCE.companyAccountToCompanyAccountDto(companyAccount);
        }
        return null;
    }

    public List<CompanyAccountDto> findAll() {
        return companyAccountRepository.findAll().stream().map(CompanyAccountMapper.INSTANCE::companyAccountToCompanyAccountDto)
                .collect(Collectors.toList());

    }

    public List<CompanyAccountDto> findByCompany(Long companyId) {
        List<CompanyAccount> accounts = companyAccountRepository.findByCompany_CompanyId(companyId).orElseThrow();
        return accounts.stream().map(CompanyAccountMapper.INSTANCE::companyAccountToCompanyAccountDto)
                .collect(Collectors.toList());
    }

    public CompanyAccountDto findByAccountNumber(String accountNumber) {
        CompanyAccount companyAccount = companyAccountRepository.findByAccountNumber(accountNumber);
        return CompanyAccountMapper.INSTANCE.companyAccountToCompanyAccountDto(companyAccount);
    }

    public ResponseEntity<String> checkCompanyBalance(CheckEnoughBalanceDto dto) {

        CompanyAccount companyAccount = companyAccountRepository.findByAccountNumber(dto.getAccountNumber());
        if(companyAccount == null)
            return ResponseEntity.badRequest().build();

        if (companyAccount.getAvailableBalance().subtract(companyAccount.getReservedAmount()).compareTo(BigDecimal.valueOf(dto.getAmount())) >= 0) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Insufficient funds!");
        }

    }

    public ResponseEntity<String> reserveCompanyMoney(RebalanceAccountDto dto) {

        CompanyAccount companyAccount = companyAccountRepository.findByAccountNumber(dto.getAccountNumber());
        if(companyAccount == null)
            return ResponseEntity.badRequest().build();

        if (companyAccount.getAvailableBalance().subtract(companyAccount.getReservedAmount()).compareTo(BigDecimal.valueOf(dto.getAmount())) < 0)
            return ResponseEntity.badRequest().build();

        companyAccount.setReservedAmount(companyAccount.getReservedAmount().add(BigDecimal.valueOf(dto.getAmount())));
        companyAccountRepository.save(companyAccount);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<String> unreserveCompanyMoney(RebalanceAccountDto dto) {

        CompanyAccount companyAccount = companyAccountRepository.findByAccountNumber(dto.getAccountNumber());
        if (companyAccount == null)
            return ResponseEntity.badRequest().build();

        if (companyAccount.getReservedAmount().compareTo(BigDecimal.valueOf(dto.getAmount())) < 0)
            return ResponseEntity.badRequest().build();
        companyAccount.setReservedAmount(companyAccount.getReservedAmount().subtract(BigDecimal.valueOf(dto.getAmount())));
        companyAccountRepository.save(companyAccount);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<String> addMoneyToCompanyAccount(RebalanceAccountDto dto) {

        CompanyAccount companyAccount = companyAccountRepository.findByAccountNumber(dto.getAccountNumber());
        if (companyAccount == null)
            return ResponseEntity.badRequest().build();

        companyAccount.setAvailableBalance(companyAccount.getAvailableBalance().add(BigDecimal.valueOf(dto.getAmount())));
        companyAccountRepository.save(companyAccount);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<String> takeMoneyFromCompanyAccount(RebalanceAccountDto dto) {

        CompanyAccount companyAccount = companyAccountRepository.findByAccountNumber(dto.getAccountNumber());
        if (companyAccount == null)
            return ResponseEntity.badRequest().build();

        companyAccount.setAvailableBalance(companyAccount.getAvailableBalance().subtract(BigDecimal.valueOf(dto.getAmount())));
        companyAccountRepository.save(companyAccount);
        return ResponseEntity.ok().build();
    }

}
