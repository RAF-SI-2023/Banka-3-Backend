//package com.example.bankservice.client;
//
//import com.example.bankservice.domains.dto.CheckEnoughBalanceDto;
//import com.example.bankservice.domains.dto.RebalanceAccountDto;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
///***
// *Salje se post zahtev na URL, doda se /setPassword na kraj, prima i salje json.
// *Kada se pozove metoda i posalje objekat, u pozadini se izvrsava http zahtev
// *
// */
//@FeignClient(name = "userServiceClient", url = "${userServiceLocation}")
//public interface UserServiceClient {
//
//    @PostMapping("/account/checkEnoughBalance")
//    ResponseEntity<String> checkEnoughBalance(@RequestBody CheckEnoughBalanceDto dto);
//
//    @PostMapping("/account/addMoneyToAccount")
//    ResponseEntity<String> addMoneyToAccount(@RequestBody RebalanceAccountDto dto);
//
//    @PostMapping("/account/takeMoneyFromAccount")
//    ResponseEntity<String> takeMoneyFromAccount(@RequestBody RebalanceAccountDto dto);
//
//    @PostMapping("/account/reserveMoney")
//    ResponseEntity<String> reserveMoney(@RequestBody RebalanceAccountDto dto);
//
//    @PostMapping("/account/unreserveMoney")
//    ResponseEntity<String> unreserveMoney(@RequestBody RebalanceAccountDto dto);
//
//    @GetMapping("/account/getEmailByAccountNumber/{accountNumber}")
//    String getEmailByAccountNumber(@PathVariable String accountNumber);
//
//    @PostMapping("/companyAccount/checkCompanyBalance")
//    ResponseEntity<String> checkCompanyBalance(@RequestBody CheckEnoughBalanceDto dto);
//
//    @PostMapping("/companyAccount/reserveCompanyMoney")
//    ResponseEntity<String> reserveCompanyMoney(@RequestBody RebalanceAccountDto dto);
//
//    @PostMapping("/companyAccount/unreserveCompanyMoney")
//    ResponseEntity<String> unreserveCompanyMoney(@RequestBody RebalanceAccountDto rebalanceAccountDto);
//
//    @PostMapping("/companyAccount/takeMoneyFromCompanyAccount")
//    ResponseEntity<String> takeMoneyFromCompanyAccount(@RequestBody RebalanceAccountDto dto);
//
//    @PostMapping("/companyAccount/addMoneyToCompanyAccount")
//    ResponseEntity<String> addMoneyToCompanyAccount(@RequestBody RebalanceAccountDto dto);
//}
