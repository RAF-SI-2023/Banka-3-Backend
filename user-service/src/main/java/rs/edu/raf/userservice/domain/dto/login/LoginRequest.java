package rs.edu.raf.userservice.domain.dto.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class LoginRequest {
    private String email;
    private String password;
}
