package rs.edu.raf.exchangeservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.edu.raf.exchangeservice.domain.model.listing.Future;
import rs.edu.raf.exchangeservice.service.listingService.FutureService;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/future")
public class FutureController {
    private final FutureService futureService;

    @GetMapping
    @Operation(description = "vracamo sve Future iz baze")
    public ResponseEntity<List<Future>> getAll(){
        return ResponseEntity.ok(futureService.findAll());
    }
}
