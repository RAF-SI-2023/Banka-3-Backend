package com.example.bankservice.controller;

import com.example.bankservice.domain.dto.account.UserAccountCreateDto;
import com.example.bankservice.domain.dto.account.UserAccountDto;
import com.example.bankservice.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("api/v1/account")
public class AccountController {

    private AccountService accountService;

    @GetMapping("/getAll")
    public List<UserAccountDto> getAll() {
        return accountService.findAllUserAccounts();
    }

    @GetMapping("/getByUser/{userId}")
    public ResponseEntity<?> getByUser(@PathVariable Long userId) {
        try {
            List<UserAccountDto> userAccountDtos = accountService.findUserAccountByUser(userId);
            return ResponseEntity.ok(userAccountDtos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Unable to get accounts by user");
        }
    }

    @PostMapping("/createAccount")
    public ResponseEntity<?> createAccount(@RequestBody UserAccountCreateDto userAccountCreateDto) {
        try {
            UserAccountDto userAccountDto = accountService.createUserAccount(userAccountCreateDto);
            return ResponseEntity.ok(userAccountDto);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Unable to create account");
        }
    }

    @DeleteMapping("/deleteAccount/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
        try {
            accountService.deleteAccount(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Unable to delete account");
        }
    }
}
