package rs.edu.raf.userservice.domains.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IsUserActiveDto {

    private String email;
    private Boolean codeActive;
}
