package rs.edu.raf.exchangeservice.domain.model.myListing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "exchange_service_schema")
public class MyStock implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long myStockId;
    private String ticker;
    private Long userId;
    private Long companyId;
    private Integer amount;
    private String currencyMark;
    private Integer privateAmount;
    // getAll ali samo public za korisnike  i kompanije > 0
    // mora da se razlikuje i da li je kompanija ili korisnik ulogovan
    // na osnovu toga se vracaju public stockovi
    private Integer publicAmount;
    @Version
    private Integer version;
}
