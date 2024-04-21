package rs.edu.raf.userservice.domain.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeSetPasswordDto implements Serializable {
    //kada zaposleni postavlja sifru, ili kad je restartuje
    private String password;
    private String email;
}
