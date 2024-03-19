package com.example.emailservice.client;

import com.example.emailservice.dto.ResetPasswordDTO;
import com.example.emailservice.dto.SetPasswordDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "userServiceClient", url = "${userServiceLocation}/employee")
public interface UserServiceClient {

    @PostMapping(value = "/setPassword",
            produces = "application/json",
            consumes = "application/json")
    ResponseEntity<String> setPassword(@RequestBody SetPasswordDTO passwordDTO);

    @PostMapping(value = "/resetPassword",
            produces = "application/json",
            consumes = "application/json")
    ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDTO passwordDTO);
}
