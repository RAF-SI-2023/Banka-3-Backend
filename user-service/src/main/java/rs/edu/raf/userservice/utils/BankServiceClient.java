package rs.edu.raf.userservice.utils;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import rs.edu.raf.userservice.domains.dto.card.CreateCardDto;
import rs.edu.raf.userservice.domains.dto.credit.CreditTransactionDto;

import java.util.List;

@FeignClient(value = "bankServiceClient",
        url = "${bankServiceLocation}")
public interface BankServiceClient {

    @PostMapping
    public ResponseEntity<?> createCreditTransactions(@RequestBody List<CreditTransactionDto> transactionCreditDtos);

//    @PostMapping("/card/create")
//    public ResponseEntity<?> createCard(@RequestBody CreateCardDto createCardDto);


}
