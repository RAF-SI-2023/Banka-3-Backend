package com.example.bankservice.controller;

import com.example.bankservice.domain.dto.companyaccount.CompanyAccountCreateDto;
import com.example.bankservice.domain.dto.companyaccount.CompanyAccountDto;
import com.example.bankservice.service.CompanyAccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/api/v2/companyAccount")
public class CompanyAccountController {

    private CompanyAccountService companyAccountService;

    @GetMapping("/getAll")
    public List<CompanyAccountDto> getAll() {
        return companyAccountService.findAll();
    }

    @GetMapping("/getByCompany/{companyId}")
    public List<CompanyAccountDto> getByCompanyId(@PathVariable Long companyId) {
        return companyAccountService.findByCompanyId(companyId);
    }

    @PostMapping("/createAccount")
    public ResponseEntity<?> createAccount(@RequestBody CompanyAccountCreateDto companyAccountCreateDto) {
        try {
            CompanyAccountDto companyAccountDto = companyAccountService.createCompanyAccount(companyAccountCreateDto);
            return ResponseEntity.ok(companyAccountDto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Unable to create account");
        }
    }

    @DeleteMapping("/deleteAccount/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
        try {
            companyAccountService.deleteCompanyAccount(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Unable to delete account");
        }
    }
}
