package rs.edu.raf.userservice.domains.dto.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class CompanyDto {

    private long companyId;
    private String title;
    private String number;
    private String email;
    private int PIB; //unique
    private int matBr; //unique
    private int sifraDelatnosti;
}
