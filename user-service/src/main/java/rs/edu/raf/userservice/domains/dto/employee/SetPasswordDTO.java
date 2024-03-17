package rs.edu.raf.userservice.domains.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetPasswordDTO implements Serializable {
    private String password;
    private String email;

}
