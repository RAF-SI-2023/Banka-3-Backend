package rs.edu.raf.exchangeservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.exchangeservice.domain.dto.offer.MyStockDto;
import rs.edu.raf.exchangeservice.domain.dto.offer.OfferDto;
import rs.edu.raf.exchangeservice.domain.model.offer.Offer;
import rs.edu.raf.exchangeservice.service.Banka4OtcService;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/otcTrade")
public class Banka4OtcController {

    private final Banka4OtcService banka4OtcService;

    //radi, testirano
    @GetMapping("/getStocks")
    @Operation(description = "dohvata stockove nase banke")
    public ResponseEntity<List<MyStockDto>> getMyStocks(){
        return ResponseEntity.ok(this.banka4OtcService.findAllStocks());
    }

    //radi, testirano
    @PostMapping("/sendOffer")
    @Operation(description = "primamo ponude od banke 4")
    public ResponseEntity<OfferDto> receiveOffer(@RequestBody OfferDto dto){
        this.banka4OtcService.receiveOffer(dto);
        return ResponseEntity.ok().build();

    }

    //radi, testirano
    @GetMapping("/getOffers")
    @Operation(description = "dohvata sve ponude koje smo dobili")
    public ResponseEntity<List<Offer>> getOffers(){
        return ResponseEntity.ok(this.banka4OtcService.findAllOffers());
    }


    @GetMapping("/getBank4Stocks")
    @Operation(description = "dohvata stockove od banke 4")
    public ResponseEntity<?> getBank4Stocks(){
        this.banka4OtcService.getBank4Stocks();
        return ResponseEntity.ok().build();
    }

}
