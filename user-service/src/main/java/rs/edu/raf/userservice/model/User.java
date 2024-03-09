package rs.edu.raf.userservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(
        name="users",
        uniqueConstraints = {@UniqueConstraint( columnNames = {"email", "jmbg"})})
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotNull(message = "This field cannot be NULL")
    @Size(max = 20, message = "The input is too long")
    private String firstName;

    @NotNull(message = "This field cannot be NULL")
    @Size(max = 20, message = "The input is too long")
    private String lastName;

    @NotNull(message = "This field cannot be NULL")
    @Size(max = 20, message = "The input is too long")
    private String username;

    @NotNull(message = "This field cannot be NULL")
    private String jmbg;

    @NotNull(message = "This field cannot be NULL")
    private Long dateOfBirth;

    @NotNull(message = "This field cannot be NULL")
    private String gender;

    @NotNull(message = "This field cannot be NULL")
    private String phoneNumber;

    @NotNull(message = "This field cannot be NULL")
    private String address;

    @NotNull(message = "This field cannot be NULL")
    @Email
    private String email;

    @NotNull(message = "This field cannot be NULL")
    private String password;

    @NotNull(message = "This field cannot be NULL")
    private String saltPassword;

    @NotNull(message = "This field cannot be NULL")
    private Boolean isActive;

    private String position;

    private String department;

    @JsonIgnore
    @ManyToMany(mappedBy = "users")
    private List<Role> roles = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "users")
    private List<Permission> permissions = new ArrayList<>();

}
