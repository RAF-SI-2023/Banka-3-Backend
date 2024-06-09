package rs.edu.raf.exchangeservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.exchangeservice.domain.dto.buySell.BuyForexDto;
import rs.edu.raf.exchangeservice.domain.model.listing.Forex;
import rs.edu.raf.exchangeservice.service.listingService.ForexService;
import rs.edu.raf.exchangeservice.service.myListingService.MyForexService;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/forex")
public class ForexController {
    private final ForexService forexService;
    private final MyForexService myForexService;

    @GetMapping()
    @Operation(description = "svi forexi iz baze")
    public ResponseEntity<List<Forex>> getAll(){
        return ResponseEntity.ok(this.forexService.findAll());
    }

    @GetMapping("/refresh")
    @Operation(description = "kada korisnik zatrazi refresh podataka")
    public ResponseEntity<List<Forex>> getAllRefreshed() throws JsonProcessingException {
        return ResponseEntity.ok(this.forexService.findAllRefreshed());
    }

    @GetMapping("/myForex/getAllForCompany/{companyId}")
    @Operation(description = "vraca sve myForex-e za kompaniju")
    public ResponseEntity<?> getAllForCompany(@PathVariable Long companyId){
        return ResponseEntity.ok(this.myForexService.findAllMyForexByCompanyId(companyId));
    }

    @PostMapping("/myForex/buyForex")
    @Operation(description = "kupovina forexa")
    public ResponseEntity<?> buyForex(@RequestBody BuyForexDto buyForexDto){
        return ResponseEntity.ok(this.myForexService.buyForex(buyForexDto));
    }

    @GetMapping("/myForex/getAllForexOrders")
    @Operation(description = "vraca sve forex ordere")
    public ResponseEntity<?> getAllForexOrders(){
        return ResponseEntity.ok(this.myForexService.findAll());
    }
}
