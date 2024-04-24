package com.example.emailservice.client;

import com.example.emailservice.domain.dto.password.SetPasswordDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "userServiceClient", url = "${userServiceLocation}")
public interface UserServiceClient {

    @PostMapping(value = "/user/setPassword",
            produces = "application/json",
            consumes = "application/json")
    ResponseEntity<String> setUserPassword(@RequestBody SetPasswordDto passwordDTO);

    @PostMapping(value = "/company/setPassword",
            produces = "application/json",
            consumes = "application/json")
    ResponseEntity<String> setCompanyPassword(@RequestBody SetPasswordDto passwordDTO);

    @PostMapping(value = "/employee/setPassword",
            produces = "application/json",
            consumes = "application/json")
    ResponseEntity<String> setEmployeePassword(@RequestBody SetPasswordDto passwordDTO);
}
