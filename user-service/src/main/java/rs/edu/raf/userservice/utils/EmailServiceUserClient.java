package rs.edu.raf.userservice.utils;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "emailServiceUserClient",
        url = "${emailServiceLocation}/user/")
public interface EmailServiceUserClient {
    @RequestMapping(method = RequestMethod.GET, value = "userActivation")
    ResponseEntity<Void> requestCodeFromEmailService(@RequestParam(name = "email") String email);
}
