package rs.edu.raf.exchangeservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.exchangeservice.domain.dto.buySell.BuyStockCompanyDto;
import rs.edu.raf.exchangeservice.domain.dto.buySell.BuyStockUserDto;
import rs.edu.raf.exchangeservice.domain.model.listing.Option;
import rs.edu.raf.exchangeservice.service.listingService.OptionService;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/option")
public class OptionController {
    private final OptionService optionService;

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

    //Firma kupuje od firme
    @PostMapping("/companyBuy")
    @Operation(description = "Firma salje zahtev drugoj firmi za kupovinu options")
    public ResponseEntity requestToBuyOptionByCompany(@RequestBody BuyStockCompanyDto buyStockCompanyDto){
        if(this.optionService.requestToBuyOptionByCompany(buyStockCompanyDto)){
            return ResponseEntity.ok().build();
        }else{
            //nema para
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

    }




}


