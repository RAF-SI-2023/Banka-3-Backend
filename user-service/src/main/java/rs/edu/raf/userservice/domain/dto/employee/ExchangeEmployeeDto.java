package rs.edu.raf.userservice.domain.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class ExchangeEmployeeDto {
    //za slanje podataka ka Exchange Servicu
    private Long employeeId;
    private String email;
    private String role;
}
