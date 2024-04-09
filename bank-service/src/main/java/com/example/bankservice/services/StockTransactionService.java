package com.example.bankservice.services;

import com.example.bankservice.client.UserServiceClient;
import com.example.bankservice.domains.dto.CheckEnoughBalanceDto;
import com.example.bankservice.domains.dto.RebalanceAccountDto;
import com.example.bankservice.domains.dto.StockTransactionDto;
import com.example.bankservice.domains.mappers.StockTransactionMapper;
import com.example.bankservice.domains.model.StockTransaction;
import com.example.bankservice.domains.model.enums.TransactionState;
import com.example.bankservice.repositories.StockTransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class StockTransactionService {

    private StockTransactionRepository stockTransactionRepository;
    private UserServiceClient userServiceClient;

    public ResponseEntity<?> startStockTransaction(StockTransactionDto dto) {

        StockTransaction stockTransaction = StockTransactionMapper.INSTANCE.stockTransactionDtoToStockTransaction(dto);
        stockTransaction.setDate(System.currentTimeMillis());

        //check if there is enough money to complete transaction
        ResponseEntity<String> response =
                userServiceClient.checkCompanyBalance(
                        new CheckEnoughBalanceDto(dto.getAccountFrom(), dto.getAmount(), null));

        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            stockTransaction.setState(TransactionState.FAILED);
            stockTransactionRepository.save(stockTransaction);
            throw new ResponseStatusException(response.getStatusCode(), response.getBody());
        }

        stockTransaction.setState(TransactionState.ACCEPTED);
        stockTransactionRepository.save(stockTransaction);

        //reserve money on company account
        userServiceClient.reserveCompanyMoney(new RebalanceAccountDto(stockTransaction.getAccountFrom(), stockTransaction.getAmount(),
                null));

        return ResponseEntity.ok().build();
    }

    @Scheduled(fixedRate = 30000)
    public void processStockTransactions() {

        List<StockTransaction> stockTransactions = stockTransactionRepository.findByState(TransactionState.ACCEPTED);
        if (stockTransactions == null)
            return;

        for (StockTransaction stockTransaction : stockTransactions) {
            finishStockTransaction(stockTransaction);
        }
    }

    private void finishStockTransaction(StockTransaction stockTransaction) {
        userServiceClient.unreserveCompanyMoney(new RebalanceAccountDto(stockTransaction.getAccountFrom(),
                stockTransaction.getAmount(), null));
        userServiceClient.takeMoneyFromCompanyAccount(new RebalanceAccountDto(stockTransaction.getAccountFrom(),
                stockTransaction.getAmount(), null));
        userServiceClient.addMoneyToCompanyAccount(new RebalanceAccountDto(stockTransaction.getAccountTo(),
                stockTransaction.getAmount(), null));
        stockTransaction.setState(TransactionState.FINISHED);
        stockTransactionRepository.save(stockTransaction);
    }


}
