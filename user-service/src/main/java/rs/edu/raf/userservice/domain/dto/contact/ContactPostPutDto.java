package rs.edu.raf.userservice.domain.dto.contact;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class ContactPostPutDto {
    private String myName;
    private String name;
    private String accountNumber;
}
