package rs.edu.raf.exchangeservice.domain.dto.bank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class UserOtcDto {
    private Long userFromId;
    private Long userToId;
    private Double amount;
    private double tax;
}
