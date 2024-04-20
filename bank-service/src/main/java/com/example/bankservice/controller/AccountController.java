package com.example.bankservice.controller;

import com.example.bankservice.domain.dto.account.AccountCreateDto;
import com.example.bankservice.domain.dto.account.AccountDto;
import com.example.bankservice.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("api/v2/account")
public class AccountController {

    private AccountService accountService;

    @GetMapping("/getAll")
    public List<AccountDto> getAll() {
        return accountService.findAll();
    }

    @GetMapping("/getByUser/{userId}")
    public List<AccountDto> getByUser(@PathVariable Long userId) {
        return accountService.findByUser(userId);
    }

    @PostMapping("/createAccount")
    public AccountDto createAccount(@RequestBody AccountCreateDto accountCreateDto) {
        return accountService.createAccount(accountCreateDto);
    }
}
