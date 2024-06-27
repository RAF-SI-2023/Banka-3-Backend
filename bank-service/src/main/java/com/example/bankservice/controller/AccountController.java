package com.example.bankservice.controller;

import com.example.bankservice.domain.dto.account.*;

import com.example.bankservice.service.AccountService;
import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("api/v1/account")
@Timed
public class AccountController {

    private AccountService accountService;

    @GetMapping("/getAll")
    @Timed("controller.account.getAll")
    public List<UserAccountDto> getAll() {
        return accountService.findAllUserAccounts();
    }

    @GetMapping("/getByUser/{userId}")
    @Timed("controller.account.getByUser")
    public ResponseEntity<?> getByUser(@PathVariable Long userId) {
        try {
            List<UserAccountDto> userAccountDtos = accountService.findUserAccountByUser(userId);
            return ResponseEntity.ok(userAccountDtos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Unable to get accounts by user");
        }
    }
    @GetMapping("/getMarginByUser/{userId}")
    public ResponseEntity<?> getMarginByUser(@PathVariable Long userId) {
        try {
            UserMarginAccountDto userAccountDtos = accountService.findUserMarginAccountByUser(userId);
            return ResponseEntity.ok(userAccountDtos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Unable to get accounts by user");
        }
    }



    @PostMapping("/createAccount")
    @Timed("controller.users.account.createAccount")
    public ResponseEntity<?> createAccount(@RequestBody UserAccountCreateDto userAccountCreateDto) {
        try {
            UserAccountDto userAccountDto = accountService.createUserAccount(userAccountCreateDto);
            return ResponseEntity.ok(userAccountDto);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Unable to create account");
        }
    }
    @PostMapping("/createMarginAccount")
    public ResponseEntity<?> createMarginAccount(@RequestBody UserMarginAccountCreateDto dto) {
        try {
            UserMarginAccountDto userMarginAccountDto = accountService.createMarginAccount(dto);
            return ResponseEntity.ok(userMarginAccountDto);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Unable to create margin account");
        }

    }

    @DeleteMapping("/deleteAccount/{id}")
    @Timed("controller.users.account.deleteAccount")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
        try {
            accountService.deleteAccount(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Unable to delete account");
        }
    }
    
    @PostMapping("/checkAccountBalanceUser")
    @Timed("controller.users.account.checkAccountBalanceUser")
    public ResponseEntity<?> checkAccountBalanceUser(
            @RequestBody CheckAccountBalanceDto checkAccountBalanceDto) {
        try {
            return ResponseEntity.ok(
                    accountService.checkBalanceUser(checkAccountBalanceDto.getId(),
                            checkAccountBalanceDto.getAmount()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Unable to check account balance");
        }
    }
    
    @PostMapping("/checkAccountBalanceCompany")
    @Timed("controller.users.account.checkAccountBalanceCompany")
    public ResponseEntity<?> checkAccountBalanceCompany(
            @RequestBody CheckAccountBalanceDto checkAccountBalanceDto) {
        try {
            return ResponseEntity.ok(
                    accountService.checkBalanceCompany(checkAccountBalanceDto.getId(),
                            checkAccountBalanceDto.getAmount()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Unable to check account balance");
        }
    }



}
