package com.example.bankservice.controller;

import com.example.bankservice.domain.dto.creditRequest.CreditRequestCreateDto;
import com.example.bankservice.domain.dto.creditRequest.CreditRequestDto;
import com.example.bankservice.domain.dto.creditRequest.ProcessCreditRequestDto;
import com.example.bankservice.service.CreditRequestService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/credit-request")
public class CreditRequestController {

    private CreditRequestService creditRequestService;

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCreditById(@PathVariable Long id) {
        try {
            CreditRequestDto creditRequestDto = creditRequestService.findById(id);
            return ResponseEntity.ok(creditRequestDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllCreditRequests() {
        try {
            return ResponseEntity.ok(creditRequestService.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCreditRequest(@RequestBody CreditRequestCreateDto creditRequestCreateDto) {
        try {
            creditRequestService.createCreditRequest(creditRequestCreateDto);
            return ResponseEntity.ok("Credit request created");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> processCreditRequest(@RequestBody ProcessCreditRequestDto processCreditRequestDto) {
        try {
            creditRequestService.processCreditRequest(processCreditRequestDto);
            return ResponseEntity.ok("Credit request processed");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
