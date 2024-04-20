package com.example.bankservice.bootstrap;

import com.example.bankservice.domain.model.Account;
import com.example.bankservice.domain.model.Currency;
import com.example.bankservice.domain.model.enums.CurrencyName;
import com.example.bankservice.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final CompanyAccountRepository companyAccountRepository;
    private final CurrencyRepository currencyRepository;
    private final TransactionRepository transactionRepository;
    private final CreditRepository creditRepository;
    private final CreditRequestRepository creditRequestRepository;
    private final CardRepository cardRepository;

    @Override
    public void run(String... args) {

        Currency rsd = new Currency();
        rsd.setMark("RSD");
        rsd.setName(CurrencyName.DINAR);

        Currency eur = new Currency();
        eur.setMark("EUR");
        eur.setName(CurrencyName.EURO);

        Currency usd = new Currency();
        usd.setMark("USD");
        usd.setName(CurrencyName.DOLLAR);

        Currency chf = new Currency();
        chf.setMark("CHF");
        chf.setName(CurrencyName.FRANK);

        Currency gbp = new Currency();
        gbp.setMark("GBP");
        gbp.setName(CurrencyName.FUNTA);

        Currency jpy = new Currency();
        jpy.setMark("JPY");
        jpy.setName(CurrencyName.JEN);

        if (currencyRepository.count() == 0) {
            loadCurrencyData(Arrays.asList(rsd, eur, usd, chf, gbp, jpy));
        }

        Account account = new Account();
        account.setUserId(1L);
        account.setEmployeeId(1L);
        account.setAccountNumber("1111111111111111");
        account.setReservedAmount(new BigDecimal(1000));
        account.setAvailableBalance(new BigDecimal(10000));
        account.setCreationDate(System.currentTimeMillis());
        account.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        account.setCurrency(currencyRepository.findById(1L).orElse(null));
        account.setAccountType("DEBIT");
        account.setActive(true);

        if (accountRepository.count() == 0) {
            loadAccountData(Arrays.asList(account));
        }
    }

    private void loadAccountData(List<Account> accounts) {
        accountRepository.saveAll(accounts);
    }

    private void loadCurrencyData(List<Currency> currencies) {
        currencyRepository.saveAll(currencies);
    }
}
