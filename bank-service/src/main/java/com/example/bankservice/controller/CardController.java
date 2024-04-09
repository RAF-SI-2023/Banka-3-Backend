package com.example.bankservice.controller;

import com.example.bankservice.domains.dto.CardDto;
import com.example.bankservice.domains.dto.CreateCardDto;
import com.example.bankservice.services.CardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/card")
public class CardController {

    private final CardService cardService;


    @GetMapping("/getAll/{userId}")
    public ResponseEntity<List<CardDto>> getAll(@PathVariable Long userId) {
        return cardService.findAllByUserId(userId);
    }
    @PutMapping("/deactivate/{cardId}")
    public ResponseEntity<String> deactivateCard(@PathVariable Long cardId) {
        return cardService.deactivateCard(cardId);
    }
    @PutMapping("/activate/{cardId}")
    public ResponseEntity<String> activateCard(@PathVariable Long cardId) {
        return cardService.activateCard(cardId);
    }
    @PostMapping("/create")
    public ResponseEntity<String> createCard(@RequestBody CreateCardDto createCardDto) {
        return cardService.createCard(createCardDto);
    }


}
