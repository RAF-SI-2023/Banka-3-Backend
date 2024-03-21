package rs.edu.raf.userservice.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.edu.raf.userservice.domains.model.Currency;
import rs.edu.raf.userservice.repositories.CurrencyRepository;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/currency")
public class CurrencyController {
    @Autowired
    private CurrencyRepository currencyRepository;

    @GetMapping("/getAll")
    public List<Currency> findAllCurrencies() {
        return currencyRepository.findAll();
    }

    @GetMapping("/findByMark?mark={mark}")
    public Currency findByName(String mark) {
        return currencyRepository.findByName(mark).orElse(null);
    }
}
