package zews.Email_Service.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountActivationDto {
    /**
     * Ova klasa treba da sluzi da prihvati
     * objekat koji stigne od frontenda prilikom provere da li je kod
     * za prosledjeni email aktivan
     */
    private String email;
    private int code;
}
