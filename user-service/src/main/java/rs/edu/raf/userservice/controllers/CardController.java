package rs.edu.raf.userservice.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.userservice.domains.dto.card.CardDto;
import rs.edu.raf.userservice.domains.dto.card.CardResponseDto;
import rs.edu.raf.userservice.domains.dto.card.CreateCardDto;
import rs.edu.raf.userservice.services.CardService;

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

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody String accountNumber, Long amount) {
        return cardService.withdraw(accountNumber, amount);
    }
    @PostMapping("/cardLogin")
    public ResponseEntity<CardResponseDto> cardLogin(@RequestBody Long userId, String accNumber, String cvv){
        return cardService.cardLogin(userId, accNumber, cvv);
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody String accountNumber, Long amount) {
        return cardService.deposit(accountNumber, amount);
    }


}
