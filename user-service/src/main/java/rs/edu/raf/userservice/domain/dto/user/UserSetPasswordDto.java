package rs.edu.raf.userservice.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSetPasswordDto implements Serializable {
    //kada korisnik restartuje lozinku
    private String email;
    private String password;
}
