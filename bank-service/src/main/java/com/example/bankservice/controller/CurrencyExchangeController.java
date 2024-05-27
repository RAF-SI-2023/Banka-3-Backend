package com.example.bankservice.controller;

import com.example.bankservice.domain.dto.currencyExchange.CurrencyExchangeDto;
import com.example.bankservice.service.CurrencyExchangeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
}
