package com.example.bankservice.bootstrap;

import com.example.bankservice.domain.model.accounts.UserAccount;
import com.example.bankservice.domain.model.Card;
import com.example.bankservice.domain.model.accounts.CompanyAccount;
import com.example.bankservice.domain.model.Currency;
import com.example.bankservice.domain.model.enums.CurrencyName;
import com.example.bankservice.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
            loadCurrencyData(List.of(rsd, eur, usd, chf, gbp, jpy));
        }

        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(2L);
        userAccount.setEmployeeId(1L);
        userAccount.setAccountNumber("1111111111111111");
        userAccount.setReservedAmount(new BigDecimal(1000));
        userAccount.setAvailableBalance(new BigDecimal(10000));
        userAccount.setCreationDate(System.currentTimeMillis());
        userAccount.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        userAccount.setCurrency(currencyRepository.findById(1L).orElse(null));
        userAccount.setAccountType("DEBIT");
        userAccount.setActive(true);

        CompanyAccount companyAccount = new CompanyAccount();
        companyAccount.setCompanyId(1L);
        companyAccount.setEmployeeId(1L);
        companyAccount.setAccountNumber("2222222222222222");
        companyAccount.setReservedAmount(new BigDecimal(1000));
        companyAccount.setAvailableBalance(new BigDecimal(10000));
        companyAccount.setCreationDate(System.currentTimeMillis());
        companyAccount.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        companyAccount.setCurrency(currencyRepository.findById(1L).orElse(null));
        companyAccount.setActive(true);

        if (accountRepository.count() == 0) {
            loadUserAccountData(List.of(userAccount));
            loadCompanyAccountData(List.of(companyAccount));
        }

        Card normalAccountCard = new Card();
        normalAccountCard.setAccountNumber("1111111111111111");
        normalAccountCard.setCardNumber("12345678");
        normalAccountCard.setCardName("DEBIT");
        normalAccountCard.setCVV("123");
        normalAccountCard.setCreationDate(System.currentTimeMillis());
        normalAccountCard.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        normalAccountCard.setActive(true);

        Card companyAccountCard = new Card();
        companyAccountCard.setAccountNumber("2222222222222222");
        companyAccountCard.setCardNumber("87654321");
        companyAccountCard.setCardName("DEBIT");
        companyAccountCard.setCVV("321");
        companyAccountCard.setCreationDate(System.currentTimeMillis());
        companyAccountCard.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        companyAccountCard.setActive(true);

        if (cardRepository.count() == 0) {
            loadCardData(List.of(normalAccountCard, companyAccountCard));
        }
    }

    private void loadUserAccountData(List<UserAccount> userAccounts) {
        accountRepository.saveAll(userAccounts);
    }

    private void loadCurrencyData(List<Currency> currencies) {
        currencyRepository.saveAll(currencies);
    }

    private void loadCompanyAccountData(List<CompanyAccount> companyAccounts) {
        accountRepository.saveAll(companyAccounts);
    }

    private void loadCardData(List<Card> cards) {
        cardRepository.saveAll(cards);
    }
}
