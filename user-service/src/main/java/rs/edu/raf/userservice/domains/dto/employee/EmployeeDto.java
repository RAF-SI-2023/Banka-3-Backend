package rs.edu.raf.userservice.domains.dto.employee;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import rs.edu.raf.userservice.domains.dto.AuthenticationDetails;
import rs.edu.raf.userservice.domains.model.Permission;
import rs.edu.raf.userservice.domains.model.Role;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class EmployeeDto implements AuthenticationDetails {

    private Long employeeId;
    private String firstName;
    private String lastName;
    private String username;
    private String jmbg;
    private Long dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String address;
    private String email;
    private Boolean isActive;
    private String position;
    private String department;
    private Role role;
    private List<Permission> permissions;

    @Override
    public Role getRole() {
        return role;
    }

    @Override
    public Long getId() {
        return employeeId;
    }

    @Override
    public List<Permission> getPermissions() {
        return permissions;
    }
}
