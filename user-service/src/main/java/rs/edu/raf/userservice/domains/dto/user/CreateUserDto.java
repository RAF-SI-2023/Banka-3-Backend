package rs.edu.raf.userservice.domains.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String email;
}
