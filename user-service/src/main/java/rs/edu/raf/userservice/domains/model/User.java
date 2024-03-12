package rs.edu.raf.userservice.domains.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(
        name = "users",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"email", "jmbg"})})
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
    private String jmbg;

    @NotNull(message = "This field cannot be NULL")
    private Long dateOfBirth;

    @NotNull(message = "This field cannot be NULL")
    private String gender;

    @NotNull(message = "This field cannot be NULL")
    private String phoneNumber;

    private String address;

    @NotNull(message = "This field cannot be NULL")
    @Email
    private String email;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private String saltPassword;

    @NotNull(message = "This field cannot be NULL")
    private Boolean isActive;
}
