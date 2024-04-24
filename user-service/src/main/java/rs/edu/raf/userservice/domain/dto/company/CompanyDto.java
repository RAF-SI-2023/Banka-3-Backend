package rs.edu.raf.userservice.domain.dto.company;

import lombok.*;
import rs.edu.raf.userservice.domain.AuthenticationDetails;
import rs.edu.raf.userservice.domain.model.Permission;
import rs.edu.raf.userservice.domain.model.Role;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class CompanyDto implements Serializable, AuthenticationDetails {
    //kada vracamo getAllCompanies ili kada vracamo single Kompaniju
    private long companyId;
    private String title;
    private String number;
    private String email;
    private int PIB;
    private int maticniBroj;
    private int sifraDelatnosti;
    private boolean isActive;

    @Override
    public Role getRole() {
        return null;
    }

    @Override
    public Long getId() {
        return companyId;
    }

    @Override
    public List<Permission> getPermissions() {
        return new ArrayList<>();
    }
}
