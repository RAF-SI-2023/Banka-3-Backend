package rs.edu.raf.userservice.domains.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordDTO implements Serializable {
    private String newPassword;
    private String email;
}
