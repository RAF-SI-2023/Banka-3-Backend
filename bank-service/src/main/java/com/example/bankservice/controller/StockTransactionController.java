package com.example.bankservice.controller;

import com.example.bankservice.domains.dto.StockTransactionDto;
import com.example.bankservice.services.StockTransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/stockTransaction")
public class StockTransactionController {

    private StockTransactionService stockTransactionService;

    @PostMapping(value = "/start")
    ResponseEntity<?> startStockTransaction (@RequestBody StockTransactionDto stockTransactionDto) {
        return stockTransactionService.startStockTransaction(stockTransactionDto);
    }
}
