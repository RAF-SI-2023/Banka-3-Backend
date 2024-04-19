package rs.edu.raf.userservice.domains.dto.card;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class CardDto {
    private String cardNumber;
    private Long expireDate;
    private String cvv;
}
