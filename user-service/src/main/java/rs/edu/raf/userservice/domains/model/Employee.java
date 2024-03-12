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
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(
        name="employees",
        uniqueConstraints = {@UniqueConstraint( columnNames = {"email", "jmbg"})})
public class Employee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

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
    @ManyToOne()
    @JoinColumn(name = "roleId")
    private Role roles;
}
