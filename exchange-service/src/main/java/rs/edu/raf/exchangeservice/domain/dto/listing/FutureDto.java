package rs.edu.raf.exchangeservice.domain.dto.listing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FutureDto {
    private Long futureId;
    private String contractName;
    private int contractSize;
    private String contractUnit;
    private int maintenanceMargin;
    private String type;
    private double price;
}
