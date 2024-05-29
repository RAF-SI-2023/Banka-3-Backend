package rs.edu.raf.exchangeservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.exchangeservice.domain.dto.offer.OfferDto;
import rs.edu.raf.exchangeservice.service.Banka4OtcService;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/otcTrade")
public class Banka4OtcController {

    private final Banka4OtcService banka4OtcService;

    @GetMapping("/getOffers")
    @Operation(description = "dohvata stockove nase banke")
    public ResponseEntity<?> getOffers(){
        return ResponseEntity.ok(banka4OtcService.findAllOffers());
    }
    @GetMapping("/getStocks")
    @Operation(description = "dohvata stockove nase banke")
    public ResponseEntity<?> getMyStocks(){
        return ResponseEntity.ok(banka4OtcService.findAllStocks());
    }

    @GetMapping("/getBank4Stocks")
    @Operation(description = "dohvata stockove od banke 4")
    public ResponseEntity<?> getBank4Stocks(){
        banka4OtcService.getBank4Stocks();
        return ResponseEntity.ok().build();
    }


    @PostMapping("/sendOffer")
    @Operation(description = "primamo ponudu od banke 4")
    public ResponseEntity<?> receiveOffer(@RequestBody OfferDto dto){
        banka4OtcService.receiveOffer(dto);
        return ResponseEntity.ok().build();

    }

}
