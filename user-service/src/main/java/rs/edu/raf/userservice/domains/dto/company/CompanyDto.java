package rs.edu.raf.userservice.domains.dto.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import rs.edu.raf.userservice.domains.model.CompanyAccount;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class CompanyDto implements Serializable {
    private long companyId;
    private String title;
    private String number;
    private String email;
    private int pib; //unique
    private int maticniBroj; //unique
    private int sifraDelatnosti;
    private List<CompanyAccount> companyAccounts;
}
