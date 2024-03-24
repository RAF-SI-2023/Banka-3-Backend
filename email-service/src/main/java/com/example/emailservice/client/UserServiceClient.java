package com.example.emailservice.client;

import com.example.emailservice.dto.ResetPasswordDTO;
import com.example.emailservice.dto.ResetUserPasswordDTO;
import com.example.emailservice.dto.SetPasswordDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
/***
 *Salje se post zahtev na URL, doda se /setPassword na kraj, prima i salje json.
 *Kada se pozove metoda i posalje objekat, u pozadini se izvrsava http zahtev
 *
 */


@FeignClient(name = "userServiceClient", url = "${userServiceLocation}")
public interface UserServiceClient {

    @PostMapping(value = "/user/setPassword",
            produces = "application/json",
            consumes = "application/json")
    ResponseEntity<String> setUserPassword(@RequestBody SetPasswordDTO passwordDTO);

    @PostMapping(value = "/employee/setPassword",
            produces = "application/json",
            consumes = "application/json")
    ResponseEntity<String> setPassword(@RequestBody SetPasswordDTO passwordDTO);




    /**
     * Meotda za komunikaciju sa user servisom, prosledjuje se mejl i nova sifra zaposlenog*/
    @PostMapping(value = "/employee/resetPassword",
            produces = "application/json"
            , consumes = "application/json"
    )
    ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDTO passwordDTO);

    @PostMapping(value = "/user/resetPassword",
            produces = "application/json"
            , consumes = "application/json"
    )
    ResponseEntity<String> resetUserPassword(@RequestBody ResetUserPasswordDTO passwordDTO);
}
