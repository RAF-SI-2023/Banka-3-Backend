package com.example.bankservice;

import com.example.bankservice.domain.dto.card.*;
import com.example.bankservice.domain.model.Card;
import com.example.bankservice.domain.model.accounts.CompanyAccount;
import com.example.bankservice.domain.model.accounts.UserAccount;
import com.example.bankservice.repository.AccountRepository;
import com.example.bankservice.repository.CardRepository;
import com.example.bankservice.repository.CompanyAccountRepository;
import com.example.bankservice.repository.UserAccountRepository;
import com.example.bankservice.service.CardService;
import io.cucumber.java.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CardServiceUnitTest {

    @InjectMocks
    private CardService cardService;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private UserAccountRepository userAccountRepository;
    @Mock
    private CompanyAccountRepository companyAccountRepository;
    @Mock
    private AccountRepository accountRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(CardServiceUnitTest.class);
    }

    private Card createDummyCard(String cardNumber){
        Card card = new Card();
        card.setCardNumber(cardNumber);
        card.setCardName("Test Card");
        card.setActive(true);
        card.setCVV("123");
        card.setCreationDate(System.currentTimeMillis());
        card.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        card.setAccountNumber("1581231231231888");
        return card;
    }

    private UserAccount createDummyUserAccount(String accountNumber){
        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(1L);
        userAccount.setActive(true);
        userAccount.setEmployeeId(1L);
        userAccount.setAvailableBalance(BigDecimal.valueOf(1000));
        userAccount.setReservedAmount(BigDecimal.valueOf(0));
        userAccount.setCreationDate(System.currentTimeMillis());
        userAccount.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        userAccount.setAccountNumber(accountNumber);
        return userAccount;
    }

    private CompanyAccount createDummyCompanyAccount(String accountNumber){
        CompanyAccount companyAccount = new CompanyAccount();
        companyAccount.setCompanyId(1L);
        companyAccount.setActive(true);
        companyAccount.setEmployeeId(1L);
        companyAccount.setAvailableBalance(BigDecimal.valueOf(1000));
        companyAccount.setReservedAmount(BigDecimal.valueOf(0));
        companyAccount.setCreationDate(System.currentTimeMillis());
        companyAccount.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        companyAccount.setAccountNumber(accountNumber);
        return companyAccount;
    }

    private CardLoginDto createDummyCardLoginDto(){
        CardLoginDto cardLoginDto = new CardLoginDto();
        cardLoginDto.setCardNumber("12345678");
        cardLoginDto.setCVV("123");
        return cardLoginDto;
    }

    private DepositFundsDto createDummyDepositFundsDto(){
        DepositFundsDto depositFundsDto = new DepositFundsDto();
        depositFundsDto.setAccountNumber("1581231231231888");
        depositFundsDto.setAmount(100.0);
        return depositFundsDto;
    }

    private WithdrawFundsDto createDummyWithdrawFundsDto(){
        WithdrawFundsDto withdrawFundsDto = new WithdrawFundsDto();
        withdrawFundsDto.setAccountNumber("1581231231231888");
        withdrawFundsDto.setAmount(100.0);
        return withdrawFundsDto;
    }

    @Test
    public void findAllTest(){
        Card card1 = createDummyCard("12345678");
        Card card2 = createDummyCard("87654321");

        List<Card> cards = List.of(card1, card2);

        given(cardRepository.findAll()).willReturn(cards);

        List<CardDto> dtos = cardService.findAll();
        for(CardDto dto : dtos){
            boolean found = false;
            for(Card c: cards){
                if(c.getCardNumber().equals(dto.getCardNumber())){
                    found = true;
                    break;
                }
            }
            if(!found){
                fail("Card not found");
            }
        }

    }

    @Test
    public void findAllByAccountNumberTest(){
        Card card1 = createDummyCard("12345678");
        Card card2 = createDummyCard("87654321");

        List<Card> cards = List.of(card1, card2);

        given(cardRepository.findAllByAccountNumber("1581231231231888")).willReturn(Optional.of(cards));

        List<CardDto> dtos = cardService.findAllByAccountNumber("1581231231231888");
        for(CardDto dto : dtos){
            boolean found = false;
            for(Card c: cards){
                if(c.getCardNumber().equals(dto.getCardNumber())){
                    found = true;
                    break;
                }
            }
            if(!found){
                fail("Card not found");
            }
        }

    }
    @Test
    public void findAllByAccountNumber_NotFoundTest(){

        given(cardRepository.findAllByAccountNumber("1581231231231888")).willReturn(Optional.empty());

        //List<CardDto> dtos = cardService.findAllByAccountNumber("1581231231231888");
        assertThrows(RuntimeException.class, () -> cardService.findAllByAccountNumber("1581231231231888"));

    }

    @Test
    public void findAllByUserIdTest(){
        Card card1 = createDummyCard("12345678");
        Card card2 = createDummyCard("87654321");
        UserAccount user = createDummyUserAccount("1581231231231888");

        List<Card> cards = List.of(card1, card2);
        List<UserAccount> users = List.of(user);

        given(userAccountRepository.findAllByUserId(1L)).willReturn(Optional.of(users));
        given(cardRepository.findAllByAccountNumber("1581231231231888")).willReturn(Optional.of(cards));

        List<CardDto> dtos = cardService.findAllByUserId(1L);
        for(CardDto dto : dtos){
            boolean found = false;
            for(Card c: cards){
                if(c.getCardNumber().equals(dto.getCardNumber())){
                    found = true;
                    break;
                }
            }
            if(!found){
                fail("Card not found");
            }
        }
    }

    @Test
    public void findAllByUserId_UserNotFoundTest(){
        Card card1 = createDummyCard("12345678");
        Card card2 = createDummyCard("87654321");
        UserAccount user = createDummyUserAccount("1581231231231888");

        List<Card> cards = List.of(card1, card2);
        List<UserAccount> users = List.of(user);

        given(userAccountRepository.findAllByUserId(1L)).willReturn(Optional.empty());
        given(cardRepository.findAllByAccountNumber("1581231231231888")).willReturn(Optional.of(cards));

        //List<CardDto> dtos = cardService.findAllByUserId(1L);
        assertThrows(RuntimeException.class, () -> cardService.findAllByUserId(1L));
    }

    @Test
    public void findAllByUserId_CardNotFoundTest(){
        Card card1 = createDummyCard("12345678");
        Card card2 = createDummyCard("87654321");
        UserAccount user = createDummyUserAccount("1581231231231888");

        List<Card> cards = List.of(card1, card2);
        List<UserAccount> users = List.of(user);

        given(userAccountRepository.findAllByUserId(1L)).willReturn(Optional.of(users));
        given(cardRepository.findAllByAccountNumber("1581231231231888")).willReturn(Optional.empty());

        //List<CardDto> dtos = cardService.findAllByUserId(1L);
        assertThrows(RuntimeException.class, () -> cardService.findAllByUserId(1L));
    }

    @Test
    public void findAllByCompanyIdTest(){
        Card card1 = createDummyCard("12345678");
        Card card2 = createDummyCard("87654321");
        CompanyAccount company = createDummyCompanyAccount("1581231231231888");

        List<Card> cards = List.of(card1, card2);
        List<CompanyAccount> companies = List.of(company);

        given(companyAccountRepository.findAllByCompanyId(1L)).willReturn(Optional.of(companies));
        given(cardRepository.findAllByAccountNumber("1581231231231888")).willReturn(Optional.of(cards));

        List<CardDto> dtos = cardService.findAllByCompanyId(1L);
        for(CardDto dto : dtos){
            boolean found = false;
            for(Card c: cards){
                if(c.getCardNumber().equals(dto.getCardNumber())){
                    found = true;
                    break;
                }
            }
            if(!found){
                fail("Card not found");
            }
        }
    }

    @Test
    public void findAllByCompanyId_CompanyNotFoundTest(){
        Card card1 = createDummyCard("12345678");
        Card card2 = createDummyCard("87654321");
        CompanyAccount company = createDummyCompanyAccount("1581231231231888");

        List<Card> cards = List.of(card1, card2);
        List<CompanyAccount> companies = List.of(company);

        given(companyAccountRepository.findAllByCompanyId(1L)).willReturn(Optional.empty());
        given(cardRepository.findAllByAccountNumber("1581231231231888")).willReturn(Optional.of(cards));

        //List<CardDto> dtos = cardService.findAllByUserId(1L);
        assertThrows(RuntimeException.class, () -> cardService.findAllByUserId(1L));
    }

    @Test
    public void findAllByCompanyId_CardNotFoundTest(){
        Card card1 = createDummyCard("12345678");
        Card card2 = createDummyCard("87654321");
        CompanyAccount company = createDummyCompanyAccount("1581231231231888");

        List<Card> cards = List.of(card1, card2);
        List<CompanyAccount> companies = List.of(company);

        given(companyAccountRepository.findAllByCompanyId(1L)).willReturn(Optional.of(companies));
        given(cardRepository.findAllByAccountNumber("1581231231231888")).willReturn(Optional.empty());

        //List<CardDto> dtos = cardService.findAllByUserId(1L);
        assertThrows(RuntimeException.class, () -> cardService.findAllByUserId(1L));
    }

    @Test
    public void cardLoginTest(){
        Card card = createDummyCard("12345678");
        CardLoginDto cardLoginDto = createDummyCardLoginDto();

        given(cardRepository.findByCardNumber("12345678")).willReturn(Optional.of(card));

        CardResponseDto response = cardService.cardLogin(cardLoginDto);
        assert(response.getAccountNumber().equals(card.getAccountNumber()));
    }
    @Test
    public void cardLogin_CardNotFoundTest(){
        Card card = createDummyCard("12345678");
        CardLoginDto cardLoginDto = createDummyCardLoginDto();

        given(cardRepository.findByCardNumber("12345678")).willReturn(Optional.empty());

        //CardResponseDto response = cardService.cardLogin(cardLoginDto);
        assertThrows(RuntimeException.class, () -> cardService.cardLogin(cardLoginDto));
    }
    @Test
    public void cardLogin_CardNotActiveTest(){
        Card card = createDummyCard("12345678");
        card.setActive(false);
        CardLoginDto cardLoginDto = createDummyCardLoginDto();

        given(cardRepository.findByCardNumber("12345678")).willReturn(Optional.of(card));

        //CardResponseDto response = cardService.cardLogin(cardLoginDto);
        assertThrows(RuntimeException.class, () -> cardService.cardLogin(cardLoginDto));
    }
    @Test
    public void cardLogin_InvalidCvvTest(){
        Card card = createDummyCard("12345678");
        card.setCVV("1");
        CardLoginDto cardLoginDto = createDummyCardLoginDto();

        given(cardRepository.findByCardNumber("12345678")).willReturn(Optional.of(card));

        //CardResponseDto response = cardService.cardLogin(cardLoginDto);
        assertThrows(RuntimeException.class, () -> cardService.cardLogin(cardLoginDto));
    }

    @Test
    public void depositTest(){
        UserAccount user = createDummyUserAccount("1581231231231888");
        DepositFundsDto depositFundsDto = createDummyDepositFundsDto();

        given(accountRepository.findByAccountNumber("1581231231231888")).willReturn(Optional.of(user));

        cardService.deposit(depositFundsDto);
        assertEquals(user.getAvailableBalance(), BigDecimal.valueOf(1100));
    }

    @Test
    public void deposit_NotFoundTest(){
        UserAccount user = createDummyUserAccount("1581231231231888");
        DepositFundsDto depositFundsDto = createDummyDepositFundsDto();

        given(accountRepository.findByAccountNumber("1581231231231888")).willReturn(Optional.empty());

        //cardService.deposit(depositFundsDto);
        assertThrows(RuntimeException.class, () -> cardService.deposit(depositFundsDto));
    }

    @Test
    public void deposit_NotActiveAccountTest(){
        UserAccount user = createDummyUserAccount("1581231231231888");
        user.setActive(false);
        DepositFundsDto depositFundsDto = createDummyDepositFundsDto();

        given(accountRepository.findByAccountNumber("1581231231231888")).willReturn(Optional.of(user));

        //cardService.deposit(depositFundsDto);
        assertThrows(RuntimeException.class, () -> cardService.deposit(depositFundsDto));
    }

    @Test
    public void withdrawTest(){
        UserAccount user = createDummyUserAccount("1581231231231888");
        WithdrawFundsDto withdrawFundsDto = createDummyWithdrawFundsDto();

        given(accountRepository.findByAccountNumber("1581231231231888")).willReturn(Optional.of(user));

        //cardService.withdraw(withdrawFundsDto);
        assertEquals(user.getAvailableBalance(), BigDecimal.valueOf(900));
    }

    @Test
    public void withdraw_NotFoundTest(){
        UserAccount user = createDummyUserAccount("1581231231231888");
        WithdrawFundsDto withdrawFundsDto = createDummyWithdrawFundsDto();

        given(accountRepository.findByAccountNumber("1581231231231888")).willReturn(Optional.empty());

        //cardService.withdraw(withdrawFundsDto);
        assertThrows(RuntimeException.class, () -> cardService.withdraw(withdrawFundsDto));
    }
    @Test
    public void withdraw_NotActiveAccountTest(){
        UserAccount user = createDummyUserAccount("1581231231231888");
        user.setActive(false);
        WithdrawFundsDto withdrawFundsDto = createDummyWithdrawFundsDto();

        given(accountRepository.findByAccountNumber("1581231231231888")).willReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> cardService.withdraw(withdrawFundsDto));
    }
    @Test
    public void withdraw_InsufficientFundsTest() {
        UserAccount user = createDummyUserAccount("1581231231231888");
        user.setAvailableBalance(BigDecimal.valueOf(50));
        WithdrawFundsDto withdrawFundsDto = createDummyWithdrawFundsDto();

        given(accountRepository.findByAccountNumber("1581231231231888")).willReturn(Optional.of(user));
        assertThrows(RuntimeException.class, () -> cardService.withdraw(withdrawFundsDto));

    }

    @Test
    public void deactivateCardTest(){
        Card card = createDummyCard("12345678");

        given(cardRepository.findById(1L)).willReturn(Optional.of(card));

        cardService.deactivateCard(1L);
        assertFalse(card.isActive());
    }
    @Test
    public void activateCardTest(){
        Card card = createDummyCard("12345678");

        given(cardRepository.findById(1L)).willReturn(Optional.of(card));

        cardService.activateCard(1L);
        assertTrue(card.isActive());
    }
}
