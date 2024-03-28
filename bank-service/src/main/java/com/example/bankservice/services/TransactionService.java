package com.example.bankservice.services;


import com.example.bankservice.client.EmailServiceClient;
import com.example.bankservice.client.UserServiceClient;
import com.example.bankservice.domains.dto.*;
import com.example.bankservice.domains.mappers.TransactionMapper;
import com.example.bankservice.domains.model.Transaction;
import com.example.bankservice.domains.model.enums.TransactionState;
import com.example.bankservice.repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final UserServiceClient userServiceClient;
    private final EmailServiceClient emailServiceClient;

    public ResponseEntity<String> doesPersonHaveEnoughBalance(CheckEnoughBalanceDto dto) {
        return userServiceClient.checkEnoughBalance(dto);
    }

    //Ako ima novca,vracamo transacionId. Posle se taj id koristi sa proveru koda
    public ResponseEntity<Long> startTransaction(TransactionDto dto) {

        ResponseEntity<String> response =
                userServiceClient.checkEnoughBalance(new CheckEnoughBalanceDto(dto.getAccountFrom(), dto.getAmount(),
                        dto.getCurrencyMark()));

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ResponseStatusException(response.getStatusCode(), response.getBody());
        }
        Transaction transaction = TransactionMapper.INSTANCE.transactionDtoToTransaction(dto);
        transaction.setDate(System.currentTimeMillis());
        transaction.setState(TransactionState.PENDING);
        userServiceClient.reserveMoney(new RebalanceAccountDto(dto.getAccountFrom(), dto.getAmount(),
                dto.getCurrencyMark()));
        transaction = transactionRepository.save(transaction);
        //Sada treba poslati poruku email servisu sa emailom korisnika i id transakcije. Zatim za to generisati kod,i
        // ako se potrefi to je to
        String email = userServiceClient.getEmailByAccountNumber(dto.getAccountFrom());
        emailServiceClient.sendTransactionActivationEmailToEmailService(new TransactionActivationDto(email,
                transaction.getTransactionId()));

        return ResponseEntity.ok(transaction.getTransactionId());
    }

    //Kada korisnik potvrdi transakciju,email servis proverava da li je kod dobar,ako jeste prebacujemo transakciju u
    // ACCEPTED stanje

    public ResponseEntity<String> confirmTransaction(ConfirmTransactionDto confirmTransactionDto) {

        Optional<Transaction> optionalTransaction =
                transactionRepository.findById(confirmTransactionDto.getTransactionId());
        if (!optionalTransaction.isPresent())
            return ResponseEntity.badRequest().body("Transaction with id " + confirmTransactionDto.getTransactionId() + " does not exist");

        Transaction transaction = optionalTransaction.get();
        if (transaction.getState() != TransactionState.PENDING)
            return ResponseEntity.badRequest().body("Transaction with id " + confirmTransactionDto.getTransactionId()
                    + " is not in PENDING state");

        transaction.setState(TransactionState.ACCEPTED);//ako je accepted,cron job ce prepoznati da treba da skine
        // sredstva
        transactionRepository.save(transaction);
        return ResponseEntity.ok("Transaction with id " + confirmTransactionDto.getTransactionId() + " is confirmed");
    }

    /**
     * Na svakih 5 minuta potrebno je postaviti scheduler koji ce sve transakcije ACCEPTED da prebaci u FINISHED stanje,
     * da skloni sumu iz rezervisanih sredstava i da umanji stvarno stanje racuna.
     */

    @Scheduled(fixedRate = 30000) // Postavljanje cron izraza da se metoda izvrsava svakih 5 minuta
    public void processTransactions() {

        Optional<List<Transaction>> optionalTransactions = transactionRepository.findByState(TransactionState.ACCEPTED);
        List<Transaction> transactions;
        if (!optionalTransactions.isPresent()) return;
        transactions = optionalTransactions.get();
        for (Transaction transaction : transactions) {
            finishTransaction(transaction);
        }
    }

    /**
     * Skida korisniku sumu novca koja je navedena u transakciji
     * Prilikom instanciranja transakcije,novac prelazi u rezervisana sredstva,i ako sve prodje kako treba tek tada
     * se skida (iz rezervisanih sredstava)
     */
    public void finishTransaction(Transaction transaction) {

        userServiceClient.unreserveMoney(new RebalanceAccountDto(transaction.getAccountFrom(),
                transaction.getAmount(), transaction.getCurrencyMark()));
        userServiceClient.takeMoneyFromAccount(new RebalanceAccountDto(transaction.getAccountFrom(),
                transaction.getAmount(), transaction.getCurrencyMark()));
        userServiceClient.addMoneyToAccount(new RebalanceAccountDto(transaction.getAccountTo(),
                transaction.getAmount(), transaction.getCurrencyMark()));
        transaction.setState(TransactionState.FINISHED);
        transactionRepository.save(transaction);


    }


}
