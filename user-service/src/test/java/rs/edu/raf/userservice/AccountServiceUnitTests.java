package rs.edu.raf.userservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import rs.edu.raf.userservice.domains.dto.account.RebalanceAccountDto;
import rs.edu.raf.userservice.domains.model.Account;
import rs.edu.raf.userservice.repositories.AccountRepository;
import rs.edu.raf.userservice.services.AccountService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class AccountServiceUnitTests {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addMoneyToAccountShouldReturnOkWhenAccountExists() {
        RebalanceAccountDto dto = new RebalanceAccountDto();
        dto.setAccountNumber("5053791123456789");
        dto.setAmount(100.0);

        Account account = new Account();
        account.setAccountNumber("5053791123456789");
        account.setBalance(200.0);

        when(accountRepository.findByAccountNumber(dto.getAccountNumber())).thenReturn(Optional.of(account));

        ResponseEntity<String> response = accountService.addMoneyToAccount(dto);

        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    public void addMoneyToAccountShouldReturnBadRequestWhenAccountDoesNotExist() {
        RebalanceAccountDto dto = new RebalanceAccountDto();
        dto.setAccountNumber("5053791123456789");
        dto.setAmount(100.0);

        when(accountRepository.findByAccountNumber(dto.getAccountNumber())).thenReturn(Optional.empty());

        ResponseEntity<String> response = accountService.addMoneyToAccount(dto);

        assertEquals(ResponseEntity.badRequest().build(), response);
    }

    @Test
    public void takeMoneyFromAccountShouldReturnOkWhenAccountExistsAndHasEnoughBalance() {
        RebalanceAccountDto dto = new RebalanceAccountDto();
        dto.setAccountNumber("5053791123456789");
        dto.setAmount(100.0);

        Account account = new Account();
        account.setAccountNumber("5053791123456789");
        account.setBalance(200.0);

        when(accountRepository.findByAccountNumber(dto.getAccountNumber())).thenReturn(Optional.of(account));

        ResponseEntity<String> response = accountService.takeMoneyFromAccount(dto);

        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    public void takeMoneyFromAccountShouldReturnBadRequestWhenAccountDoesNotExist() {
        RebalanceAccountDto dto = new RebalanceAccountDto();
        dto.setAccountNumber("5053791123456789");
        dto.setAmount(100.0);

        when(accountRepository.findByAccountNumber(dto.getAccountNumber())).thenReturn(Optional.empty());

        ResponseEntity<String> response = accountService.takeMoneyFromAccount(dto);

        assertEquals(ResponseEntity.badRequest().build(), response);
    }

    @Test
    public void takeMoneyFromAccountShouldReturnBadRequestWhenAccountDoesNotHaveEnoughBalance() {
        RebalanceAccountDto dto = new RebalanceAccountDto();
        dto.setAccountNumber("5053791123456789");
        dto.setAmount(300.0);

        Account account = new Account();
        account.setAccountNumber("5053791123456789");
        account.setBalance(200.0);

        when(accountRepository.findByAccountNumber(dto.getAccountNumber())).thenReturn(Optional.of(account));

        ResponseEntity<String> response = accountService.takeMoneyFromAccount(dto);

        assertEquals(ResponseEntity.badRequest().build(), response);
    }
}