package rs.edu.raf.exchangeservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import rs.edu.raf.exchangeservice.domain.dto.StockTransactionDto;

@FeignClient(name = "bankServiceClient", url = "${bankServiceLocation}")
public interface BankServiceClient {

    @PostMapping(value = "/stockTransaction/start")
    ResponseEntity<?> startStockTransaction (@RequestBody StockTransactionDto stockTransactionDto);
}
