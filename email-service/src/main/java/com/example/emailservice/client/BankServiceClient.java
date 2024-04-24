package com.example.emailservice.client;

import com.example.emailservice.domain.dto.FinalizeTransactionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "bankServiceClient", url = "${bankServiceLocation}")
public interface BankServiceClient {

    @PostMapping(value = "/transaction/confirmPaymentTransaction",
            produces = "application/json",
            consumes = "application/json")
    ResponseEntity<?> confirmPaymentTransaction(FinalizeTransactionDto finalizeTransactionDto);
}
