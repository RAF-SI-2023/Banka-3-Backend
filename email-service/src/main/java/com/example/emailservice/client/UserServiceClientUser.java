package com.example.emailservice.client;

import com.example.emailservice.dto.SetPasswordDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "userServiceClientUser", url = "${userServiceLocation}/user")
public interface UserServiceClientUser {

    @PostMapping(value = "/setPassword",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> setPassword(@RequestBody SetPasswordDTO passwordDTO);

}
