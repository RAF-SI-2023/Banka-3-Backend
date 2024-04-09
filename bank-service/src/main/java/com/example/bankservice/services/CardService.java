package com.example.bankservice.services;

import com.example.bankservice.domains.dto.CardDto;
import com.example.bankservice.domains.dto.CreateCardDto;
import com.example.bankservice.domains.mappers.CardMapper;
import com.example.bankservice.domains.model.Card;
import com.example.bankservice.repositories.CardRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    public ResponseEntity<List<CardDto>> findAllByUserId(Long userId) {
        Optional<List<Card>> optionalCards = cardRepository.findAllByUserId(userId);

        if(optionalCards.isPresent()) {
            List<Card>cards = optionalCards.get();
            return ResponseEntity.ok(cards.stream().map(CardMapper.INSTANCE::cardToCardDto).collect(Collectors.toList()));
        }
        return ResponseEntity.notFound().build();

    }
    public ResponseEntity<String> deactivateCard(Long cardId) {
        Optional<Card> optionalCard = cardRepository.findById(cardId);
        if(optionalCard.isPresent()) {
            Card card = optionalCard.get();
            card.setStatus(false);
            cardRepository.save(card);
            return ResponseEntity.ok("Card deactivated");
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<String> activateCard(Long cardId) {
        Optional<Card> optionalCard = cardRepository.findById(cardId);
        if(optionalCard.isPresent()) {
            Card card = optionalCard.get();
            card.setStatus(true);
            cardRepository.save(card);
            return ResponseEntity.ok("Card activated");
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<String>createCard(CreateCardDto createCardDto) {

        Card card = Card.builder()
                .userId(createCardDto.getUserId())
                .cardNumber(getRandomCardNumber())
                .accountNumber(createCardDto.getAccountNumber())
                .cardName("DEBIT") //promeniti, ne znam sta treba da stoji :(
                .creationDate(System.currentTimeMillis())
                .expireDate(System.currentTimeMillis() + 1000000000)
                .cvv(getRandomCvvNumber())
                .status(true)
                .build();
        cardRepository.save(card);
        return ResponseEntity.ok("Card created");
    }

    private String getRandomCardNumber(){
        Random random = new Random();
        long leftLimit = 1_000_000_000_000_000L; // 16 digits
        long rightLimit = 9_999_999_999_999_999L; // 16 digits
        long generatedCardNumber = leftLimit + (long) (random.nextDouble() * (rightLimit - leftLimit));
        return Long.toString(generatedCardNumber);

    }
    private String getRandomCvvNumber(){
        Random random = new Random();
        long leftLimit = 100L; // 3 digits
        long rightLimit = 999L; // 3 digits
        long generatedCvvNumber = leftLimit + (long) (random.nextDouble() * (rightLimit - leftLimit));
        return Long.toString(generatedCvvNumber);
    }












}
