package rs.edu.raf.exchangeservice.client;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import rs.edu.raf.exchangeservice.domain.dto.ActuaryDto;

import java.util.List;

@FeignClient(name = "userServiceClient", url = "${userServiceLocation}")
public interface UserServiceClient {

    @GetMapping(value = "/employee/getExchangeEmployees")
    @Operation(description = "da povucemo sve zaposlene sa ROLE agent ili supervisor")
    ResponseEntity<List<ActuaryDto>> getEmployees();


    @GetMapping("/getByCompany/{id}")
    @Operation(description = "da povucemo sve zaposlene sa ROLE agent ili supervisor")
    ResponseEntity<?> getCompanyById(@PathVariable Long id);



}
