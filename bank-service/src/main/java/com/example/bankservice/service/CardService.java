package com.example.bankservice.service;

import com.example.bankservice.domain.dto.card.CardDto;
import com.example.bankservice.domain.mapper.CardMapper;
import com.example.bankservice.domain.model.Card;
import com.example.bankservice.repository.AccountRepository;
import com.example.bankservice.repository.CardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    public List<CardDto> findAll() {
        return cardRepository.findAll().stream().filter(Card::isActive).map(cardMapper::cardToCardDto).toList();
    }

    public List<CardDto> findAllByAccountNumber(String accountNumber) {
        return cardRepository.findAllByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Unable to find cards for account number"))
                .stream()
                .filter(Card::isActive).map(cardMapper::cardToCardDto).toList();
    }

    public void deactivateCard(Long cardId) {
        cardRepository.findById(cardId).ifPresent(card -> {
            card.setActive(false);
            cardRepository.save(card);
        });
    }

    public void activateCard(Long cardId) {
        cardRepository.findById(cardId).ifPresent(card -> {
            card.setActive(true);
            cardRepository.save(card);
        });
    }
}
