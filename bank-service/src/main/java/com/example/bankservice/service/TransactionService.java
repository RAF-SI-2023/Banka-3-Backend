package com.example.bankservice.service;

import com.example.bankservice.client.EmailServiceClient;
import com.example.bankservice.client.UserServiceClient;
import com.example.bankservice.domain.dto.currencyExchange.CurrencyExchangeDto;
import com.example.bankservice.domain.dto.transaction.*;
import com.example.bankservice.domain.mapper.TransactionMapper;
import com.example.bankservice.domain.model.Transaction;
import com.example.bankservice.domain.model.accounts.Account;
import com.example.bankservice.domain.model.accounts.CompanyAccount;
import com.example.bankservice.domain.model.accounts.UserAccount;
import com.example.bankservice.domain.model.enums.TransactionStatus;
import com.example.bankservice.domain.model.enums.TransactionType;
import com.example.bankservice.domain.model.marginAccounts.CompanyMarginAccount;
import com.example.bankservice.domain.model.marginAccounts.MarginAccount;
import com.example.bankservice.domain.model.marginAccounts.UserMarginAccount;
import com.example.bankservice.repository.AccountRepository;
import com.example.bankservice.repository.MarginAccountRepository;
import com.example.bankservice.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionService {

    private final UserServiceClient userServiceClient;
    private final EmailServiceClient emailServiceClient;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;
    private final MarginAccountRepository marginAccountRepository;
    private TransactionRepository transactionRepository;
    private AccountService accountService;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public StartPaymentTransactionDto startPaymentTransaction(PaymentTransactionDto paymentTransactionDto) {
        Account accountFrom = accountService.extractAccountForAccountNumber(paymentTransactionDto.getAccountFrom());
        Account accountTo = accountService.extractAccountForAccountNumber(paymentTransactionDto.getAccountTo());

        if (!accountService.checkBalance(paymentTransactionDto.getAccountFrom(), paymentTransactionDto.getAmount())) {
            throw new RuntimeException("Insufficient funds");
        }
        
        checkIfAccountsAreTheSame(accountFrom, accountTo);

        Long transactionId = 0L;
        if (accountFrom.getCurrency().getMark().equals(accountTo.getCurrency().getMark())) {
            transactionId = startSameCurrencyPaymentTransaction(paymentTransactionDto, accountFrom, accountTo);
        } else {
            throw new RuntimeException("Different currency transactions are not supported");
        }

        return new StartPaymentTransactionDto(transactionId);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void confirmPaymentTransaction(ConfirmPaymentTransactionDto confirmPaymentTransactionDto) {
        Transaction transaction = transactionRepository.findById(confirmPaymentTransactionDto.getTransactionId())
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (transaction.getTransactionStatus().equals(TransactionStatus.PENDING)) {
            acceptTransaction(transaction);
            Account accountFrom = accountService.extractAccountForAccountNumber(transaction.getAccountFrom());

            accountService.reserveFunds(accountFrom, transaction.getAmount());
        } else {
            throw new RuntimeException("Transaction already completed");
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void startCurrencyExchangeTransaction(CurrencyExchangeDto currencyExchangeDto) {
        Account accountFrom = accountService.extractAccountForAccountNumber(currencyExchangeDto.getAccountFrom());
        Account accountTo = accountService.extractAccountForAccountNumber(currencyExchangeDto.getAccountTo());

        checkIfAccountsAreTheSame(accountFrom, accountTo);
        
        if (!accountService.checkBalance(currencyExchangeDto.getAccountFrom(), currencyExchangeDto.getAmount())) {
            throw new RuntimeException("Insufficient funds");
        }

        if (accountFrom.getCurrency().getMark().equals(accountTo.getCurrency().getMark())) {

        } else {
            throw new RuntimeException("Different currency transactions are not supported");
        }

    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void stockBuyTransaction(StockTransactionDto stockTransactionDto) {
        // ovde umesto da trazi bank account, stavimo proveru da li StokcTransactionDto ima userId ili companyId
        // i na osnovu njihovog id-a i marka nadjemo account

        Account accountFrom = null;
        if (stockTransactionDto.getEmployeeId() != null) {
            accountFrom = accountService.findBankAccountForGivenCurrency(stockTransactionDto.getCurrencyMark());
        } else {
            accountFrom = accountService.findAccount(stockTransactionDto);
        }
        Account accountTo = accountService.findExchangeAccountForGivenCurrency(stockTransactionDto.getCurrencyMark());

        checkIfAccountsAreTheSame(accountFrom, accountTo);

        if (accountFrom.getAvailableBalance().compareTo(BigDecimal.valueOf(stockTransactionDto.getAmount())) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        Transaction transaction = new Transaction();
        transaction.setAccountFrom(accountFrom.getAccountNumber());
        transaction.setAccountTo(accountTo.getAccountNumber());
        transaction.setAmount(BigDecimal.valueOf(stockTransactionDto.getAmount()));
        transaction.setType(TransactionType.STOCK_TRANSACTION);
        transaction.setTransactionStatus(TransactionStatus.ACCEPTED);
        transaction.setDate(System.currentTimeMillis());

        transactionRepository.save(transaction);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void stockBuyMarginTransaction(StockMarginTransactionDto stockMarginTransactionDto) {

        MarginAccount marginAccount = accountService.findMarginAccount(stockMarginTransactionDto);
        if(!marginAccount.isActive()){
            throw new RuntimeException("Margin account is not active");
        }

        //I -skidanje sa racuna banke
        Double forBank=stockMarginTransactionDto.getAmount()*(marginAccount.getBankParticipation());


        //II
        Double forAccount=stockMarginTransactionDto.getAmount()-forBank;
        System.out.println("FOR ACCOUNT: "+forAccount + " FOR BANK: "+forBank + " AMOUNT: "+stockMarginTransactionDto.getAmount());

        if (marginAccount.getInitialMargin().compareTo(BigDecimal.valueOf(forAccount)) < 0) {
            throw new RuntimeException("Insufficient funds");
        }
        //TODO staviti dole u finishTransaction
        //marginAccount.setInitialMargin(marginAccount.getInitialMargin().subtract(BigDecimal.valueOf(forAccount)));
        //Deaktivacija: Ako padnemo ispod
        if(marginAccount.getInitialMargin().compareTo(marginAccount.getMaintenanceMargin())==-1){
            marginAccount.setActive(false);
        }

        /**prva transackija je bank->exchange, druga transakcija je margin->exchange */

        //Skidanje sa racuna banke
        Account exchangeAccount = accountService.findExchangeAccountForGivenCurrency("RSD");
        Account bankAccount = accountService.findBankAccountForGivenCurrency("RSD");

        //bank->exchange
        Transaction bankToExchange = new Transaction();
        bankToExchange.setAccountFrom(bankAccount.getAccountNumber());
        bankToExchange.setAccountTo(exchangeAccount.getAccountNumber());
        bankToExchange.setAmount(BigDecimal.valueOf(forBank));
        bankToExchange.setType(TransactionType.STOCK_TRANSACTION);
        bankToExchange.setTransactionStatus(TransactionStatus.ACCEPTED);
        bankToExchange.setDate(System.currentTimeMillis());
        transactionRepository.save(bankToExchange);

        //margin->exchange
        Transaction marginToExchange = new Transaction();
        marginToExchange.setAccountFrom(marginAccount.getAccountNumber());
        marginToExchange.setAccountTo(exchangeAccount.getAccountNumber());
        marginToExchange.setAmount(BigDecimal.valueOf(forAccount));
        marginToExchange.setType(TransactionType.MARGIN_TRANSACTION);
        marginToExchange.setTransactionStatus(TransactionStatus.ACCEPTED);
        marginToExchange.setDate(System.currentTimeMillis());
        transactionRepository.save(marginToExchange);

        marginAccount.getLoanValue().add(BigDecimal.valueOf(forBank));
        marginAccountRepository.save(marginAccount);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void addToMarginUser(AddToMarginDto dto,Long userId){
        //Provera da li imamo novca na tekucem RSD racunu
        if(!accountService.checkBalanceUser(userId,dto.getAmount())){
            throw new RuntimeException("Insufficient funds");
        }

        Optional<UserMarginAccount> optional = marginAccountRepository.findUserMarginAccountByUserId(userId);
        if(optional.isEmpty()){
            throw new RuntimeException("Margin account not found");
        }
        UserMarginAccount account = optional.get();

        //Dodajemo na marzni
        if(!account.isActive()){
            Double checkInitialMargin = dto.getAmount() + account.getInitialMargin().doubleValue();
            if(checkInitialMargin >= account.getMaintenanceMargin().doubleValue()){
                account.setActive(true);
            }
        }

        //Skidamo sa tekuceg
        Account userRSDAccount=accountService.findUserAccountForIdAndCurrency(userId,"RSD");

        Transaction transaction = new Transaction();
        transaction.setAccountFrom(userRSDAccount.getAccountNumber());
        transaction.setAccountTo(account.getAccountNumber());
        transaction.setAmount(BigDecimal.valueOf(dto.getAmount()));
        transaction.setType(TransactionType.MARGIN_TRANSACTION);
        transaction.setTransactionStatus(TransactionStatus.ACCEPTED);
        transaction.setDate(System.currentTimeMillis());
        transactionRepository.save(transaction);

        marginAccountRepository.save(account);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void withdrawFromMarginUser(WithdrawFromMarginDto dto,Long userId){
        Optional<UserMarginAccount> optional = marginAccountRepository.findUserMarginAccountByUserId(userId);
        if(optional.isEmpty()){
            throw new RuntimeException("Margin account not found");
        }
        UserMarginAccount account = optional.get();

        if(!account.isActive()){
            throw new RuntimeException("Margin account is not active");
        }

        if(account.getInitialMargin().compareTo(BigDecimal.valueOf(dto.getAmount())) < 0){
            throw new RuntimeException("Insufficient funds");
        }
        //Ako bismo otisli isbog Maitenance
        Double checkInitialMargin = account.getInitialMargin().doubleValue() - dto.getAmount();
        if(checkInitialMargin >= 0){
            if(checkInitialMargin < account.getMaintenanceMargin().doubleValue()){
                account.setActive(false);
            }
        }else {
            throw new RuntimeException("Insufficient funds");
        }

        //transakcija margin->user
        Account userRSDAccount=accountService.findUserAccountForIdAndCurrency(userId,"RSD");

        Transaction transaction = new Transaction();
        transaction.setAccountFrom(account.getAccountNumber());
        transaction.setAccountTo(userRSDAccount.getAccountNumber());
        transaction.setAmount(BigDecimal.valueOf(dto.getAmount()));
        transaction.setType(TransactionType.MARGIN_TRANSACTION);
        transaction.setTransactionStatus(TransactionStatus.ACCEPTED);
        transaction.setDate(System.currentTimeMillis());
        transactionRepository.save(transaction);

        marginAccountRepository.save(account);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void addToMarginCompany(AddToMarginDto dto,Long companyId){
        //Provera da li imamo novca na tekucem RSD racunu
        if(!accountService.checkBalanceCompany(companyId,dto.getAmount())){
            throw new RuntimeException("Insufficient funds");
        }

        Optional<CompanyMarginAccount> optional = marginAccountRepository.findCompanyMarginAccountByCompanyId(companyId);
        if(optional.isEmpty()){
            throw new RuntimeException("Margin account not found");
        }
        MarginAccount account = optional.get();

        //Dodajemo na marzni
        if(!account.isActive()){
            Double checkInitialMargin = dto.getAmount() + account.getInitialMargin().doubleValue();
            if(checkInitialMargin >= account.getMaintenanceMargin().doubleValue()){
                account.setActive(true);
            }
        }

        //Skidamo sa tekuceg
        Account companyRSDAccount=accountService.findCompanyAccountForIdAndCurrency(companyId,"RSD");

        Transaction transaction = new Transaction();
        transaction.setAccountFrom(companyRSDAccount.getAccountNumber());
        transaction.setAccountTo(account.getAccountNumber());
        transaction.setAmount(BigDecimal.valueOf(dto.getAmount()));
        transaction.setType(TransactionType.MARGIN_TRANSACTION);
        transaction.setTransactionStatus(TransactionStatus.ACCEPTED);
        transaction.setDate(System.currentTimeMillis());
        transactionRepository.save(transaction);

        marginAccountRepository.save(account);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void withdrawFromMarginCompany(WithdrawFromMarginDto dto,Long companyId){
        Optional<CompanyMarginAccount> optional = marginAccountRepository.findCompanyMarginAccountByCompanyId(companyId);
        if(optional.isEmpty()){
            throw new RuntimeException("Margin account not found");
        }
        CompanyMarginAccount account = optional.get();

        if(!account.isActive()){
            throw new RuntimeException("Margin account is not active");
        }

        if(account.getInitialMargin().compareTo(BigDecimal.valueOf(dto.getAmount())) < 0){
            throw new RuntimeException("Insufficient funds");
        }

        //Ako bismo otisli isbog Maitenance
        Double checkInitialMargin = account.getInitialMargin().doubleValue() - dto.getAmount();
        if(checkInitialMargin >= 0){
            if(checkInitialMargin < account.getMaintenanceMargin().doubleValue()){
                account.setActive(false);
            }
        }else {
            throw new RuntimeException("Insufficient funds");
        }

        //transakcija margin->company
        Account companyRSDAccount=accountService.findCompanyAccountForIdAndCurrency(companyId,"RSD");

        Transaction transaction = new Transaction();
        transaction.setAccountFrom(account.getAccountNumber());
        transaction.setAccountTo(companyRSDAccount.getAccountNumber());
        transaction.setAmount(BigDecimal.valueOf(dto.getAmount()));
        transaction.setType(TransactionType.MARGIN_TRANSACTION);
        transaction.setTransactionStatus(TransactionStatus.ACCEPTED);
        transaction.setDate(System.currentTimeMillis());
        transactionRepository.save(transaction);

        marginAccountRepository.save(account);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void stockSellMarginTransaction(StockMarginTransactionDto dto) {
        MarginAccount marginAccount = accountService.findMarginAccount(dto);
        if(!marginAccount.isActive()){
            throw new RuntimeException("Margin account is not active");
        }
        //Prvo skidamo dug prema banci
        Double takeFromAccGiveToBank=dto.getAmount()*(marginAccount.getBankParticipation());


        /** dve transakcije, prva je exchange->bank, druga je exchange->margin */


        Account exchangeaccount = accountService.findExchangeAccountForGivenCurrency("RSD");
        Account bankAccount = accountService.findBankAccountForGivenCurrency("RSD");

        Transaction exchangeToBank = new Transaction();
        exchangeToBank.setAccountFrom(exchangeaccount.getAccountNumber());
        exchangeToBank.setAccountTo(bankAccount.getAccountNumber());
        exchangeToBank.setAmount(BigDecimal.valueOf(takeFromAccGiveToBank));
        exchangeToBank.setType(TransactionType.STOCK_TRANSACTION);
        exchangeToBank.setTransactionStatus(TransactionStatus.ACCEPTED);
        exchangeToBank.setDate(System.currentTimeMillis());
        transactionRepository.save(exchangeToBank);

        Transaction exchangeToMargin = new Transaction();
        exchangeToMargin.setAccountFrom(exchangeaccount.getAccountNumber());
        exchangeToMargin.setAccountTo(marginAccount.getAccountNumber());
        exchangeToMargin.setAmount(BigDecimal.valueOf(dto.getAmount()-takeFromAccGiveToBank));
        exchangeToMargin.setType(TransactionType.MARGIN_TRANSACTION);
        exchangeToMargin.setTransactionStatus(TransactionStatus.ACCEPTED);
        exchangeToMargin.setDate(System.currentTimeMillis());
        transactionRepository.save(exchangeToMargin);

        marginAccount.setLoanValue(marginAccount.getLoanValue().subtract(BigDecimal.valueOf(takeFromAccGiveToBank)));
        marginAccountRepository.save(marginAccount);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void stockSellTransaction(StockTransactionDto stockTransactionDto) {
        Account accountFrom = accountService.findExchangeAccountForGivenCurrency(stockTransactionDto.getCurrencyMark());
        //ovde umesto da trazi bank account, stavimo proveru da li StokcTransactionDto ima userId ili companyId
        // i na osnovu njihovog id-a i marka nadjemo account
        Account accountTo = null;
        if(stockTransactionDto.getEmployeeId() != null){
            accountTo = accountService.findBankAccountForGivenCurrency(stockTransactionDto.getCurrencyMark());
        } else {
            accountTo = accountService.findAccount(stockTransactionDto);
        }
        checkIfAccountsAreTheSame(accountFrom, accountTo);
        if (accountFrom.getAvailableBalance().compareTo(BigDecimal.valueOf(stockTransactionDto.getAmount())) < 0) {
            throw new RuntimeException("Insufficient funds");
        }
        if(stockTransactionDto.getTax() > 0.0) {
            Account bankAccount = accountService.findBankAccountForGivenCurrency(stockTransactionDto.getCurrencyMark());
            if(!accountTo.getAccountNumber().equals(bankAccount.getAccountNumber()))
                startPayTax(accountTo, bankAccount, stockTransactionDto.getTax());
        }
        Transaction transaction = new Transaction();
        transaction.setAccountFrom(accountFrom.getAccountNumber());
        transaction.setAccountTo(accountTo.getAccountNumber());
        transaction.setAmount(BigDecimal.valueOf(stockTransactionDto.getAmount()));
        transaction.setType(TransactionType.STOCK_TRANSACTION);
        transaction.setTransactionStatus(TransactionStatus.ACCEPTED);
        transaction.setDate(System.currentTimeMillis());
        transactionRepository.save(transaction);
    }


    public List<CreditTransactionDto> getAllCreditTransactions() {
        List<Transaction> transactions = transactionRepository.findAllByType(TransactionType.CREDIT_APPROVE_TRANSACTION)
                .orElseThrow(() -> new RuntimeException("Transactions not found"));

        return transactions.stream().map(transactionMapper::transactionToCreditTransactionDto).toList();
    }

    public List<MarginTransactionDto> getAllMarginTransactions(String accountNumber) {
        List<Transaction> transactions = transactionRepository.findByAccountFromOrAccountToAndType(accountNumber, accountNumber, TransactionType.MARGIN_TRANSACTION)
                .orElseThrow(() -> new RuntimeException("Transactions not found"));

        return transactions.stream()
                .sorted((t1, t2) -> t2.getDate().compareTo(t1.getDate())) // Sortiranje po datumu, najnoviji prvo
                .map(transactionMapper::transactionToMarginTransactionDto)
                .collect(Collectors.toList());
    }

    public List<FinishedPaymentTransactionDto> getAllPaymentTransactions(String accountNumber) {
//        List<Transaction> transactions =
//                transactionRepository.findByAccountFromOrAccountToAndType(accountNumber,
//                                accountNumber, TransactionType.PAYMENT_TRANSACTION)
//                .orElseThrow(() -> new RuntimeException("Transactions not found"));

        List<Transaction> transactions =
                transactionRepository.findByAccountFromOrAccountTo(accountNumber,
                                accountNumber)
                        .orElseThrow(() -> new RuntimeException("Transactions not found"));

        Collections.sort(transactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction t1, Transaction t2) {
                return t2.getDate().compareTo(t1.getDate());
            }
        });

        return transactions.stream().filter(transaction -> transaction.getTransactionStatus() == TransactionStatus.FINISHED).map(transactionMapper::transactionToFinishedPaymentTransactionDto).toList();
    }
    
    public void otcUserTransaction(UserOtcTransactionDto userOtcTransactionDto) {
        Account accountFrom = accountService.findUserAccountForIdAndCurrency(
                userOtcTransactionDto.getUserFromId(), "RSD");
        Account accountTo = accountService.findUserAccountForIdAndCurrency(
                userOtcTransactionDto.getUserToId(), "RSD");
        
        checkIfAccountsAreTheSame(accountFrom, accountTo);

        if(userOtcTransactionDto.getTax() > 0.0) {
            Account bankAccount = accountService.findBankAccountForGivenCurrency("RSD");
            startPayTax(accountTo, bankAccount, userOtcTransactionDto.getTax());
        }

        startOTCTransaction(accountFrom, accountTo, userOtcTransactionDto.getAmount());
    }
    
    public void otcCompanyTransaction(CompanyOtcTransactionDto companyOtcTransactionDto) {
        Account accountFrom = accountService.findCompanyAccountForIdAndCurrency(
                companyOtcTransactionDto.getCompanyFromId(), "RSD");
        Account accountTo = accountService.findCompanyAccountForIdAndCurrency(
                companyOtcTransactionDto.getCompanyToId(), "RSD");
        
        checkIfAccountsAreTheSame(accountFrom, accountTo);
        //todo payTax(accTO, bankACC, tax)
        if(companyOtcTransactionDto.getTax() > 0.0) {
            Account bankAccount = accountService.findBankAccountForGivenCurrency("RSD");
            if(!accountTo.getAccountNumber().equals(bankAccount.getAccountNumber()))
                startPayTax(accountTo, bankAccount, companyOtcTransactionDto.getTax());
        }

        startOTCTransaction(accountFrom, accountTo, companyOtcTransactionDto.getAmount());
    }

    public void otcBank4Transaction(CompanyOtcTransactionDto companyOtcTransactionDto){
        Account accountFrom = accountService.findCompanyAccountForIdAndCurrency(
                companyOtcTransactionDto.getCompanyFromId(), "RSD");
        Account accountTo = accountService.findCompanyAccountForIdAndCurrency(
                companyOtcTransactionDto.getCompanyToId(), "RSD");

        checkIfAccountsAreTheSame(accountFrom, accountTo);

        startOTCTransaction(accountFrom, accountTo, companyOtcTransactionDto.getAmount());
    }

    private Long startSameCurrencyPaymentTransaction(PaymentTransactionDto paymentTransactionDto,
                                                     Account accountFrom,
                                                     Account accountTo) {
        Transaction transaction = transactionMapper.paymentTransactionDtoToTransaction(paymentTransactionDto);
        transaction.setDate(System.currentTimeMillis());
        transaction.setTransactionStatus(TransactionStatus.PENDING);
        transaction.setType(TransactionType.PAYMENT_TRANSACTION);
        transaction = transactionRepository.save(transaction);

        String email = (accountFrom instanceof UserAccount) ?
                userServiceClient.getEmailByUserId(String.valueOf(((UserAccount) accountFrom).getUserId())).getEmail() :
                userServiceClient.getEmailByCompanyId(String.valueOf(((CompanyAccount) accountFrom).getCompanyId()));

        emailServiceClient.sendTransactionActivationEmailToEmailService(new PaymentTransactionActivationDto(email,
                transaction.getTransactionId()));

        return transaction.getTransactionId();
    }

    private void startPayTax(Account accountFrom, Account accountTo, Double tax) {
        Transaction transaction = new Transaction();
        transaction.setAccountFrom(accountFrom.getAccountNumber());
        transaction.setAccountTo(accountTo.getAccountNumber());
        transaction.setAmount(BigDecimal.valueOf(tax));
        transaction.setType(TransactionType.PAY_TAX_TRANSACTION);
        transaction.setTransactionStatus(TransactionStatus.ACCEPTED);
        transaction.setDate(System.currentTimeMillis());
        transactionRepository.save(transaction);
    }
    
    private void startOTCTransaction(Account accountFrom, Account accountTo, Double amount) {
        Transaction transaction = new Transaction();
        transaction.setAccountFrom(accountFrom.getAccountNumber());
        transaction.setAccountTo(accountTo.getAccountNumber());
        transaction.setAmount(BigDecimal.valueOf(amount));
        transaction.setType(TransactionType.OTC_TRANSACTION);
        transaction.setTransactionStatus(TransactionStatus.ACCEPTED);
        transaction.setDate(System.currentTimeMillis());
        transactionRepository.save(transaction);
    }

    @Scheduled(fixedRate = 30000) // Postavljanje cron izraza da se metoda izvrsava svakih 5 minuta
    public void processTransactions() {

        Optional<List<Transaction>> optionalTransactions = transactionRepository.findAllByTransactionStatus(TransactionStatus.ACCEPTED);
        List<Transaction> transactions;
        if (!optionalTransactions.isPresent()) return;
        transactions = optionalTransactions.get();
        for (Transaction transaction : transactions) {
            if (transaction.getType().equals(TransactionType.CREDIT_APPROVE_TRANSACTION)) {
                finishCreditTransaction(transaction);
            } else if (transaction.getType().equals(TransactionType.PAYMENT_TRANSACTION)) {
                finishTransaction(transaction);
            } else if (transaction.getType().equals(TransactionType.STOCK_TRANSACTION)) {
                finishStockTransaction(transaction);
            } else if (transaction.getType().equals(TransactionType.OTC_TRANSACTION)) {
                finishOTCTransaction(transaction);
            }else if(transaction.getType().equals(TransactionType.PAY_TAX_TRANSACTION)) {
                finishTaxTransaction(transaction);
            }else if(transaction.getType().equals(TransactionType.MARGIN_TRANSACTION)) {
                finishMarginTransaction(transaction);
            }
        }
    }

    private void finishTaxTransaction(Transaction transaction) {
        Account accountFrom = accountRepository.findByAccountNumber(transaction.getAccountFrom())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        Account accountTo = accountRepository.findByAccountNumber(transaction.getAccountTo())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        accountService.transferOtcFunds(accountFrom, accountTo, transaction.getAmount());
        transaction.setTransactionStatus(TransactionStatus.FINISHED);
        transactionRepository.save(transaction);
    }

    private void finishTransaction(Transaction transaction) {
        Account accountFrom = accountRepository.findByAccountNumber(transaction.getAccountFrom())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        Account accountTo = accountRepository.findByAccountNumber(transaction.getAccountTo())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        accountService.transferFunds(accountFrom, accountTo, transaction.getAmount());
        transaction.setTransactionStatus(TransactionStatus.FINISHED);
        transactionRepository.save(transaction);
    }
    private void finishMarginTransaction(Transaction transaction) {
        Optional<MarginAccount> marginOptional = marginAccountRepository.findByAccountNumber(transaction.getAccountFrom());

        MarginAccount marginAccount=null;
        if(marginOptional.isPresent()){
            /** PRVI SLUCAJ AKO KUPUJEMO */
            marginAccount=marginOptional.get();
            Account accountTo = accountRepository.findByAccountNumber(transaction.getAccountTo())
                    .orElseThrow(() -> new RuntimeException("Account not found"));

            accountService.transferFromMarginFunds(marginAccount, accountTo, transaction.getAmount());
            transaction.setTransactionStatus(TransactionStatus.FINISHED);

        }else if(marginAccountRepository.findByAccountNumber(transaction.getAccountTo()).isPresent()){
            /** DRUGI SLUCAJ AKO PRODAJEMO */

            marginAccount=marginAccountRepository.findByAccountNumber(transaction.getAccountTo()).get();
            Account accountFrom = accountRepository.findByAccountNumber(transaction.getAccountFrom())
                    .orElseThrow(() -> new RuntimeException("Account not found"));

            accountService.transferToMarginFunds(accountFrom, marginAccount, transaction.getAmount());
            transaction.setTransactionStatus(TransactionStatus.FINISHED);

        }else {
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            new RuntimeException("Margin account not found");
        }

        transactionRepository.save(transaction);
    }

    private void finishCreditTransaction(Transaction transaction) {
        Account accountFrom = accountRepository.findByAccountNumber(transaction.getAccountFrom())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        Account accountTo = accountRepository.findByAccountNumber(transaction.getAccountTo())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        accountService.transferCreditFunds(accountFrom, accountTo, transaction.getAmount());
        transaction.setTransactionStatus(TransactionStatus.FINISHED);
        transactionRepository.save(transaction);
    }

    private void finishStockTransaction(Transaction transaction) {
        Account accountFrom = accountRepository.findByAccountNumber(transaction.getAccountFrom())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        Account accountTo = accountRepository.findByAccountNumber(transaction.getAccountTo())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        accountService.transferStockFunds(accountFrom, accountTo, transaction.getAmount());
        transaction.setTransactionStatus(TransactionStatus.FINISHED);
        transactionRepository.save(transaction);
    }


    private void finishOTCTransaction(Transaction transaction) {
        Account accountFrom = accountRepository.findByAccountNumber(transaction.getAccountFrom())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        Account accountTo = accountRepository.findByAccountNumber(transaction.getAccountTo())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        accountService.transferOtcFunds(accountFrom, accountTo, transaction.getAmount());
        transaction.setTransactionStatus(TransactionStatus.FINISHED);
        transactionRepository.save(transaction);
    }

    private void acceptTransaction(Transaction transaction) {
        transaction.setTransactionStatus(TransactionStatus.ACCEPTED);
        transactionRepository.save(transaction);
    }
    
    private void checkIfAccountsAreTheSame(Account accountFrom, Account accountTo) {
        if (accountFrom.getAccountNumber().equals(accountTo.getAccountNumber())) {
            throw new RuntimeException("Accounts are the same");
        }
    }
}
