package rs.edu.raf.userservice.domain.model;

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
@Table(name = "companies", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})}, schema = "user_service_schema")
public class Company implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;

    @NotNull(message = "title field cannot be NULL")
    @Size(max = 20, message = "The input is too long")
    private String title;

    @NotNull(message = "number field cannot be NULL")
    @Size(min = 5, max = 10, message = "The input is too long")
    private String number;

    @NotNull(message = "PIB field cannot be NULL")
    private int PIB;

    @NotNull(message = "maticniBroj field cannot be NULL")
    private int maticniBroj;

    @NotNull(message = "sifraDelatnosti field cannot be NULL")
    private int sifraDelatnosti;

    @NotNull(message = "email field cannot be NULL")
    @Email
    private String email;

    @JsonIgnore
    private String password;

    private boolean isActive = true;

    private boolean codeActive = false;
}
