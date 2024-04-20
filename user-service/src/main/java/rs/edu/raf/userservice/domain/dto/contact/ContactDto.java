package rs.edu.raf.userservice.domain.dto.contact;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.userservice.domain.model.User;

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
