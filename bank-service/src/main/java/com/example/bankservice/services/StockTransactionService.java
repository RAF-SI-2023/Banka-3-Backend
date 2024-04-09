package com.example.bankservice.services;

import com.example.bankservice.client.UserServiceClient;
import com.example.bankservice.domains.dto.CheckEnoughBalanceDto;
import com.example.bankservice.domains.dto.StockTransactionDto;
import com.example.bankservice.domains.mappers.StockTransactionMapper;
import com.example.bankservice.domains.model.StockTransaction;
import com.example.bankservice.domains.model.enums.TransactionState;
import com.example.bankservice.repositories.StockTransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class StockTransactionService {

    private StockTransactionRepository stockTransactionRepository;
    private UserServiceClient userServiceClient;

    public ResponseEntity<?> startStockTransaction(StockTransactionDto dto) {

        //check if there is enough money to complete transaction
        ResponseEntity<String> response =
                userServiceClient.checkCompanyEnoughBalance(
                        new CheckEnoughBalanceDto(dto.getAccountFrom(), dto.getAmount(), null));

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ResponseStatusException(response.getStatusCode(), response.getBody());
        }

        StockTransaction stockTransaction = StockTransactionMapper.INSTANCE.stockTransactionDtoToStockTransaction(dto);
        stockTransaction.setDate(System.currentTimeMillis());
        stockTransaction.setState(TransactionState.PENDING);
        stockTransactionRepository.save(stockTransaction);

        return ResponseEntity.ok().build();
    }


}
