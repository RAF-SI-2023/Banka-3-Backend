package com.example.bankservice.bootstrap;

import com.example.bankservice.domain.model.Account;
import com.example.bankservice.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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
    public void run(String... args) throws Exception {
        if (accountRepository.count() == 0) {
            loadAccountData();
        }

    }

    private void loadAccountData() {
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

        accountRepository.save(account);
    }
}
