package rs.edu.raf.userservice.utils;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "emailServiceClient",
        url = "${emailServiceLocation}/employee/")
public interface EmailServiceClient {
    @RequestMapping(method = RequestMethod.GET, value = "employeeCreated")
    ResponseEntity<Void> sendEmailToEmailService(@RequestParam(name = "email") String email);

    @RequestMapping(method = RequestMethod.GET, value = "tryPasswordReset ")
    ResponseEntity<Void> sendEmailToEmailServiceForResetPassword(@RequestParam(name = "email") String email);
}
