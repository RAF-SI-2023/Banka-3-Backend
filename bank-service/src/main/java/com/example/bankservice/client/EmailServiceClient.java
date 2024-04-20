package com.example.bankservice.client;

import com.example.bankservice.domain.dto.transaction.PaymentTransactionActivationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/***
 * Klijent za komunikaciju sa emailService.
 * Salje se get zahtev na putanju sa prosledjenim mejlom kao parametrom
 */
@FeignClient(value = "emailServiceClient", url = "${emailServiceLocation}")
public interface EmailServiceClient {
    @PostMapping(value = "/transaction/begin")
    ResponseEntity<Void> sendTransactionActivationEmailToEmailService(@RequestBody PaymentTransactionActivationDto paymentTransactionActivationDto);
}
