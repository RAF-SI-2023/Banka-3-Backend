package rs.edu.raf.userservice.util.client;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import rs.edu.raf.userservice.domain.dto.employee.ExchangeEmployeeDto;

@FeignClient(name = "exchangeServiceClient", url = "${exchangeServiceLocation}")
public interface ExchangeServiceClient {

    @PostMapping(value ="/actuary/addActuary",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "kada se doda novi zaposleni sa rolom agent ili supervisor, saljemo njegove podatke ka Exchange servicu")
    ResponseEntity<?> addActuary(@RequestBody ExchangeEmployeeDto exchangeEmployeeDto);

}
