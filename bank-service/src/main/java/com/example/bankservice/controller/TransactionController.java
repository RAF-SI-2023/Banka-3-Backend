package com.example.bankservice.controller;


import com.example.bankservice.domains.dto.ConfirmTransactionDto;
import com.example.bankservice.domains.dto.TransactionDto;
import com.example.bankservice.services.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1/transaction")
@AllArgsConstructor
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    //TODO preimenovati posle rute po potrebi,nisam imao nigde predefinisano kako to treba da izgleda

    /**
     * Ukoliko korisnik ima dovoljno sredstava na racunu, bank servis ce pokrenuti transakciju
     * Kontaktirace email servis za slanje mejla korisniku sa kodom za potvrdu transakcije
     * Ukoliko korisnik potvrdi transakciju,email servis gadja rutu /confirmTransaction
     */
    @PostMapping(value = "/startTransaction")
    public ResponseEntity<Long> startTransaction(@RequestBody TransactionDto dto) {
        return transactionService.startTransaction(dto);
    }

    @PostMapping(value = "/confirmTransaction")
    public ResponseEntity<String> confirmTransaction(@RequestBody ConfirmTransactionDto confirmTransactionDto) {
        return transactionService.confirmTransaction(confirmTransactionDto);
    }

    @GetMapping(value = "/getAllTransactions/{accountId}")
    public ResponseEntity<?> getAllTransactions(@PathVariable String accountId) {
        return ResponseEntity.ok(transactionService.getAllTransactions(accountId));
    }
}
