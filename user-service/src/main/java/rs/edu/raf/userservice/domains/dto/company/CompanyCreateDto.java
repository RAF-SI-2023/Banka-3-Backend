package rs.edu.raf.userservice.domains.dto.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CompanyCreateDto {

    @NotBlank
    private String title;
    @NotBlank
    private String number;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Size(min=100000000, max=999999999)
    private int pib; //unique
    @NotBlank
    @Size(min=10000000, max=99999999)
    private int matBr; //unique
    @NotBlank
    @Size(min=1000, max=9000)
    private int sifraDelatnosti;
}
