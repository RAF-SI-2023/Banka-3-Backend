package rs.edu.raf.exchangeservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Actuary implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long actuaryId;
    private Long employeeId; //ovo dobijamo iz User Service-a
    private String email; //ovo dobijamo iz User Service-a
    private String role; //ovo dobijamo iz User Service-a
    private Double limitValue;
    private Double limitUsed;
    private boolean orderRequest;
}
