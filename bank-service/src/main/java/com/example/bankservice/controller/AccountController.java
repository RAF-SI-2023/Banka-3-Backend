package com.example.bankservice.controller;

import com.example.bankservice.domain.dto.account.AccountDto;
import com.example.bankservice.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("api/v2/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/getAll")
    public List<AccountDto> getAll() {
        return accountService.findAll();
    }
}
