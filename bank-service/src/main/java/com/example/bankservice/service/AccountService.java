package com.example.bankservice.service;

import com.example.bankservice.client.EmailServiceClient;
import com.example.bankservice.client.UserServiceClient;
import com.example.bankservice.domain.dto.account.UserAccountCreateDto;
import com.example.bankservice.domain.dto.account.UserAccountDto;
import com.example.bankservice.domain.dto.companyaccount.CompanyAccountCreateDto;
import com.example.bankservice.domain.dto.companyaccount.CompanyAccountDto;
import com.example.bankservice.domain.dto.emailService.TransactionFinishedDto;
import com.example.bankservice.domain.mapper.CompanyAccountMapper;
import com.example.bankservice.domain.mapper.UserAccountMapper;
import com.example.bankservice.domain.model.Card;
import com.example.bankservice.domain.model.accounts.Account;
import com.example.bankservice.domain.model.accounts.CompanyAccount;
import com.example.bankservice.domain.model.accounts.UserAccount;
import com.example.bankservice.repository.AccountRepository;
import com.example.bankservice.repository.CardRepository;
import com.example.bankservice.repository.CompanyAccountRepository;
import com.example.bankservice.repository.UserAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountService {

    private static final String bankAccountRSD = "3333333333333333";
    private static final String bankAccountEUR = "4444444444444444";
    private static final String bankAccountUSD = "5555555555555555";
    private static final String bankAccountCHF = "6666666666666666";
    private static final String bankAccountGBP = "7777777777777777";
    private static final String bankAccountJPY = "8888888888888888";

    private final AccountRepository accountRepository;
    private final UserAccountMapper userAccountMapper;
    private final CardRepository cardRepository;
    private final UserAccountRepository userAccountRepository;
    private final CompanyAccountRepository companyAccountRepository;
    private final CompanyAccountMapper companyAccountMapper;
    private final EmailServiceClient emailServiceClient;
    private final UserServiceClient userServiceClient;

    public List<UserAccountDto> findAllUserAccounts() {
        return userAccountRepository.findAll().stream().filter(Account::isActive)
                .map(userAccountMapper::userAccountToUserAccountDto)
                .collect(Collectors.toList());
    }

    public List<CompanyAccountDto> findAllCompanyAccounts() {
        return companyAccountRepository.findAll().stream().filter(Account::isActive)
                .map(companyAccountMapper::companyAccountToCompanyAccountDto)
                .collect(Collectors.toList());
    }

    public List<UserAccountDto> findUserAccountByUser(Long userId) {
        return accountRepository.findUserAccountByUserId(userId).orElseThrow(() -> new RuntimeException("Not found")).stream()
                .map(userAccountMapper::userAccountToUserAccountDto).collect(Collectors.toList());
    }

    public List<CompanyAccountDto> findCompanyAccountByCompany(Long companyId) {
        return accountRepository.findCompanyAccountByCompanyId(companyId).orElseThrow().stream()
                .map(companyAccountMapper::companyAccountToCompanyAccountDto)
                .collect(Collectors.toList());
    }

    public UserAccountDto findUserAccountByAccountNumber(String accountNumber) {
        return userAccountMapper.userAccountToUserAccountDto((UserAccount) accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found")));
    }


    public UserAccountDto createUserAccount(UserAccountCreateDto userAccountCreateDto) {

        UserAccount userAccount = userAccountMapper.userAccountCreateDtoToUserAccount(userAccountCreateDto);
        userAccount.setAccountNumber(String.valueOf(new BigInteger(53, new Random())));
        userAccount.setCreationDate(System.currentTimeMillis());
        userAccount.setExpireDate(System.currentTimeMillis() + 31556952000L);
        userAccount.setReservedAmount(new BigDecimal(0));
        userAccount.setActive(true);

        createCard(userAccount);
        return userAccountMapper.userAccountToUserAccountDto(accountRepository.save(userAccount));
    }

    public CompanyAccountDto createCompanyAccount(CompanyAccountCreateDto companyAccountCreateDto) {

        CompanyAccount account = companyAccountMapper.companyAccountCreateDtoToCompanyAccount(companyAccountCreateDto);
        account.setAccountNumber(String.valueOf(new BigInteger(53, new Random())));
        account.setCreationDate(System.currentTimeMillis());
        account.setExpireDate(System.currentTimeMillis() + 31556952000L);
        account.setReservedAmount(new BigDecimal(0));
        account.setActive(true);

        createCard(account);
        return companyAccountMapper.companyAccountToCompanyAccountDto(accountRepository.save(account));
    }

    public void reserveFunds(Account account, BigDecimal amount) {
        account.setReservedAmount(account.getReservedAmount().add(amount));
        account.setAvailableBalance(account.getAvailableBalance().subtract(amount));
        accountRepository.save(account);
    }

    public void transferFunds(Account accountFrom, Account accountTo, BigDecimal amount) {
        accountFrom.setReservedAmount(accountFrom.getReservedAmount().subtract(amount));
        accountTo.setAvailableBalance(accountTo.getAvailableBalance().add(amount));
        accountRepository.save(accountFrom);
        accountRepository.save(accountTo);
        if (accountTo instanceof UserAccount) {
            sendFundsRecievedEmail(accountTo, amount);
        }
    }

    public void transferCreditFunds(Account accountFrom, Account accountTo, BigDecimal amount) {
        accountFrom.setAvailableBalance(accountFrom.getAvailableBalance().subtract(amount));
        accountTo.setAvailableBalance(accountTo.getAvailableBalance().add(amount));
        accountRepository.save(accountFrom);
        accountRepository.save(accountTo);
        if (accountTo instanceof UserAccount) {
            sendFundsRecievedEmail(accountTo, amount);
        }
    }

    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        account.setActive(false);
    }

    public boolean checkBalance(String accountNumber, Double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new RuntimeException("Account not found"));
        return account.getAvailableBalance().compareTo(new BigDecimal(amount)) >= 0;
    }

    public Account extractAccountForAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public Account findBankAccountForGivenCurrency(String currencyMark) {
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

    private void createCard(Account userAccount) {
        Card card = new Card();
        card.setAccountNumber(userAccount.getAccountNumber());
        card.setCardNumber(String.valueOf(new BigInteger(53, new Random())));
        card.setCardName("VISA");
        card.setCVV(String.valueOf(new Random().nextInt(999)));
        card.setCreationDate(System.currentTimeMillis());
        card.setExpireDate(System.currentTimeMillis() + 31556952000L);
        card.setActive(true);

        cardRepository.save(card);
    }

    private void sendFundsRecievedEmail(Account account, BigDecimal amount) {
        if (account instanceof UserAccount) {
            String email = userServiceClient.getEmailByUserId(String.valueOf(((UserAccount) account).getUserId())).getEmail();
            emailServiceClient.sendTransactionFinishedEmailToEmailService(new TransactionFinishedDto(email, account.getCurrency().getMark(), amount));
        }
    }
}
