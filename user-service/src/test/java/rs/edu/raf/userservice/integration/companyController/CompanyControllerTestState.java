package rs.edu.raf.userservice.integration.companyController;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyControllerTestState {

        String jwtToken;
        Long companyId;
        String email;
}
