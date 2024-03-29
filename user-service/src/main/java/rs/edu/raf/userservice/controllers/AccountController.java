package rs.edu.raf.userservice.controllers;

import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.userservice.domains.dto.account.AccountCreateDto;
import rs.edu.raf.userservice.domains.dto.account.AccountDto;
import rs.edu.raf.userservice.domains.dto.account.CheckEnoughBalanceDto;
import rs.edu.raf.userservice.domains.dto.account.RebalanceAccountDto;
import rs.edu.raf.userservice.services.AccountService;

import java.util.List;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/getAll")
    public List<AccountDto> getAll() {
        return accountService.findAll();
    }

    @GetMapping("/getByUser/{userId}")
    public List<AccountDto> getByUser(@PathVariable Long userId) {
        return accountService.findByUser(userId);
    }

    @GetMapping("/getByAccountNumber/{accountNumber}")
    public AccountDto getByAccountNumber(@PathVariable String accountNumber) {
        return accountService.findByAccountNumber(accountNumber);
    }

    @GetMapping("/getEmailByAccountNumber/{accountNumber}")
    public String getEmailByAccountNumber(@PathVariable String accountNumber) {
        return accountService.getEmailByAccountNumber(accountNumber);
    }

    //TODO ubaciti u test
    @PostMapping("/checkEnoughBalance")
    public ResponseEntity<String> checkEnoughBalance(@RequestBody CheckEnoughBalanceDto checkEnoughBalanceDto) {
        return accountService.checkEnoughBalance(checkEnoughBalanceDto);
    }

    @PostMapping("/unreserveMoney")
    public ResponseEntity<String> unreserveMoney(@RequestBody RebalanceAccountDto dto) {
        return accountService.unreserveMoney(dto);
    }

    @PostMapping("/addMoneyToAccount")
    public ResponseEntity<String> addMoneyToAccount(@RequestBody RebalanceAccountDto dto) {
        return accountService.addMoneyToAccount(dto);
    }

    @PostMapping("/takeMoneyFromAccount")
    public ResponseEntity<String> takeMoneyFromAccount(@RequestBody RebalanceAccountDto dto) {
        return accountService.takeMoneyFromAccount(dto);
    }

    @PostMapping("/reserveMoney")
    public ResponseEntity<String> reserveMoney(@RequestBody RebalanceAccountDto dto) {
        return accountService.reserveMoney(dto);
    }


    //preauthorize sa employee
    @PostMapping("/createAccount")
    public AccountDto addAccount(@RequestBody AccountCreateDto accountCreateDto) {
        return accountService.create(accountCreateDto);
    }

    @DeleteMapping("/{accountId}")
    public void deleteAccount(@PathVariable Long accountId) {
        accountService.deactivate(accountId);
    }

}
