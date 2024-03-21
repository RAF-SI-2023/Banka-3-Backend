package rs.edu.raf.userservice.utils;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "emailServiceClient",
        url = "${emailServiceLocation}/")
public interface EmailServiceClient {
    @RequestMapping(method = RequestMethod.GET, value = "employee/employeeCreated")
    ResponseEntity<Void> sendEmailToEmailService(@RequestParam(name = "email") String email);

    @RequestMapping(method = RequestMethod.GET, value = "user/tryChangePassword ")
    ResponseEntity<Void> sendEmailToEmailServiceForResetPassword(@RequestParam(name = "email") String email);
}
