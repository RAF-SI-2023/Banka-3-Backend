package com.example.bankservice.bootstrap;

import com.example.bankservice.domain.model.Card;
import com.example.bankservice.domain.model.CreditRequest;
import com.example.bankservice.domain.model.Currency;
import com.example.bankservice.domain.model.accounts.CompanyAccount;
import com.example.bankservice.domain.model.accounts.UserAccount;
import com.example.bankservice.domain.model.enums.CreditRequestStatus;
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

        if (currencyRepository.count() == 0) {
            loadCurrencyData(List.of(rsd, eur, usd));
        }

        UserAccount jankoRacunDinarski = new UserAccount();
        jankoRacunDinarski.setUserId(1L);
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
        jankoRacunEuro.setUserId(1L);
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
        strahinjaRacunDinarski.setUserId(2L);
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
        strahinjaRacunEuro.setUserId(2L);
        strahinjaRacunEuro.setEmployeeId(1L);
        strahinjaRacunEuro.setAccountNumber("3213213213213213");
        strahinjaRacunEuro.setReservedAmount(new BigDecimal(1000));
        strahinjaRacunEuro.setAvailableBalance(new BigDecimal(10000));
        strahinjaRacunEuro.setCreationDate(System.currentTimeMillis());
        strahinjaRacunEuro.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        strahinjaRacunEuro.setCurrency(currencyRepository.findById(2L).orElse(null));
        strahinjaRacunEuro.setAccountType("DEBIT");
        strahinjaRacunEuro.setActive(true);

        UserAccount strahinjaRacunDolarski = new UserAccount();
        strahinjaRacunDolarski.setUserId(2L);
        strahinjaRacunDolarski.setEmployeeId(1L);
        strahinjaRacunDolarski.setAccountNumber("1212121212121212");
        strahinjaRacunDolarski.setReservedAmount(new BigDecimal(1000));
        strahinjaRacunDolarski.setAvailableBalance(new BigDecimal(10000));
        strahinjaRacunDolarski.setCreationDate(System.currentTimeMillis());
        strahinjaRacunDolarski.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        strahinjaRacunDolarski.setCurrency(currencyRepository.findById(3L).orElse(null));
        strahinjaRacunDolarski.setAccountType("DEBIT");
        strahinjaRacunDolarski.setActive(true);

        UserAccount krasicRacunDinarski = new UserAccount();
        krasicRacunDinarski.setUserId(4L);
        krasicRacunDinarski.setEmployeeId(1L);
        krasicRacunDinarski.setAccountNumber("1232121212121312");
        krasicRacunDinarski.setReservedAmount(new BigDecimal(1000));
        krasicRacunDinarski.setAvailableBalance(new BigDecimal(10000));
        krasicRacunDinarski.setCreationDate(System.currentTimeMillis());
        krasicRacunDinarski.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        krasicRacunDinarski.setCurrency(currencyRepository.findById(1L).orElse(null));
        krasicRacunDinarski.setAccountType("DEBIT");
        krasicRacunDinarski.setActive(true);

        UserAccount krasicRacunEurski = new UserAccount();
        krasicRacunEurski.setUserId(4L);
        krasicRacunEurski.setEmployeeId(1L);
        krasicRacunEurski.setAccountNumber("1242121212121412");
        krasicRacunEurski.setReservedAmount(new BigDecimal(1000));
        krasicRacunEurski.setAvailableBalance(new BigDecimal(10000));
        krasicRacunEurski.setCreationDate(System.currentTimeMillis());
        krasicRacunEurski.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        krasicRacunEurski.setCurrency(currencyRepository.findById(2L).orElse(null));
        krasicRacunEurski.setAccountType("DEBIT");
        krasicRacunEurski.setActive(true);

        UserAccount krasicRacunDolarski = new UserAccount();
        krasicRacunDolarski.setUserId(4L);
        krasicRacunDolarski.setEmployeeId(1L);
        krasicRacunDolarski.setAccountNumber("1262121212121612");
        krasicRacunDolarski.setReservedAmount(new BigDecimal(1000));
        krasicRacunDolarski.setAvailableBalance(new BigDecimal(10000));
        krasicRacunDolarski.setCreationDate(System.currentTimeMillis());
        krasicRacunDolarski.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        krasicRacunDolarski.setCurrency(currencyRepository.findById(3L).orElse(null));
        krasicRacunDolarski.setAccountType("DEBIT");
        krasicRacunDolarski.setActive(true);

        CompanyAccount companyAccount = new CompanyAccount();
        companyAccount.setCompanyId(1L);
        companyAccount.setEmployeeId(1L);
        companyAccount.setAccountNumber("3333333333333333");
        companyAccount.setReservedAmount(new BigDecimal(1000));
        companyAccount.setAvailableBalance(new BigDecimal(100000000));
        companyAccount.setCreationDate(System.currentTimeMillis());
        companyAccount.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        companyAccount.setCurrency(currencyRepository.findById(1L).orElse(null));
        companyAccount.setActive(true);

        CompanyAccount companyAccount2 = new CompanyAccount();
        companyAccount2.setCompanyId(1L);
        companyAccount2.setEmployeeId(1L);
        companyAccount2.setAccountNumber("4444444444444444");
        companyAccount2.setReservedAmount(new BigDecimal(1000));
        companyAccount2.setAvailableBalance(new BigDecimal(100000000));
        companyAccount2.setCreationDate(System.currentTimeMillis());
        companyAccount2.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        companyAccount2.setCurrency(currencyRepository.findById(2L).orElse(null));
        companyAccount2.setActive(true);

        CompanyAccount companyAccount3 = new CompanyAccount();
        companyAccount3.setCompanyId(1L);
        companyAccount3.setEmployeeId(1L);
        companyAccount3.setAccountNumber("5555555555555555");
        companyAccount3.setReservedAmount(new BigDecimal(1000));
        companyAccount3.setAvailableBalance(new BigDecimal(100000000));
        companyAccount3.setCreationDate(System.currentTimeMillis());
        companyAccount3.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        companyAccount3.setCurrency(currencyRepository.findById(3L).orElse(null));
        companyAccount3.setActive(true);


        CompanyAccount exchangeAccount1 = new CompanyAccount();
        exchangeAccount1.setCompanyId(2L);
        exchangeAccount1.setEmployeeId(1L);
        exchangeAccount1.setAccountNumber("1234567891231231");
        exchangeAccount1.setReservedAmount(new BigDecimal(1000));
        exchangeAccount1.setAvailableBalance(new BigDecimal(10000));
        exchangeAccount1.setCreationDate(System.currentTimeMillis());
        exchangeAccount1.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        exchangeAccount1.setCurrency(currencyRepository.findById(1L).orElse(null));
        exchangeAccount1.setActive(true);

        CompanyAccount exchangeAccount2 = new CompanyAccount();
        exchangeAccount2.setCompanyId(2L);
        exchangeAccount2.setEmployeeId(1L);
        exchangeAccount2.setAccountNumber("9876543219876543");
        exchangeAccount2.setReservedAmount(new BigDecimal(1000));
        exchangeAccount2.setAvailableBalance(new BigDecimal(10000));
        exchangeAccount2.setCreationDate(System.currentTimeMillis());
        exchangeAccount2.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        exchangeAccount2.setCurrency(currencyRepository.findById(2L).orElse(null));
        exchangeAccount2.setActive(true);

        CompanyAccount exchangeAccount3 = new CompanyAccount();
        exchangeAccount3.setCompanyId(2L);
        exchangeAccount3.setEmployeeId(1L);
        exchangeAccount3.setAccountNumber("1098765432101234");
        exchangeAccount3.setReservedAmount(new BigDecimal(1000));
        exchangeAccount3.setAvailableBalance(new BigDecimal(10000));
        exchangeAccount3.setCreationDate(System.currentTimeMillis());
        exchangeAccount3.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        exchangeAccount3.setCurrency(currencyRepository.findById(3L).orElse(null));
        exchangeAccount3.setActive(true);



        CompanyAccount goodCompanyAccountDin = new CompanyAccount();
        goodCompanyAccountDin.setCompanyId(3L);
        goodCompanyAccountDin.setEmployeeId(1L);
        goodCompanyAccountDin.setAccountNumber("1234567891231231");
        goodCompanyAccountDin.setReservedAmount(new BigDecimal(1000));
        goodCompanyAccountDin.setAvailableBalance(new BigDecimal(10000));
        goodCompanyAccountDin.setCreationDate(System.currentTimeMillis());
        goodCompanyAccountDin.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        goodCompanyAccountDin.setCurrency(currencyRepository.findById(1L).orElse(null));
        goodCompanyAccountDin.setActive(true);

        CompanyAccount goodCompanyAccountEur = new CompanyAccount();
        goodCompanyAccountEur.setCompanyId(3L);
        goodCompanyAccountEur.setEmployeeId(1L);
        goodCompanyAccountEur.setAccountNumber("1233567891231231");
        goodCompanyAccountEur.setReservedAmount(new BigDecimal(1000));
        goodCompanyAccountEur.setAvailableBalance(new BigDecimal(10000));
        goodCompanyAccountEur.setCreationDate(System.currentTimeMillis());
        goodCompanyAccountEur.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        goodCompanyAccountEur.setCurrency(currencyRepository.findById(2L).orElse(null));
        goodCompanyAccountEur.setActive(true);

        CompanyAccount goodCompanyAccountUsd = new CompanyAccount();
        goodCompanyAccountUsd.setCompanyId(3L);
        goodCompanyAccountUsd.setEmployeeId(1L);
        goodCompanyAccountUsd.setAccountNumber("1232227891231231");
        goodCompanyAccountUsd.setReservedAmount(new BigDecimal(1000));
        goodCompanyAccountUsd.setAvailableBalance(new BigDecimal(10000));
        goodCompanyAccountUsd.setCreationDate(System.currentTimeMillis());
        goodCompanyAccountUsd.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        goodCompanyAccountUsd.setCurrency(currencyRepository.findById(3L).orElse(null));
        goodCompanyAccountUsd.setActive(true);

        CompanyAccount badCompanyAccountDin = new CompanyAccount();
        badCompanyAccountDin.setCompanyId(4L);
        badCompanyAccountDin.setEmployeeId(1L);
        badCompanyAccountDin.setAccountNumber("1232457891231231");
        badCompanyAccountDin.setReservedAmount(new BigDecimal(1000));
        badCompanyAccountDin.setAvailableBalance(new BigDecimal(10000));
        badCompanyAccountDin.setCreationDate(System.currentTimeMillis());
        badCompanyAccountDin.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        badCompanyAccountDin.setCurrency(currencyRepository.findById(1L).orElse(null));
        badCompanyAccountDin.setActive(true);

        CompanyAccount badCompanyAccountEur = new CompanyAccount();
        badCompanyAccountEur.setCompanyId(4L);
        badCompanyAccountEur.setEmployeeId(1L);
        badCompanyAccountEur.setAccountNumber("1231677891231231");
        badCompanyAccountEur.setReservedAmount(new BigDecimal(1000));
        badCompanyAccountEur.setAvailableBalance(new BigDecimal(10000));
        badCompanyAccountEur.setCreationDate(System.currentTimeMillis());
        badCompanyAccountEur.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        badCompanyAccountEur.setCurrency(currencyRepository.findById(2L).orElse(null));
        badCompanyAccountEur.setActive(true);

        CompanyAccount badCompanyAccountUsd = new CompanyAccount();
        badCompanyAccountUsd.setCompanyId(4L);
        badCompanyAccountUsd.setEmployeeId(1L);
        badCompanyAccountUsd.setAccountNumber("1233337891222231");
        badCompanyAccountUsd.setReservedAmount(new BigDecimal(1000));
        badCompanyAccountUsd.setAvailableBalance(new BigDecimal(10000));
        badCompanyAccountUsd.setCreationDate(System.currentTimeMillis());
        badCompanyAccountUsd.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        badCompanyAccountUsd.setCurrency(currencyRepository.findById(3L).orElse(null));
        badCompanyAccountUsd.setActive(true);

        if (accountRepository.count() == 0) {
            loadUserAccountData(List.of(jankoRacunDinarski,
                    strahinjaRacunDinarski,
                    jankoRacunEuro,
                    strahinjaRacunEuro,
                    krasicRacunDinarski,
                    krasicRacunEurski,
                    krasicRacunDolarski));
            loadCompanyAccountData(List.of(companyAccount,
                    companyAccount2,
                    companyAccount3,
                    exchangeAccount1,
                    exchangeAccount2,
                    exchangeAccount3,
                    goodCompanyAccountDin,
                    goodCompanyAccountEur,
                    goodCompanyAccountUsd,
                    badCompanyAccountDin,
                    badCompanyAccountEur,
                    badCompanyAccountUsd));
        }

        Card jankoDinarskiKartica = new Card();
        jankoDinarskiKartica.setAccountNumber("1111111111111111");
        jankoDinarskiKartica.setCardNumber("12345678");
        jankoDinarskiKartica.setCardName("Janko dinarski kartica");
        jankoDinarskiKartica.setCVV("123");
        jankoDinarskiKartica.setCreationDate(System.currentTimeMillis());
        jankoDinarskiKartica.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        jankoDinarskiKartica.setActive(true);

        Card jankoEurskiKartica = new Card();
        jankoEurskiKartica.setAccountNumber("1231231231231231");
        jankoEurskiKartica.setCardNumber("12312312");
        jankoEurskiKartica.setCardName("Janko dinarski kartica");
        jankoEurskiKartica.setCVV("456");
        jankoEurskiKartica.setCreationDate(System.currentTimeMillis());
        jankoEurskiKartica.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        jankoEurskiKartica.setActive(true);

        Card strahinjaDinarskiKartica = new Card();
        strahinjaDinarskiKartica.setAccountNumber("2222222222222222");
        strahinjaDinarskiKartica.setCardNumber("87654321");
        strahinjaDinarskiKartica.setCardName("DEBIT");
        strahinjaDinarskiKartica.setCVV("321");
        strahinjaDinarskiKartica.setCreationDate(System.currentTimeMillis());
        strahinjaDinarskiKartica.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        strahinjaDinarskiKartica.setActive(true);

        Card strahinjaEurskiKartica = new Card();
        strahinjaEurskiKartica.setAccountNumber("3213213213213213");
        strahinjaEurskiKartica.setCardNumber("32132132");
        strahinjaEurskiKartica.setCardName("DEBIT");
        strahinjaEurskiKartica.setCVV("789");
        strahinjaEurskiKartica.setCreationDate(System.currentTimeMillis());
        strahinjaEurskiKartica.setExpireDate(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 10);
        strahinjaEurskiKartica.setActive(true);

        if (cardRepository.count() == 0) {
            loadCardData(List.of(jankoDinarskiKartica, jankoEurskiKartica, strahinjaDinarskiKartica, strahinjaEurskiKartica));
        }

        CreditRequest creditRequest = new CreditRequest();
        creditRequest.setUserId(1L);
        creditRequest.setName("KES");
        creditRequest.setAccountNumber("2222222222222222");
        creditRequest.setAmount(new BigDecimal(100000));
        creditRequest.setApplianceReason("Kupovina stana");
        creditRequest.setMonthlyPaycheck(new BigDecimal(1000));
        creditRequest.setEmployed(true);
        creditRequest.setDateOfEmployment(System.currentTimeMillis());
        creditRequest.setPaymentPeriod(100);
        creditRequest.setStatus(CreditRequestStatus.PROCESSING);
        creditRequest.setCurrencyMark("RSD");

        if (creditRequestRepository.count() == 0) {
            loadCreditRequestData(List.of(creditRequest));
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

    private void loadCreditRequestData(List<CreditRequest> creditRequests) {
        creditRequestRepository.saveAll(creditRequests);
    }
}
