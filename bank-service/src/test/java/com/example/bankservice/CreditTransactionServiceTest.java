//package com.example.bankservice;
//
//import com.example.bankservice.domains.dto.CreditTransactionDto;
//import com.example.bankservice.repositories.CreditTransactionRepository;
//import com.example.bankservice.services.CreditTransactionService;
//import io.cucumber.java.Before;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//import static org.mockito.ArgumentMatchers.anyList;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//
//@ExtendWith(MockitoExtension.class)
//class CreditTransactionServiceTest {
//    @Mock
//    private CreditTransactionRepository creditTransactionRepository;
//
//    @InjectMocks
//    private CreditTransactionService creditTransactionService;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    public void testCreateCreditTransactions() {
//        // Priprema podataka za test
//        List<CreditTransactionDto> transactionCreditDtos = Stream.of(
//                new CreditTransactionDto(/* Popunite polja za prvi transaction */),
//                new CreditTransactionDto(/* Popunite polja za drugi transaction */)
//        ).collect(Collectors.toList());
//
//        // Izvršavanje metode koju testiramo
//        creditTransactionService.createCreditTransactions(transactionCreditDtos);
//
//        // Provera poziva saveAll metoda nad repository-jem
//        verify(creditTransactionRepository, times(1)).saveAll(anyList());
//    }
//}