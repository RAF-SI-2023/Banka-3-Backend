package com.example.bankservice.client;

import com.example.bankservice.domain.dto.userService.UserEmailDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/***
 *Salje se post zahtev na URL, doda se /setPassword na kraj, prima i salje json.
 *Kada se pozove metoda i posalje objekat, u pozadini se izvrsava http zahtev
 *
 */
@FeignClient(name = "userServiceClient", url = "${userServiceLocation}")
public interface UserServiceClient {

    @GetMapping("/user/findEmailById/{userId}")
    UserEmailDto getEmailByUserId(@PathVariable String userId);

    @GetMapping("/{companyId}")
    String getEmailByCompanyId(@PathVariable String companyId);
}
