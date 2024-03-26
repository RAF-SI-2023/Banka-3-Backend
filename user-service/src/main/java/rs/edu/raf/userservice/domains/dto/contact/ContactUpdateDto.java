package rs.edu.raf.userservice.domains.dto.contact;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactUpdateDto {

    private String myName;
    private String name;
    private String accountNumber;
}
