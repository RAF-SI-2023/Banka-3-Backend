package rs.edu.raf.userservice.domain.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.userservice.domain.AuthenticationDetails;
import rs.edu.raf.userservice.domain.model.Permission;
import rs.edu.raf.userservice.domain.model.Role;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements AuthenticationDetails, Serializable {
    //za GetAll ili kada vracamo single User-a
    private Long userId;
    private String firstName;
    private String lastName;
    private String jmbg;
    private Long dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String address;
    private String email;
    private boolean isActive;
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
    @JsonIgnore
    public List<Permission> getPermissions() {
        return new ArrayList<>();
    }
}
