package rs.edu.raf.exchangeservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.edu.raf.exchangeservice.domain.model.listing.Forex;
import rs.edu.raf.exchangeservice.service.listingService.ForexService;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/forex")
public class ForexController {
    private final ForexService forexService;

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
}
