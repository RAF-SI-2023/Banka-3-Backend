package com.example.bankservice.bootstrap;

import com.example.bankservice.domain.model.Card;
import com.example.bankservice.domain.model.Currency;
import com.example.bankservice.domain.model.accounts.CompanyAccount;
import com.example.bankservice.domain.model.accounts.UserAccount;
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

        UserAccount jankoRacunDinarski = new UserAccount();
        jankoRacunDinarski.setUserId(2L);
        jankoRacunDinarski.setEmployeeId(1L);
        jankoRacunDinarski.setAccountNumber("1111111111111111");
        jankoRacunDinarski.setReservedAmount(new BigDecimal(1000));
        jankoRacunDinarski.setAvailableBalance(new BigDecimal(10000));
        jankoRacunDinarski.setCreationDate(System.currentTimeMillis());
        jankoRacunDinarski.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        jankoRacunDinarski.setCurrency(currencyRepository.findById(1L).orElse(null));
        jankoRacunDinarski.setAccountType("DEBIT");
        jankoRacunDinarski.setActive(true);

        UserAccount jankoRacunEuro = new UserAccount();
        jankoRacunEuro.setUserId(2L);
        jankoRacunEuro.setEmployeeId(1L);
        jankoRacunEuro.setAccountNumber("1231231231231231");
        jankoRacunEuro.setReservedAmount(new BigDecimal(1000));
        jankoRacunEuro.setAvailableBalance(new BigDecimal(10000));
        jankoRacunEuro.setCreationDate(System.currentTimeMillis());
        jankoRacunEuro.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        jankoRacunEuro.setCurrency(currencyRepository.findById(2L).orElse(null));
        jankoRacunEuro.setAccountType("DEBIT");
        jankoRacunEuro.setActive(true);

        UserAccount strahinjaRacunDinarski = new UserAccount();
        strahinjaRacunDinarski.setUserId(1L);
        strahinjaRacunDinarski.setEmployeeId(1L);
        strahinjaRacunDinarski.setAccountNumber("2222222222222222");
        strahinjaRacunDinarski.setReservedAmount(new BigDecimal(1000));
        strahinjaRacunDinarski.setAvailableBalance(new BigDecimal(10000));
        strahinjaRacunDinarski.setCreationDate(System.currentTimeMillis());
        strahinjaRacunDinarski.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        strahinjaRacunDinarski.setCurrency(currencyRepository.findById(1L).orElse(null));
        strahinjaRacunDinarski.setAccountType("DEBIT");
        strahinjaRacunDinarski.setActive(true);

        UserAccount strahinjaRacunEuro = new UserAccount();
        strahinjaRacunEuro.setUserId(1L);
        strahinjaRacunEuro.setEmployeeId(1L);
        strahinjaRacunEuro.setAccountNumber("3213213213213213");
        strahinjaRacunEuro.setReservedAmount(new BigDecimal(1000));
        strahinjaRacunEuro.setAvailableBalance(new BigDecimal(10000));
        strahinjaRacunEuro.setCreationDate(System.currentTimeMillis());
        strahinjaRacunEuro.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        strahinjaRacunEuro.setCurrency(currencyRepository.findById(2L).orElse(null));
        strahinjaRacunEuro.setAccountType("DEBIT");
        strahinjaRacunEuro.setActive(true);

        CompanyAccount companyAccount = new CompanyAccount();
        companyAccount.setCompanyId(1L);
        companyAccount.setEmployeeId(1L);
        companyAccount.setAccountNumber("3333333333333333");
        companyAccount.setReservedAmount(new BigDecimal(1000));
        companyAccount.setAvailableBalance(new BigDecimal(10000));
        companyAccount.setCreationDate(System.currentTimeMillis());
        companyAccount.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        companyAccount.setCurrency(currencyRepository.findById(1L).orElse(null));
        companyAccount.setActive(true);

        CompanyAccount companyAccount2 = new CompanyAccount();
        companyAccount2.setCompanyId(1L);
        companyAccount2.setEmployeeId(1L);
        companyAccount2.setAccountNumber("4444444444444444");
        companyAccount2.setReservedAmount(new BigDecimal(1000));
        companyAccount2.setAvailableBalance(new BigDecimal(10000));
        companyAccount2.setCreationDate(System.currentTimeMillis());
        companyAccount2.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        companyAccount2.setCurrency(currencyRepository.findById(2L).orElse(null));
        companyAccount2.setActive(true);

        CompanyAccount companyAccount3 = new CompanyAccount();
        companyAccount3.setCompanyId(1L);
        companyAccount3.setEmployeeId(1L);
        companyAccount3.setAccountNumber("5555555555555555");
        companyAccount3.setReservedAmount(new BigDecimal(1000));
        companyAccount3.setAvailableBalance(new BigDecimal(10000));
        companyAccount3.setCreationDate(System.currentTimeMillis());
        companyAccount3.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        companyAccount3.setCurrency(currencyRepository.findById(3L).orElse(null));
        companyAccount3.setActive(true);

        CompanyAccount companyAccount4 = new CompanyAccount();
        companyAccount4.setCompanyId(1L);
        companyAccount4.setEmployeeId(1L);
        companyAccount4.setAccountNumber("6666666666666666");
        companyAccount4.setReservedAmount(new BigDecimal(1000));
        companyAccount4.setAvailableBalance(new BigDecimal(10000));
        companyAccount4.setCreationDate(System.currentTimeMillis());
        companyAccount4.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        companyAccount4.setCurrency(currencyRepository.findById(4L).orElse(null));
        companyAccount4.setActive(true);

        CompanyAccount companyAccount5 = new CompanyAccount();
        companyAccount5.setCompanyId(1L);
        companyAccount5.setEmployeeId(1L);
        companyAccount5.setAccountNumber("7777777777777777");
        companyAccount5.setReservedAmount(new BigDecimal(1000));
        companyAccount5.setAvailableBalance(new BigDecimal(10000));
        companyAccount5.setCreationDate(System.currentTimeMillis());
        companyAccount5.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        companyAccount5.setCurrency(currencyRepository.findById(5L).orElse(null));
        companyAccount5.setActive(true);

        CompanyAccount companyAccount6 = new CompanyAccount();
        companyAccount6.setCompanyId(1L);
        companyAccount6.setEmployeeId(1L);
        companyAccount6.setAccountNumber("8888888888888888");
        companyAccount6.setReservedAmount(new BigDecimal(1000));
        companyAccount6.setAvailableBalance(new BigDecimal(10000));
        companyAccount6.setCreationDate(System.currentTimeMillis());
        companyAccount6.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        companyAccount6.setCurrency(currencyRepository.findById(6L).orElse(null));
        companyAccount6.setActive(true);

        if (accountRepository.count() == 0) {
            loadUserAccountData(List.of(jankoRacunDinarski,
                    strahinjaRacunDinarski,
                    jankoRacunEuro,
                    strahinjaRacunEuro));
            loadCompanyAccountData(List.of(companyAccount,
                    companyAccount2,
                    companyAccount3,
                    companyAccount4,
                    companyAccount5,
                    companyAccount6));
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