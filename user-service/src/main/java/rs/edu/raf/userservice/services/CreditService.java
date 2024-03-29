package rs.edu.raf.userservice.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.edu.raf.userservice.domains.dto.credit.CreateCreditDto;
import rs.edu.raf.userservice.domains.dto.credit.CreditDto;
import rs.edu.raf.userservice.domains.dto.credit.CreditTransactionDto;
import rs.edu.raf.userservice.domains.mappers.CreditMapper;
import rs.edu.raf.userservice.domains.model.Account;
import rs.edu.raf.userservice.domains.model.Credit;
import rs.edu.raf.userservice.domains.model.User;
import rs.edu.raf.userservice.repositories.AccountRepository;
import rs.edu.raf.userservice.repositories.CreditRepository;
import rs.edu.raf.userservice.repositories.UserRepository;
import rs.edu.raf.userservice.utils.BankServiceClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreditService {

    private final CreditRepository creditRepository;

    private final UserRepository userRepository;

    private final AccountRepository accountRepository;

    private BankServiceClient bankServiceClient;

    public CreditService(CreditRepository creditRepository, UserRepository userRepository,
                         AccountRepository accountRepository, BankServiceClient bankServiceClient) {
        this.creditRepository = creditRepository;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.bankServiceClient = bankServiceClient;
    }

    public List<CreditDto> findAll() {
        return creditRepository.findAll().stream().map(CreditMapper.INSTANCE::creditToCreditDto)
                .collect(Collectors.toList());
    }

    public List<CreditDto> findAllUserCredits(Long userId) {
        List<Credit> credits = creditRepository.findByUser_UserId(userId).orElseThrow();
        return credits.stream().map(CreditMapper.INSTANCE::creditToCreditDto)
                .collect(Collectors.toList());
    }

    public CreditDto findById(Long id) {
        return creditRepository.findById(id).map(CreditMapper.INSTANCE::creditToCreditDto).orElseThrow();
    }

    public CreditDto createCredit(CreateCreditDto createCreditDto) {
        Credit credit = CreditMapper.INSTANCE.createCreditDtoToCredit(createCreditDto);
        User user = userRepository.findById(createCreditDto.getUserId()).orElseThrow();
        credit.setUser(user);
        credit.setFee(new BigDecimal(0.05));
        credit.setStartDate(System.currentTimeMillis());
        credit.setEndDate(calculateEndDate(credit.getStartDate(), credit.getPaymentPeriod()));
        credit.setMonthlyFee((credit.getAmount().multiply(new BigDecimal(1).add(credit.getFee()))).divide(new BigDecimal(credit.getPaymentPeriod())));
//        credit.setMonthlyFee((credit.getAmount() * (1 + credit.getFee())) / credit.getPaymentPeriod());
        credit.setRemainingAmount(credit.getAmount().multiply(new BigDecimal(1).add(credit.getFee())));
//        credit.setRemainingAmount(credit.getAmount() * (1 + credit.getFee()));
        credit.setAccountNumber(createCreditDto.getAccountNumber());

        creditRepository.save(credit);

        //Da li je potrebno prebaciti novac na racun preko transakcije ?
        Account account = accountRepository.findByAccountNumber(credit.getAccountNumber()).orElseThrow();
        account.setAvailableBalance(account.getAvailableBalance().add(credit.getAmount()));
//        account.setAvailableBalance(account.getAvailableBalance() + credit.getAmount());
        accountRepository.save(account);

        return CreditMapper.INSTANCE.creditToCreditDto(credit);
    }

    @Scheduled(cron = "0 10 5 * *")
    public void creditMonthlyPay() {
        List<Credit> credits = creditRepository.findAll();
        List<CreditTransactionDto> transactionCreditDtos = new ArrayList<>();
        for (Credit credit : credits) {
            if (credit.getRemainingAmount().compareTo(new BigDecimal(0)) > 0) {
                Account account = accountRepository.findByAccountNumber(credit.getAccountNumber()).orElseThrow();
                account.setAvailableBalance(account.getAvailableBalance().subtract(credit.getMonthlyFee()));
//                account.setAvailableBalance(account.getAvailableBalance() - credit.getMonthlyFee());
                credit.setRemainingAmount(credit.getRemainingAmount().subtract(credit.getMonthlyFee()));
//                credit.setRemainingAmount(credit.getRemainingAmount() - credit.getMonthlyFee());

                accountRepository.save(account);
                creditRepository.save(credit);

                CreditTransactionDto creditTransactionDto = new CreditTransactionDto();
                creditTransactionDto.setAccountFrom(account.getAccountNumber());
                creditTransactionDto.setCreditId(credit.getId());
                creditTransactionDto.setAmount(credit.getMonthlyFee());
                creditTransactionDto.setCurrencyMark(credit.getCurrencyMark());
                creditTransactionDto.setDate(System.currentTimeMillis());

                transactionCreditDtos.add(creditTransactionDto);
            }
        }

        bankServiceClient.createCreditTransactions(transactionCreditDtos);
    }

    private long calculateEndDate(long startDate, int paymentPeriod) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startDate);

        calendar.add(Calendar.MONTH, paymentPeriod);

        return calendar.getTimeInMillis();
    }


}
