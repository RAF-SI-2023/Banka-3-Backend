package com.example.bankservice.controller;

import com.example.bankservice.domain.dto.transaction.*;
import com.example.bankservice.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    private TransactionService transactionService;

    @PostMapping(value = "/startPaymentTransaction")
    public ResponseEntity<?> startTransaction(@RequestBody PaymentTransactionDto paymentTransactionDto) {
        try {
            StartPaymentTransactionDto startPaymentTransactionDto = transactionService.startPaymentTransaction(paymentTransactionDto);
            return ResponseEntity.ok(startPaymentTransactionDto);
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

    @GetMapping(value = "/getAllMarginTransactions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllMarginTransactions() {
        try {
            List<MarginTransactionDto> transactions = transactionService.getAllMarginTransactions();
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/getAllPaymentTransactions/{accountNumber}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllPaymentTransactions(@PathVariable String accountNumber) {
        try {
            List<FinishedPaymentTransactionDto> transactions = transactionService.getAllPaymentTransactions(accountNumber);
            return ResponseEntity.ok().body(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/stockBuyTransaction",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> stockBuyTransaction(@RequestBody StockTransactionDto stockTransactionDto) {
        try {
            transactionService.stockBuyTransaction(stockTransactionDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping(value = "/stockBuyMarginTransaction",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> stockBuyMarginTransaction(@RequestBody StockMarginTransactionDto stockTransactionDto) {
        try {
            transactionService.stockBuyMarginTransaction(stockTransactionDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping(value = "/addToMargin/{userId}",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> stockBuyMarginTransaction(@RequestBody AddToMarginDto dto,@PathVariable Long userId) {
        try {
            transactionService.addToMargin(dto,userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping(value = "/withdrawFromMargin/{userId}",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> stockBuyMarginTransaction(@RequestBody WithdrawFromMarginDto dto,@PathVariable Long userId) {
        try {
            transactionService.withdrawFromMargin(dto,userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @PostMapping(value = "/stockSellTransaction",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> stockSellTransaction(@RequestBody StockTransactionDto stockTransactionDto) {
        try {
            transactionService.stockSellTransaction(stockTransactionDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping(value = "/stockSellMarginTransaction",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> stockSellMarginTransaction(@RequestBody StockMarginTransactionDto dto) {
        try {
            transactionService.stockSellMarginTransaction(dto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    
    @PostMapping(value = "/otcUserTransaction",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> otcUserTransaction(
            @RequestBody UserOtcTransactionDto userOtcTransactionDto) {
        try {
            transactionService.otcUserTransaction(userOtcTransactionDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PostMapping(value = "/otcCompanyTransaction",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> otcCompanyTransaction(
            @RequestBody CompanyOtcTransactionDto companyOtcTransactionDto) {
        try {
            transactionService.otcCompanyTransaction(companyOtcTransactionDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/otcBank4transaction",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> otcBank4Transaction(@RequestBody CompanyOtcTransactionDto companyOtcTransactionDto) {
        try {
            transactionService.otcBank4Transaction(companyOtcTransactionDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
