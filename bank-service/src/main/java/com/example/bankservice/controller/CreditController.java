package com.example.bankservice.controller;

import com.example.bankservice.domain.dto.credit.CreditDto;
import com.example.bankservice.service.CreditService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/credit")
public class CreditController {

    private final CreditService creditService;

    @GetMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAllCreditsByUserId(@PathVariable Long userId) {
        try {
            List<CreditDto> creditDtos = creditService.findAllCreditsByUserId(userId);
            return ResponseEntity.ok(creditDtos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll() {
        try {
            List<CreditDto> creditDtos = creditService.findAll();
            return ResponseEntity.ok(creditDtos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
