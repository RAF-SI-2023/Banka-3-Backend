package com.example.bankservice.client;

import com.example.bankservice.domains.dto.CheckEnoughBalanceDto;
import com.example.bankservice.domains.dto.RebalanceAccountDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

/***
 *Salje se post zahtev na URL, doda se /setPassword na kraj, prima i salje json.
 *Kada se pozove metoda i posalje objekat, u pozadini se izvrsava http zahtev
 *
 */


@FeignClient(name = "userServiceClient", url = "${userServiceLocation}")
public interface UserServiceClient {


    @GetMapping("/api/v1/account/checkEnoughBalance")
    ResponseEntity<String>checkEnoughBalance(@RequestBody CheckEnoughBalanceDto dto);

    @GetMapping("/api/v1/account/addMoneyToAccount")
    ResponseEntity<String>addMoneyToAccount(@RequestBody RebalanceAccountDto dto);

    @GetMapping("/api/v1/account/takeMoneyFromAccount")
    ResponseEntity<String>takeMoneyFromAccount(@RequestBody RebalanceAccountDto dto);

    @GetMapping("/api/v1/account/reserveMoney")
    ResponseEntity<String>reserveMoney(@RequestBody RebalanceAccountDto dto);

    @GetMapping("/api/v1/account/unreserveMoney")
    ResponseEntity<String>unreserveMoney(@RequestBody RebalanceAccountDto dto);

    @GetMapping("/api/v1/account/getEmailByAccountNumber")
    String getEmailByAccountNumber(String accountNumber);





}
