package rs.edu.raf.exchangeservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import rs.edu.raf.exchangeservice.domain.dto.ActuaryDto;

import java.util.List;

@FeignClient(name = "userServiceClient", url = "${userServiceLocation}")
public interface UserServiceClient {
    @GetMapping(value = "/employee/getExchangeEmployees",
            produces = "application/json",
            consumes = "application/json")
    ResponseEntity<List<ActuaryDto>> getEmployees();

}
