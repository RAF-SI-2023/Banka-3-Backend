package rs.edu.raf.userservice.domains.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "users")
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

    @ManyToOne()
    @JoinColumn(name = "roleId")
    private Role role;
}
