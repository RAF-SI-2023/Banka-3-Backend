package rs.edu.raf.userservice.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.userservice.domains.dto.account.AccountCreateDto;
import rs.edu.raf.userservice.domains.dto.account.AccountDto;
import rs.edu.raf.userservice.domains.dto.account.CheckEnoughBalanceDto;
import rs.edu.raf.userservice.domains.dto.account.RebalanceAccountDto;
import rs.edu.raf.userservice.services.AccountService;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/getAll")
    public List<AccountDto> getAll() {
        return accountService.findAll();
    }

    @GetMapping("/getByUser/{userId}")
    public List<AccountDto> getByUser(Long userId) {
        return accountService.findByUser(userId);
    }

    @GetMapping("/getByAccountNumber/{accountNumber}")
    public AccountDto getByAccountNumber(String accountNumber) {
        return accountService.findByAccountNumber(accountNumber);
    }

    @GetMapping("/getEmailByAccountNumber/{accountNumber}")
    public String getEmailByAccountNumber(String accountNumber) {
        return accountService.getEmailByAccountNumber(accountNumber);
    }

    //TODO ubaciti u test
    @GetMapping("/checkEnoughBalance")
    public ResponseEntity<String> checkEnoughBalance(@RequestBody CheckEnoughBalanceDto checkEnoughBalanceDto) {
        return accountService.checkEnoughBalance(checkEnoughBalanceDto);
    }

    @GetMapping("/unreserveMoney")
    public ResponseEntity<String> unreserveMoney(@RequestBody RebalanceAccountDto dto) {
        return accountService.unreserveMoney(dto);
    }

    @GetMapping("/addMoneyToAccount")
    public ResponseEntity<String> addMoneyToAccount(@RequestBody RebalanceAccountDto dto) {
        return accountService.addMoneyToAccount(dto);
    }

    @GetMapping("/takeMoneyFromAccount")
    public ResponseEntity<String> takeMoneyFromAccount(@RequestBody RebalanceAccountDto dto) {
        return accountService.takeMoneyFromAccount(dto);
    }

    @GetMapping("/reserveMoney")
    public ResponseEntity<String> reserveMoney(@RequestBody RebalanceAccountDto dto) {
        return accountService.reserveMoney(dto);
    }


    //preauthorize sa employee
    @PostMapping("/{userId}")
    public AccountDto addUser(@PathVariable Long userId, @RequestBody AccountCreateDto accountCreateDto) {
        return accountService.create(accountCreateDto, userId);
    }

    @DeleteMapping("/{accountId}")
    public void deleteAccount(@PathVariable Long accountId) {
        accountService.deactivate(accountId);
    }


}
