package com.example.bankservice.controller;

import com.example.bankservice.domains.dto.CreditTransactionDto;
import com.example.bankservice.services.CreditTransactionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@AllArgsConstructor
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/creditTransaction")
public class CreditTransactionController {

    @Autowired
    private CreditTransactionService creditTransactionService;

    @PostMapping()
    public ResponseEntity<?> createCreditTransactions(@RequestBody List<CreditTransactionDto> transactionCreditDtos) {
        creditTransactionService.createCreditTransactions(transactionCreditDtos);
        return ResponseEntity.ok().build();
    }
}
