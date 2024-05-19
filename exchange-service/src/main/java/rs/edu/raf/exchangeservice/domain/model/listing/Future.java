package rs.edu.raf.exchangeservice.domain.model.listing;

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
public class Future implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long futureId;
    private String contractName;
    private int contractSize;
    private String contractUnit;
    private int maintenanceMargin;
    private String type;
    private String currencyMark;
    private double price;
}