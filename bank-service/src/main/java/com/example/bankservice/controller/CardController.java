package com.example.bankservice.controller;

import com.example.bankservice.domain.dto.card.*;
import com.example.bankservice.service.CardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/card")
public class CardController {

    private final CardService cardService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        try {
            List<CardDto> cards = cardService.findAll();
            return ResponseEntity.ok(cards);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Unable to get all cards");
        }
    }

    @GetMapping("/getAllByUser/{userId}")
    public ResponseEntity<?> getAllByUser(@PathVariable Long userId) {
        try {
            List<CardDto> cards = cardService.findAllByUserId(userId);
            return ResponseEntity.ok(cards);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getAllByCompany/{companyId}")
    public ResponseEntity<?> getAllByCompany(@PathVariable Long companyId) {
        try {
            List<CardDto> cards = cardService.findAllByCompanyId(companyId);
            return ResponseEntity.ok(cards);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/cardLogin")
    public ResponseEntity<?> cardLogin(@RequestBody CardLoginDto cardLoginDto) {
        try {
            CardResponseDto cardResponseDto = cardService.cardLogin(cardLoginDto);
            return ResponseEntity.ok(cardResponseDto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody WithdrawFundsDto withdrawFundsDto) {
        try {
            cardService.withdraw(withdrawFundsDto);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody DepositFundsDto depositFundsDto) {
        try {
            cardService.deposit(depositFundsDto);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getByAccount/{accountNumber}")
    public ResponseEntity<?> getAllByAccount(@PathVariable String accountNumber) {
        try {
            List<CardDto> cards = cardService.findAllByAccountNumber(accountNumber);
            return ResponseEntity.ok(cards);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Unable to get cards by account number");
        }
    }

    @PutMapping("/deactivate/{cardId}")
    public ResponseEntity<?> deactivateCard(@PathVariable Long cardId) {
        try {
            cardService.deactivateCard(cardId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Unable to deactivate card");

        }
    }
    @PutMapping("/activate/{cardId}")
    public ResponseEntity<?> activateCard(@PathVariable Long cardId) {
        try {
            cardService.activateCard(cardId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Unable to activate card");

        }
    }
}
