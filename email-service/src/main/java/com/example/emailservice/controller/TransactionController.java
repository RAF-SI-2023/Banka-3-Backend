package com.example.emailservice.controller;

import com.example.emailservice.domain.dto.ConfirmTransactionDto;
import com.example.emailservice.domain.dto.TransactionActivationDto;
import com.example.emailservice.domain.dto.bankService.TransactionFinishedDto;
import com.example.emailservice.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/transaction")
@CrossOrigin
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/begin")
    @Operation(description = "Korisniku se salje email sa kodom za potvrdu transakcije")
    public void beginTransaction(@RequestBody TransactionActivationDto dto) {
        transactionService.beginTransaction(dto);
    }


    @PostMapping("/confirm")
    @Operation(description = "Korisnik unosi kod za potvrdu transakcije, a ovaj endpoint proverava da li je kod validan")
    public ResponseEntity<String> validateTransaction(@RequestBody ConfirmTransactionDto dto) {
        try {
            transactionService.confirmTransaction(dto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/finished")
    @Operation(description = "Kada legnu pare na racun, stize obavestenje na mail")
    public ResponseEntity<Void> sendTransactionFinishedEmail(@RequestBody TransactionFinishedDto transactionFinishedDto) {
        transactionService.sendTransactionFinishedEmail(transactionFinishedDto);
        return ResponseEntity.ok().build();
    }
}
