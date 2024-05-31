package rs.edu.raf.exchangeservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.edu.raf.exchangeservice.domain.model.TaxStock;
import rs.edu.raf.exchangeservice.service.TaxService;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/tax")
public class TaxController {
    private final TaxService taxService;

    @GetMapping()
    @Operation(description = "svi taxovi iz baze")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(this.taxService.findAll());
    }
}
