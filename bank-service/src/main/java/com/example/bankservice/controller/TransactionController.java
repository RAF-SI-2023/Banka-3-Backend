package com.example.bankservice.controller;

import com.example.bankservice.domain.dto.transaction.ConfirmPaymentTransactionDto;
import com.example.bankservice.domain.dto.transaction.PaymentTransactionDto;
import com.example.bankservice.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/api/v2/transaction")
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
//        try {
//            return ResponseEntity.ok(transactionService.confirmPaymentTransaction(confirmPaymentTransactionDto));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//        }
        return null;
    }

    @GetMapping(value = "/getAllPaymentTransactions/{accountId}")
    public ResponseEntity<?> getAllPaymentTransactions(@PathVariable String accountId) {
//        try {
//            return ResponseEntity.ok(transactionService.getAllPaymentTransactions(accountId));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build("Unable to get all transactions for accountId");
//        }
        return null;
    }
}
