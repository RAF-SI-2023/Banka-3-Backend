package rs.edu.raf.userservice.domains.dto.contact;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.userservice.domains.model.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactDto {
    private Long id;
    private User user;
    private String myName;
    private String name;
    private String accountNumber;
}
