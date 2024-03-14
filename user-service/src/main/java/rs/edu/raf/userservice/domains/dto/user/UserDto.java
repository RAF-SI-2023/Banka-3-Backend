package rs.edu.raf.userservice.domains.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.userservice.domains.dto.AuthenticationDetails;
import rs.edu.raf.userservice.domains.model.Permission;
import rs.edu.raf.userservice.domains.model.Role;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public Long getId() {
        return userId;
    }

    @Override
    public List<Permission> getPermissions() {
        return new ArrayList<>();
    }

}
