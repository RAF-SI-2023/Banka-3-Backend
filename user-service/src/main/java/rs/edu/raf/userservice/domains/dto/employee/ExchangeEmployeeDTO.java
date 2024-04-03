package rs.edu.raf.userservice.domains.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import rs.edu.raf.userservice.domains.model.Role;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class ExchangeEmployeeDTO {
    private Long employeeId;
    private String email;
    private Role role;

}
