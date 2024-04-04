package rs.edu.raf.exchangeservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.exchangeservice.domain.dto.BuyStockDto;
import rs.edu.raf.exchangeservice.domain.dto.SellStockDto;
import rs.edu.raf.exchangeservice.domain.model.Stock;
import rs.edu.raf.exchangeservice.service.StockService;
import rs.edu.raf.exchangeservice.service.myListingsService.MyStockService;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/stock")
public class StockController {
    private final StockService stockService;
    private final MyStockService myStockService;

    @GetMapping
    public ResponseEntity<List<Stock>> getAll(){
        return ResponseEntity.ok(stockService.findAll());
    }

    @GetMapping("/refresh")
    public ResponseEntity<List<Stock>> getAllRefreshed() throws JsonProcessingException {
        return ResponseEntity.ok(this.stockService.findAllRefreshed());
    }

    @PostMapping("/buyStock")
    public ResponseEntity buyStock(@RequestBody BuyStockDto buyStockDto){
        System.out.println(this.myStockService.buyStock(buyStockDto));
        return null;
    }

    @PostMapping("/sellStock")
    public ResponseEntity sellStock(@RequestBody SellStockDto sellStockDto){
        sellStockDto.toString();
        System.out.println(this.myStockService.sellStock(sellStockDto));
        return null;
    }

}
