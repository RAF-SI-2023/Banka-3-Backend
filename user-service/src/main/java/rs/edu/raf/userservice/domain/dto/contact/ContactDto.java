package rs.edu.raf.userservice.domain.dto.contact;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.userservice.domain.model.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactDto {
    //za vracnje kompletnog Kontakta, kada front zove getALl
    private Long contactId;
    private User user;
    private String myName;
    private String name;
    private String accountNumber;
}
