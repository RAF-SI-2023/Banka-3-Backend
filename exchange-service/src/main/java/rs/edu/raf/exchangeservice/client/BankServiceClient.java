package rs.edu.raf.exchangeservice.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "bankServiceClient", url = "${bankServiceLocation}")
public interface BankServiceClient {


}
