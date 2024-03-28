package com.example.emailservice.controller;


import com.example.emailservice.dto.ConfirmTransactionDTO;
import com.example.emailservice.dto.TransactionActivationDTO;
import com.example.emailservice.service.impl.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
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
    public void beginTransaction(@RequestBody TransactionActivationDTO dto) {
        transactionService.beginTransaction(dto);
    }


    //Korisnik unosi kod za potvrdu transakcije, a ovaj endpoint proverava da li je kod validan.
    @PostMapping("/confirm")
    public ResponseEntity<String> validateTransaction(@RequestBody ConfirmTransactionDTO dto) {
        return transactionService.confirmTransaction(dto);
    }


}
