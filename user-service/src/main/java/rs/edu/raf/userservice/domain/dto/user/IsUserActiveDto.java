package rs.edu.raf.userservice.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IsUserActiveDto implements Serializable {
    //ovo nam treba da proverimo, prilikom Login-a, da li je korisnik
    //uneo kod za aktivaciju
    private String email;
    private boolean isActive;
}
