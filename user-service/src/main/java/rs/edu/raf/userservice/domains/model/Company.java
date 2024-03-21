package rs.edu.raf.userservice.domains.model;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@ToString
public class Company implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;

    @NotNull(message = "This field cannot be NULL")
    private String title;

    @NotNull(message = "This field cannot be NULL")
    @Size(max = 20, message = "The input is too long")
    private String number;

    @NotNull(message = "This field cannot be NULL")
    @Column(unique = true)
    private int pib;

    @NotNull(message = "This field cannot be NULL")
    @Column(unique = true)
    private int maticniBroj;

    @NotNull(message = "This field cannot be NULL")
    private int sifraDelatnosti;

    @NotNull(message = "This field cannot be NULL")
    @Email
    private String email;

    @OneToMany(mappedBy = "company")
    private List<CompanyAccount> companyAccounts;




}
