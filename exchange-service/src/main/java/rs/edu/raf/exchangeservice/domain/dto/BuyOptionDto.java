package rs.edu.raf.exchangeservice.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BuyOptionDto {

    private Long employeeId;
    private String ticker;
    private Double price;
    private Double limitValue;
    private Double stopValue;
    private boolean margine;

    private String contractSymbol;  //mora da stigne

}