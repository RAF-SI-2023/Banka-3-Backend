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
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
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
    @Size(max = 1)
    private String gender;

    @NotNull(message = "This field cannot be NULL")
    private String phoneNumber;

    private String address;

    @NotNull(message = "This field cannot be NULL")
    @Email
    private String email;

    @JsonIgnore
    private String password;

    @NotNull(message = "This field cannot be NULL")
    private boolean isActive;

    @OneToMany
    private List<Account> accounts = new ArrayList<>();

    @OneToMany
    private List<ForeignAccount> foreignAccounts = new ArrayList<>();

    @OneToMany
    private List<Credit> credits = new ArrayList<>();

    @OneToMany
    private List<Contact> contacts = new ArrayList<>();

    @OneToMany
    private List<CreditRequest> creditsRequests = new ArrayList<>();

    private Boolean codeActive = false;
}
