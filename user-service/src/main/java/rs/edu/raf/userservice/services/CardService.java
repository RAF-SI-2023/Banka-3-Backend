package rs.edu.raf.userservice.services;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rs.edu.raf.userservice.domains.dto.card.CardDto;
import rs.edu.raf.userservice.domains.dto.card.CardResponseDto;
import rs.edu.raf.userservice.domains.dto.card.CreateCardDto;
import rs.edu.raf.userservice.domains.mappers.CardMapper;
import rs.edu.raf.userservice.domains.model.Account;
import rs.edu.raf.userservice.domains.model.Card;
import rs.edu.raf.userservice.repositories.AccountRepository;
import rs.edu.raf.userservice.repositories.CardRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;

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

    public ResponseEntity<CardResponseDto> cardLogin(Long userId, String accNumber, String cvv){
        Optional<Card> optionalCard = cardRepository.findByAccountNumber(accNumber);
        if(optionalCard.isPresent()){
            CardResponseDto dto = new CardResponseDto(accNumber);
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<String> deposit(String accountNumber, Long amount){

        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);
        if (!optionalAccount.isPresent()) return ResponseEntity.badRequest().build();
        Account account = optionalAccount.get();


        account.setAvailableBalance(account.getAvailableBalance().add(new BigDecimal(amount)));
        accountRepository.save(account);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<String> withdraw(String accountNumber, Long amount){
        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);

        if (!optionalAccount.isPresent()) return ResponseEntity.badRequest().build();
        Account account = optionalAccount.get();

        if(account.getAvailableBalance().compareTo(new BigDecimal(amount)) < 0) return ResponseEntity.badRequest().build();
        account.setAvailableBalance(account.getAvailableBalance().subtract(new BigDecimal(amount)));
        accountRepository.save(account);

        return ResponseEntity.ok().build();
    }












}
