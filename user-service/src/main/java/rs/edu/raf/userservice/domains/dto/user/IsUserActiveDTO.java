package rs.edu.raf.userservice.domains.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IsUserActiveDTO implements Serializable {
    private String email;
    private Boolean isActive;
}
