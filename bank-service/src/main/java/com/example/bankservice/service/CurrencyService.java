package com.example.bankservice.service;

import com.example.bankservice.domain.model.Currency;
import com.example.bankservice.repository.CurrencyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CurrencyService {

    private CurrencyRepository currencyRepository;

    public Currency findByMark(String currencyMark) {
        return currencyRepository.findByMark(currencyMark)
                .orElseThrow(() -> new RuntimeException("Currency not found"));
    }

    public List<Currency> findAll() {
        return currencyRepository.findAll();
    }
}
