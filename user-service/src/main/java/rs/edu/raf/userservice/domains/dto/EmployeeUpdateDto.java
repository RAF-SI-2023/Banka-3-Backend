package rs.edu.raf.userservice.domains.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import rs.edu.raf.userservice.domains.model.Role;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class EmployeeUpdateDto {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String username;
    @Email
    @NotBlank
    private String email;
    @NotNull
    private Long dateOfBirth;
    @NotBlank
    private String gender;
    @NotBlank
    private String jmbg;
    @NotBlank
    private String address;
    @NotBlank
    private String password;
    @NotBlank
    private String department;
    @NotBlank
    private String position;
    @NotBlank
    private String phoneNumber;
    @NotNull
    private Boolean isActive;

    @NotNull
    private List<Role> roles;
}
