package com.example.bankservice.controller;

import com.example.bankservice.domain.dto.transaction.ConfirmPaymentTransactionDto;
import com.example.bankservice.domain.dto.transaction.CreditTransactionDto;
import com.example.bankservice.domain.dto.transaction.PaymentTransactionDto;
import com.example.bankservice.domain.dto.transaction.StockTransactionDto;
import com.example.bankservice.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    private TransactionService transactionService;

    @PostMapping(value = "/startPaymentTransaction")
    public ResponseEntity<?> startTransaction(@RequestBody PaymentTransactionDto paymentTransactionDto) {
        try {
            transactionService.startPaymentTransaction(paymentTransactionDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/confirmPaymentTransaction")
    public ResponseEntity<String> confirmTransaction(@RequestBody ConfirmPaymentTransactionDto confirmPaymentTransactionDto) {
        try {
            transactionService.confirmPaymentTransaction(confirmPaymentTransactionDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/getAllCreditTransactions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllCreditTransactions() {
        try {
            List<CreditTransactionDto> transactions = transactionService.getAllCreditTransactions();
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/getAllPaymentTransactions/{accountId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllPaymentTransactions(@PathVariable String accountId) {
        try {
            List<PaymentTransactionDto> transactions = transactionService.getAllPaymentTransactions();
            return ResponseEntity.ok().body(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/stockTransaction",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> stockTransaction(@RequestBody StockTransactionDto stockTransactionDto) {
        try {
            transactionService.stockBuyTransaction(stockTransactionDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
