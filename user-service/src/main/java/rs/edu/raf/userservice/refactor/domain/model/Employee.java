package rs.edu.raf.userservice.refactor.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "employees", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})}, schema = "user_service_schema")
public class Employee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @NotNull(message = "firstName field cannot be NULL")
    @Size(max = 20, message = "The input is too long")
    private String firstName;

    @NotNull(message = "lastName field cannot be NULL")
    @Size(max = 20, message = "The input is too long")
    private String lastName;

    @NotNull(message = "username field cannot be NULL")
    @Size(max = 20, message = "The input is too long")
    private String username;

    @NotNull(message = "jmbg field cannot be NULL")
    @Size(min = 13, max = 13, message = "JMBG must be 13 characters long")
    private String jmbg;

    @NotNull(message = "dateOfBirth field cannot be NULL")
    private Long dateOfBirth;

    @NotNull(message = "gender field cannot be NULL")
    @Size(max = 1, message = "gender must be 1 characters long")
    private String gender;

    @NotNull(message = "phoneNumber field cannot be NULL")
    @Size(min = 6, max = 9, message = "phone must be between 6 and 9 characters")
    private String phoneNumber;

    @NotNull(message = "address field cannot be NULL")
    @Size(max = 60)
    private String address;

    @NotNull(message = "email field cannot be NULL")
    @Email
    private String email;

    @JsonIgnore
    private String password;

    private Boolean isActive = true;

    @ManyToOne()
    @JoinColumn(name = "roleId")
    private Role role;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "employee_permissions",
            joinColumns = @JoinColumn(name = "employeeId"),
            inverseJoinColumns = @JoinColumn(name = "permissionId")
    )
    private List<Permission> permissions;
}
