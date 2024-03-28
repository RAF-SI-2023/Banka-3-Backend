package rs.edu.raf.userservice;


import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.userservice.domains.dto.credit.CreateCreditDto;
import rs.edu.raf.userservice.domains.dto.credit.CreditDto;
import rs.edu.raf.userservice.domains.exceptions.ForbiddenException;
import rs.edu.raf.userservice.domains.model.Account;
import rs.edu.raf.userservice.domains.model.Credit;
import rs.edu.raf.userservice.domains.model.User;
import rs.edu.raf.userservice.repositories.AccountRepository;
import rs.edu.raf.userservice.repositories.CreditRepository;
import rs.edu.raf.userservice.repositories.UserRepository;
import rs.edu.raf.userservice.services.CreditService;
import rs.edu.raf.userservice.utils.BankServiceClient;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreditServiceTest {

    @Mock
    private CreditRepository creditRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private BankServiceClient bankServiceClient;
    @InjectMocks
    private CreditService creditService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAll() {

        User user = new User();
        User user1 = new User();

        // Priprema podataka za test
        List<Credit> credits = Arrays.asList(
                new Credit(1l, user, "Name", "123456789", BigDecimal.valueOf(100.00), 50, BigDecimal.valueOf(0.05), 280599l, 13022011l, BigDecimal.valueOf(300.00), BigDecimal.valueOf(20000.00), "RSD"),
                new Credit(2l, user1, "Name", "123456789", BigDecimal.valueOf(100.00), 50, BigDecimal.valueOf(0.05), 280599l, 13022011l, BigDecimal.valueOf(300.00), BigDecimal.valueOf(20000.00), "RSD")
        );

        // Podešavanje ponašanja mock CreditRepository-ja
        when(creditRepository.findAll()).thenReturn(credits);

        // Izvršavanje metode koju testiramo
        List<CreditDto> result = creditService.findAll();

        // Provera rezultata
        assertEquals(credits.size(), result.size());
        for (int i = 0; i < credits.size(); i++) {
            assertEquals(credits.get(i).getUser(), result.get(i).getUser());
            assertEquals(credits.get(i).getName(), result.get(i).getName());
            assertEquals(credits.get(i).getAccountNumber(), result.get(i).getAccountNumber());
            assertEquals(credits.get(i).getAmount(), BigDecimal.valueOf(result.get(i).getAmount()));
            assertEquals(credits.get(i).getPaymentPeriod(), result.get(i).getPaymentPeriod());
            assertEquals(credits.get(i).getStartDate(), result.get(i).getStartDate());
            assertEquals(credits.get(i).getEndDate(), result.get(i).getEndDate());
            assertEquals(credits.get(i).getMonthlyFee(), BigDecimal.valueOf(result.get(i).getMonthlyFee()));
            assertEquals(credits.get(i).getRemainingAmount(), BigDecimal.valueOf(result.get(i).getRemainingAmount()));
            assertEquals(credits.get(i).getCurrencyMark(), result.get(i).getCurrencyMark());
        }
    }
    @Test
    public void testFindAllNoData() {
        // Priprema
        List<Credit> emptyList = Collections.emptyList();

        when(creditRepository.findAll()).thenReturn(emptyList);

        // Izvršenje
        List<CreditDto> result = creditService.findAll();

        // Provera
        assertEquals(0, result.size());

        // Provera da li se metoda findAll pozvala tačno jednom
        verify(creditRepository, times(1)).findAll();
    }

    @Test
    public void testFindAllUserCredits() {
        User user = new User();
        User user1 = new User();

        Credit credit1 = new Credit(1l, user, "Name", "123456789", BigDecimal.valueOf(100.00), 50, BigDecimal.valueOf(0.05), 280599l, 13022011l, BigDecimal.valueOf(300.00), BigDecimal.valueOf(20000.00), "RSD");
        Credit credit2 = new Credit(2l, user1, "Name", "123456789", BigDecimal.valueOf(100.00), 50, BigDecimal.valueOf(0.05), 280599l, 13022011l, BigDecimal.valueOf(300.00), BigDecimal.valueOf(20000.00), "RSD");
        List<Credit> credits = Arrays.asList(credit1, credit2);

        when(creditRepository.findByUser_UserId(user.getUserId())).thenReturn(Optional.of(credits));

        List<CreditDto> result = creditService.findAllUserCredits(user.getUserId());

        assertEquals(credits.size(), result.size());
        for (int i = 0; i < credits.size(); i++) {
            assertEquals(credits.get(i).getUser(), result.get(i).getUser());
            assertEquals(credits.get(i).getName(), result.get(i).getName());
            assertEquals(credits.get(i).getAccountNumber(), result.get(i).getAccountNumber());
            assertEquals(credits.get(i).getAmount(), BigDecimal.valueOf(result.get(i).getAmount()));
            assertEquals(credits.get(i).getPaymentPeriod(), result.get(i).getPaymentPeriod());
            assertEquals(credits.get(i).getStartDate(), result.get(i).getStartDate());
            assertEquals(credits.get(i).getEndDate(), result.get(i).getEndDate());
            assertEquals(credits.get(i).getMonthlyFee(), BigDecimal.valueOf(result.get(i).getMonthlyFee()));
            assertEquals(credits.get(i).getRemainingAmount(), BigDecimal.valueOf(result.get(i).getRemainingAmount()));
            assertEquals(credits.get(i).getCurrencyMark(), result.get(i).getCurrencyMark());
        }
    }

    @Test
    public void testFindAllUserCreditsNotFound() {
        Long userId = 1L;

        when(creditRepository.findByUser_UserId(userId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> creditService.findAllUserCredits(userId));

    }

    @Test
    public void testFindById() {
        User user = new User();
        Long id = 123L;
        Credit credit1 = new Credit(1l, user, "Name", "123456789", BigDecimal.valueOf(100.00), 50, BigDecimal.valueOf(0.05), 280599l, 13022011l, BigDecimal.valueOf(300.00), BigDecimal.valueOf(20000.00), "RSD");

        CreditDto expectedDto = new CreditDto(user, "Name", "123456789", 100.00, 50, 0.05, 280599l, 13022011l, 300.00, 20000.00, "RSD");

        // Podešavanje ponašanja mock CreditRepository-ja
        when(creditRepository.findById(id)).thenReturn(Optional.of(credit1));

        // Izvršavanje metode koju testiramo
        CreditDto result = creditService.findById(id);

        // Provera rezultata
        assertEquals(expectedDto.getUser(), result.getUser());
        assertEquals(expectedDto.getName(), result.getName());
        assertEquals(expectedDto.getAccountNumber(), result.getAccountNumber());
        assertEquals(expectedDto.getAmount(), result.getAmount());
        assertEquals(expectedDto.getPaymentPeriod(), result.getPaymentPeriod());
        assertEquals(expectedDto.getFee(), result.getFee(), 0.001); // Podesite tačnost na vaš zahtev
        assertEquals(expectedDto.getStartDate(), result.getStartDate());
        assertEquals(expectedDto.getEndDate(), result.getEndDate());
        assertEquals(expectedDto.getMonthlyFee(), result.getMonthlyFee());
        assertEquals(expectedDto.getRemainingAmount(), result.getRemainingAmount());
        assertEquals(expectedDto.getCurrencyMark(), result.getCurrencyMark());
    }

    @Test
    public void testFindByIdNotFound() {
        Long id = 123L;

        when(creditRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> creditService.findById(id));

    }

    @Test
    public void testCreateCredit() {

        User user = new User();
        user.setUserId(1L);
        Long userId = 1L;
        CreateCreditDto createCreditDto = new CreateCreditDto(userId, "Name", "123456789", "RSD", 100.00, 50);
        Credit credit = new Credit(1l, user, "Name", "123456789", BigDecimal.valueOf(100.00), 50, BigDecimal.valueOf(0.05), 280599l, 13022011l, BigDecimal.valueOf(300.00), BigDecimal.valueOf(20000.00), "RSD");
        Account account = new Account();
        account.setAvailableBalance(BigDecimal.valueOf(10000.00));


        when(userRepository.findById(createCreditDto.getUserId())).thenReturn(Optional.of(user));

        when(accountRepository.findByAccountNumber(createCreditDto.getAccountNumber())).thenReturn(Optional.of(account));

        CreditDto result = creditService.createCredit(createCreditDto);

        assertEquals(createCreditDto.getUserId(), result.getUser().getUserId());
        assertEquals(createCreditDto.getAccountNumber(), result.getAccountNumber());
        assertEquals(createCreditDto.getName(), credit.getName());
        assertEquals(createCreditDto.getCurrencyMark(), credit.getCurrencyMark());
        assertEquals(createCreditDto.getPaymentPeriod(), credit.getPaymentPeriod());
        assertEquals(BigDecimal.valueOf(createCreditDto.getAmount()), credit.getAmount());
    }

    @Test
    public void testCreditMonthlyPay() {
        // Priprema podataka za test
        User user = new User();
        User user1 = new User();



        List<Credit> credits = new ArrayList<>();
        Credit credit1 = new Credit(1l, user, "Name", "123456789", BigDecimal.valueOf(100.00), 50, BigDecimal.valueOf(0.05), 280599l, 13022011l, BigDecimal.valueOf(300.00), BigDecimal.valueOf(20000.00), "RSD");
        Credit credit2 = new Credit(2l, user1, "Name", "123456789", BigDecimal.valueOf(100.00), 50, BigDecimal.valueOf(0.05), 280599l, 13022011l, BigDecimal.valueOf(300.00), BigDecimal.valueOf(20000.00), "RSD");
        credits.add(credit1);
        credits.add(credit2);

        Account account = new Account();
        account.setAvailableBalance(BigDecimal.valueOf(10000));

        when(creditRepository.findAll()).thenReturn(credits);
        when(accountRepository.findByAccountNumber(anyString())).thenReturn(Optional.of(account));

        // Izvršavanje metode koju testiramo
        creditService.creditMonthlyPay();

        // Provera poziva save metoda
        verify(accountRepository, times(credits.size())).save(any(Account.class));
        verify(creditRepository, times(credits.size())).save(any(Credit.class));

        // Provera poziva createCreditTransactions metoda
        verify(bankServiceClient, times(1)).createCreditTransactions(anyList());
    }
}
