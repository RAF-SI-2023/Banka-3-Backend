package com.example.emailservice.controller;

import com.example.emailservice.domain.dto.password.SetUserPasswordCodeDto;
import com.example.emailservice.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping(value = "/api/v1/company")
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping("/companyActivation")
    @Operation(description = "ovu rutu ganja User-Service kako bi poslao Code na email kompanije koja se prvi put loguje")
    public ResponseEntity<?> companyActivation(@RequestParam(name = "email") String email) {
        companyService.companyActivation(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/activateCompany")
    @Operation(description = "ovde nam stizu Code, password i email za Company kom treba podesiti sifru")
    public ResponseEntity<?> setCompanyPassword(@RequestBody SetUserPasswordCodeDto setUserPasswordCodeDTO) {
        Boolean companyActivated = companyService.setCompanyPassword(setUserPasswordCodeDTO);
        if (companyActivated) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
