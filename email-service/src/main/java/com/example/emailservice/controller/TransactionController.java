package com.example.emailservice.controller;


import com.example.emailservice.dto.ConfirmTransactionDto;
import com.example.emailservice.dto.TransactionActivationDto;
import com.example.emailservice.dto.bankService.TransactionFinishedDto;
import com.example.emailservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/transaction")
@CrossOrigin
@RequiredArgsConstructor
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    //Korisniku se salje email sa kodom za potvrdu transakcije
    @PostMapping("/begin")
    public void beginTransaction(@RequestBody TransactionActivationDto dto) {
        transactionService.beginTransaction(dto);
    }


    //Korisnik unosi kod za potvrdu transakcije, a ovaj endpoint proverava da li je kod validan.
    @PostMapping("/confirm")
    public ResponseEntity<String> validateTransaction(@RequestBody ConfirmTransactionDto dto) {
        try {
            transactionService.confirmTransaction(dto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/finished")
    public ResponseEntity<Void> sendTransactionFinishedEmail(@RequestBody TransactionFinishedDto transactionFinishedDto) {
        transactionService.sendTransactionFinishedEmail(transactionFinishedDto);
        return ResponseEntity.ok().build();
    }
}
