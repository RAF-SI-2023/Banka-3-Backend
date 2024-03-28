package com.example.bankservice.controllers;

import com.example.bankservice.domains.dto.CreditTransactionDto;
import com.example.bankservice.domains.model.CreditTransaction;
import com.example.bankservice.services.CreditTransactionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
@RequestMapping("/api/v1/credit-transaction")
public class CreditTransactionController {

    @Autowired
    private CreditTransactionService creditTransactionService;

    @PostMapping()
    public ResponseEntity<?> createCreditTransactions(@RequestBody List<CreditTransactionDto> transactionCreditDtos) {
        creditTransactionService.createCreditTransactions(transactionCreditDtos);
        return ResponseEntity.ok().build();
    }
}
