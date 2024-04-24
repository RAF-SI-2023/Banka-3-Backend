package com.example.bankservice;

import com.example.bankservice.domain.model.Currency;
import com.example.bankservice.domain.model.enums.CurrencyName;
import com.example.bankservice.repository.CurrencyRepository;
import com.example.bankservice.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {
    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CurrencyService currencyService;

    @Test
    void findByMark_CurrencyExists_ReturnsCurrency() {
        // Arrange
        String currencyMark = "USD";

        Currency expectedCurrency = new Currency(1L, CurrencyName.DOLLAR, currencyMark);

        when(currencyRepository.findByMark(currencyMark)).thenReturn(Optional.of(expectedCurrency));

        Currency actualCurrency = currencyService.findByMark(currencyMark);

        assertEquals(expectedCurrency, actualCurrency);
    }

    @Test
    void findByMark_CurrencyDoesNotExist_ThrowsRuntimeException() {
        String currencyMark = "EUR";

        when(currencyRepository.findByMark(currencyMark)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> currencyService.findByMark(currencyMark));
    }

    @Test
    void findAll_ReturnsListOfCurrencies() {
        List<Currency> expectedCurrencies = Arrays.asList(
                new Currency(1L, CurrencyName.DOLLAR,"USD"),
                new Currency(2L, CurrencyName.EURO, "EUR"),
                new Currency(3L,CurrencyName.DINAR, "RSD")
        );

        when(currencyRepository.findAll()).thenReturn(expectedCurrencies);

        List<Currency> actualCurrencies = currencyService.findAll();

        assertEquals(expectedCurrencies.size(), actualCurrencies.size());
        for (int i = 0; i < expectedCurrencies.size(); i++) {
            assertEquals(expectedCurrencies.get(i), actualCurrencies.get(i));
        }
    }
}