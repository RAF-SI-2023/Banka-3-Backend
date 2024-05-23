package rs.edu.raf.exchangeservice.domain.model.myListing;

import lombok.*;
import rs.edu.raf.exchangeservice.domain.model.enums.BankCertificate;
import rs.edu.raf.exchangeservice.domain.model.enums.SellerCertificate;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FutureContract implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long futureContractId;
    private Long companySellerId;
    private Long companyBuyerId;
    @Enumerated(EnumType.STRING)
    private BankCertificate bankCertificate;
    @Enumerated(EnumType.STRING)
    private SellerCertificate sellerCertificate;
    private String comment;
    private Long dateCreated;
    private Long dateFinished;
    private String contractNumber;
    private String about;
    private String contractName;
    private Double price;
}
