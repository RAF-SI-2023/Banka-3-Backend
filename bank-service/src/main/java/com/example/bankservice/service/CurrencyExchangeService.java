package com.example.bankservice.service;

import com.example.bankservice.domain.dto.currencyExchange.CurrencyExchangeDto;
import com.example.bankservice.domain.model.CurrencyExchange;
import com.example.bankservice.domain.model.accounts.Account;
import com.example.bankservice.domain.model.accounts.UserAccount;
import com.example.bankservice.repository.AccountRepository;
import com.example.bankservice.repository.CurrencyExchangeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Service
@AllArgsConstructor
public class CurrencyExchangeService {

    private static final String API_KEY = "96aa86545baf8162d6ecbe21";
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/pair/";
    private static final String bankAccountRSD = "3333333333333333";
    private static final String bankAccountEUR = "4444444444444444";
    private static final String bankAccountUSD = "5555555555555555";
    private static final String bankAccountCHF = "6666666666666666";
    private static final String bankAccountGBP = "7777777777777777";
    private static final String bankAccountJPY = "8888888888888888";
    private final CurrencyExchangeRepository currencyExchangeRepository;
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    @Transactional()
    public void startCurrencyExchangeTransaction(CurrencyExchangeDto currencyExchangeDto) {
        Account accountFrom = accountService.extractAccountForAccountNumber(currencyExchangeDto.getAccountFrom());
        Account accountTo = accountService.extractAccountForAccountNumber(currencyExchangeDto.getAccountTo());

        if (!accountService.checkBalance(currencyExchangeDto.getAccountFrom(), currencyExchangeDto.getAmount()) ||
                currencyExchangeDto.getAmount() == 0) {
            throw new RuntimeException("Insufficient funds");
        }

        Account bankAccountReciever = findBankAccountForGivenCurrency(accountFrom.getCurrency().getMark());
        Account bankAccountSender = findBankAccountForGivenCurrency(accountTo.getCurrency().getMark());

        BigDecimal commission = BigDecimal.valueOf(currencyExchangeDto.getAmount()).multiply(new BigDecimal("0.05"));
        BigDecimal convertedAmount;
        try {
            convertedAmount = convertCurrency(accountFrom.getCurrency().getMark(),
                    accountTo.getCurrency().getMark(),
                    currencyExchangeDto.getAmount() - commission.doubleValue());
            if (convertedAmount == null || convertedAmount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("Currency conversion failed");
            }
        } catch (Exception e) {
            throw new RuntimeException("Currency conversion failed");
        }

        accountFrom.setAvailableBalance(accountFrom.getAvailableBalance()
                .subtract(BigDecimal.valueOf(currencyExchangeDto.getAmount())));
        bankAccountReciever.setAvailableBalance(bankAccountReciever.getAvailableBalance()
                .add(BigDecimal.valueOf(currencyExchangeDto.getAmount())));


        bankAccountSender.setAvailableBalance(bankAccountSender
                .getAvailableBalance().subtract(convertedAmount));
        accountTo.setAvailableBalance(accountTo.getAvailableBalance().add(convertedAmount));

        accountRepository.saveAll(List.of(accountFrom, accountTo, bankAccountReciever, bankAccountSender));
        currencyExchangeRepository.save(new CurrencyExchange(0L,
                accountFrom.getAccountNumber(),
                accountTo.getAccountNumber(),
                BigDecimal.valueOf(currencyExchangeDto.getAmount()),
                new BigDecimal("0.05").multiply(BigDecimal.valueOf(currencyExchangeDto.getAmount())),
                accountFrom.getCurrency().getMark()));
    }

    private Account findBankAccountForGivenCurrency(String currencyMark) {
        switch (currencyMark) {
            case "RSD":
                return accountRepository.findByAccountNumber(bankAccountRSD)
                        .orElseThrow(() -> new RuntimeException("Bank account not found"));
            case "EUR":
                return accountRepository.findByAccountNumber(bankAccountEUR)
                        .orElseThrow(() -> new RuntimeException("Bank account not found"));
            case "USD":
                return accountRepository.findByAccountNumber(bankAccountUSD)
                        .orElseThrow(() -> new RuntimeException("Bank account not found"));
            case "CHF":
                return accountRepository.findByAccountNumber(bankAccountCHF)
                        .orElseThrow(() -> new RuntimeException("Bank account not found"));
            case "GBP":
                return accountRepository.findByAccountNumber(bankAccountGBP)
                        .orElseThrow(() -> new RuntimeException("Bank account not found"));
            case "JPY":
                return accountRepository.findByAccountNumber(bankAccountJPY)
                        .orElseThrow(() -> new RuntimeException("Bank account not found"));
            default:
                throw new RuntimeException("Bank account not found");
        }
    }

    private BigDecimal convertCurrency(String fromCurrency, String toCurrency, Double amount) {
        fromCurrency = fromCurrency.toUpperCase();
        toCurrency = toCurrency.toUpperCase();
        try {
            String urlStr = API_URL + fromCurrency + "/" + toCurrency + "/" + amount;
            URL url = new URL(urlStr);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            String jsonResponse = response.toString();
            BigDecimal conversionRate = new BigDecimal(String.valueOf(parseJsonResponse(jsonResponse)));


            BigDecimal convertedAmount = BigDecimal.valueOf(amount).multiply(conversionRate);

            return convertedAmount;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private BigDecimal parseJsonResponse(String jsonResponse) {

        String conversionRateKey = "\"conversion_rate\":";
        int startIndex = jsonResponse.indexOf(conversionRateKey);
        if (startIndex != -1) {
            int endIndex = jsonResponse.indexOf(",", startIndex + conversionRateKey.length());
            if (endIndex != -1) {
                String rateString = jsonResponse.substring(startIndex + conversionRateKey.length(), endIndex);
                return new BigDecimal(rateString.trim());
            }
        }
        return null;
    }
}
