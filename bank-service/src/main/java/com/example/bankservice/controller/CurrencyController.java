package com.example.bankservice.controller;

import com.example.bankservice.domain.model.Currency;
import com.example.bankservice.service.CurrencyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/api/v1/currency")
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping("/getAll")
    public List<Currency> findAllCurrencies() {
        return currencyService.findAll();
    }

    @GetMapping("/findByMark")
    public ResponseEntity<?> findByMark(@RequestParam String currencyMark) {
        try {
            Currency currency = currencyService.findByMark(currencyMark);
            return ResponseEntity.ok(currency);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Currency not found");
        }

    }
}
