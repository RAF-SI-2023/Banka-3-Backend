package com.example.bankservice.controller;

import com.example.bankservice.domain.dto.currencyExchange.CurrencyExchangeDto;
import com.example.bankservice.service.CurrencyExchangeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/currencyExchange")
public class CurrencyExchangeController {

    private CurrencyExchangeService currencyExchangeService;

    @PostMapping()
    public ResponseEntity<?> currencyExchangeTransaction(@RequestBody CurrencyExchangeDto currencyExchangeDto) {
        try {
            currencyExchangeService.startCurrencyExchangeTransaction(currencyExchangeDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping
    public ResponseEntity<?> getMoneyMadeOnExchangeTransactions() {
        try {
            return ResponseEntity.ok(currencyExchangeService.getMoneyMadeOnExchangeTransactions());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
