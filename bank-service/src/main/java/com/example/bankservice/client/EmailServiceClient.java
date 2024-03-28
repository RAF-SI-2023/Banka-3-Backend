package com.example.bankservice.client;

import com.example.bankservice.domains.dto.TransactionActivationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/***
 * Klijent za komunikaciju sa emailService.
 * Salje se get zahtev na putanju sa prosledjenim mejlom kao parametrom
 */
@FeignClient(value = "emailServiceClient", url = "${emailServiceLocation}/")
public interface EmailServiceClient {
    @RequestMapping(method = RequestMethod.GET, value = "transaction/begin")
    ResponseEntity<Void> sendTransactionActivationEmailToEmailService(@RequestParam(name = "email") TransactionActivationDto dto);
}
