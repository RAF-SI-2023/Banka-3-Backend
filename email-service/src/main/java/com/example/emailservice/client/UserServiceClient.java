package com.example.emailservice.client;

import com.example.emailservice.dto.ResetUserPasswordDTO;
import com.example.emailservice.dto.SetPasswordDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "userServiceClient", url = "${userServiceLocation}")
public interface UserServiceClient {
    @PostMapping(value = "/employee/setPassword",
            produces = "application/json",
            consumes = "application/json")
    ResponseEntity<String> setPassword(@RequestBody SetPasswordDTO passwordDTO);

    @PostMapping(value = "/user/resetPassword",
            produces = "application/json",
            consumes = "application/json")
    ResponseEntity<String> resetUserPassword(@RequestBody ResetUserPasswordDTO passwordDTO);

}
