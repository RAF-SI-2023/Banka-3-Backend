package com.example.emailservice.controller;


import com.example.emailservice.dto.TransactionActivationDTO;
import com.example.emailservice.dto.ConfirmTransactionDTO;
import com.example.emailservice.service.impl.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    //Korisniku se salje email sa kodom za potvrdu transakcije
    @GetMapping("/begin")
    public void beginTransaction(@RequestBody TransactionActivationDTO dto){
         transactionService.beginTransaction(dto);
    }


    //Korisnik unosi kod za potvrdu transakcije, a ovaj endpoint proverava da li je kod validan.
    @GetMapping("/confirm")
    public ResponseEntity<String> validateTransaction(@RequestBody ConfirmTransactionDTO dto){
        return transactionService.confirmTransaction(dto);
    }




}
