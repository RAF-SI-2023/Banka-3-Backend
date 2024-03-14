package rs.edu.raf.userservice.domains.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDto {
    private String firstName;
    private String lastName;
    private String jmbg;
    private String dateOfBirth;
    private String gender;
    private String phoneNumber;
    @Email
    private String email;
}
