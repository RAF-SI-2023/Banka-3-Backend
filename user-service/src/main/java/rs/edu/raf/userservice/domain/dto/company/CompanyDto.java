package rs.edu.raf.userservice.domain.dto.company;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class CompanyDto implements Serializable {
    private long companyId;
    private String title;
    private String number;
    private String email;
    private int pib;
    private int maticniBroj;
    private int sifraDelatnosti;
}
