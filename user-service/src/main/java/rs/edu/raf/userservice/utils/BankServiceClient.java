package rs.edu.raf.userservice.utils;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import rs.edu.raf.userservice.domains.dto.credit.CreditTransactionDto;

import java.util.List;

@FeignClient(value = "bankServiceClient",
        url = "${bankServiceLocation}/")
public interface BankServiceClient {

    @PostMapping
    public ResponseEntity<?> createCreditTransactions(@RequestBody List<CreditTransactionDto> transactionCreditDtos);
}
