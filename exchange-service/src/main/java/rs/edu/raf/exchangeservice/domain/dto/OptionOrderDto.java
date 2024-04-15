package rs.edu.raf.exchangeservice.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OptionOrderDto {

    private Long optionOrderId;
    private Long employeeId;
    private String contractSymbol;
    private String status;  //PROCESSING, WAITING, FAILED, FINISHED
    private int amount;
    private int amountLeft;
    private boolean aon;
    private boolean margine;

    private String stockListing; //povezujemo ga sa Stock
    private String optionType; //calls ili puts

}