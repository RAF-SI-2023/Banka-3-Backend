package rs.edu.raf.exchangeservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.exchangeservice.domain.dto.buySell.BuyOptionDto;
import rs.edu.raf.exchangeservice.domain.dto.buySell.BuyStockCompanyDto;
import rs.edu.raf.exchangeservice.domain.dto.buySell.SellOptionDto;
import rs.edu.raf.exchangeservice.domain.model.listing.Option;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyOption;
import rs.edu.raf.exchangeservice.service.listingService.OptionService;
import rs.edu.raf.exchangeservice.service.myListingService.MyOptionService;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/option")
public class OptionController {
    private final OptionService optionService;
    private final MyOptionService myOptionService;

    @GetMapping("/calls/{ticker}")
    @Operation(description = "vracamo sve calls vrednosti za odredjeni ticker")
    public ResponseEntity<List<Option>> getAllCalls(@PathVariable String ticker){
        return ResponseEntity.ok(optionService.findCalls(ticker));
    }

    @GetMapping("/puts/{ticker}")
    @Operation(description = "vracamo sve puts vrednosti za odredjeni ticker")
    public ResponseEntity<List<Option>> getAllPutss(@PathVariable String ticker){
        return ResponseEntity.ok(optionService.findPuts(ticker));
    }

    @GetMapping("/refresh")
    @Operation(description = "kada korisnik zatrazi refresh podataka")
    public ResponseEntity<List<Option>> getAllRefreshed() throws JsonProcessingException {
        return ResponseEntity.ok(this.optionService.findAllRefreshed());
    }


    @PostMapping("/bankBuyOption")
    @Operation(description = "Banka kupuje odredjenu kolicinu opcija sa berze")
    public ResponseEntity buyOptionsFromExchange(@RequestBody BuyOptionDto buyOptionDto) {
        MyOption myOption = this.optionService.buyOptionsFromExchange(buyOptionDto);
        if(myOption == null)
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.ok(myOption);
    }

    @PostMapping("/bankSellOption")
    @Operation(description = "Banka prodaje odredjenu kolicnu opcija berzi")
    public ResponseEntity sellOptionsToExchange(@RequestBody SellOptionDto sellOptionDto) {
        this.myOptionService.sellOptionsToExchange(sellOptionDto);
        return ResponseEntity.ok().build();
    }
}


