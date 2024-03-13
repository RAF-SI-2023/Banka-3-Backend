package rs.edu.raf.userservice.domains.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import rs.edu.raf.userservice.domains.model.Role;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
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
    private String saltPassword;
    private Boolean isActive;
    private String position;
    private String department;
    private Role role;

    @Override
    public Role getRole() {
        return role;
    }
}
