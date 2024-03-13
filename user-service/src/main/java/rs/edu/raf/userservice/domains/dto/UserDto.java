package rs.edu.raf.userservice.domains.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.userservice.domains.model.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements AuthenticationDetails {
    private Long userId;
    private String firstName;
    private String lastName;
    private String jmbg;
    private Long dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String address;
    private String email;
    private Boolean isActive;
    private Role role;

    @Override
    public Role getRole() {
        return role;
    }
}
