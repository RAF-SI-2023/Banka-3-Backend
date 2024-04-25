package rs.edu.raf.exchangeservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.exchangeservice.domain.model.history.StockDaily;
import rs.edu.raf.exchangeservice.domain.model.history.StockIntraday;
import rs.edu.raf.exchangeservice.domain.model.history.StockMonthly;
import rs.edu.raf.exchangeservice.domain.model.history.StockWeekly;
import rs.edu.raf.exchangeservice.service.historyService.StockDailyService;
import rs.edu.raf.exchangeservice.service.historyService.StockIntradayService;
import rs.edu.raf.exchangeservice.service.historyService.StockMonthlyService;
import rs.edu.raf.exchangeservice.service.historyService.StockWeeklyService;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/history")
public class HistoryController {
    private final StockIntradayService stockIntradayService;
    private final StockDailyService stockDailyService;
    private final StockWeeklyService stockWeeklyService;
    private final StockMonthlyService stockMonthlyService;

    @GetMapping("/intraday/{ticker}")
    @Operation(description = "vracamo sve vrednosti po satima")
    public ResponseEntity<List<StockIntraday>> getAllIntraday(@PathVariable String ticker){
        return ResponseEntity.ok(stockIntradayService.findByTicker(ticker));
    }

    @GetMapping("/daily/{ticker}")
    @Operation(description = "vracamo sve vrednosti po danima")
    public ResponseEntity<List<StockDaily>> getAllDaily(@PathVariable String ticker){
        return ResponseEntity.ok(stockDailyService.findByTicker(ticker));
    }

    @GetMapping("/weekly/{ticker}")
    @Operation(description = "vracamo sve vrednosti po nedeljama")
    public ResponseEntity<List<StockWeekly>> getAllWeekly(@PathVariable String ticker){
        return ResponseEntity.ok(stockWeeklyService.findByTicker(ticker));
    }

    @GetMapping("/monthly/{ticker}")
    @Operation(description = "vracamo sve vrednosti po mesecima, do 5 godina unazad")
    public ResponseEntity<List<StockMonthly>> getAllMonthly(@PathVariable String ticker){
        return ResponseEntity.ok(stockMonthlyService.findByTicker(ticker));
    }
}
