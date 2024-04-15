package rs.edu.raf.userservice.domains.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(schema = "user_service_schema")
public class CompanyAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyAccountId;

    @ManyToOne
    private Company company;

    @NotNull(message = "This field cannot be NULL")
    private BigDecimal balance;

    @NotNull(message = "This field cannot be NULL")
    private BigDecimal availableBalance;

    @NotNull(message = "This field cannot be NULL")
    private BigDecimal reservedAmount;

    @ManyToOne()
    @JoinColumn(name = "employeeId")
    private Employee employee;

    @NotNull(message = "This field cannot be NULL")
    private Long creationDate;

    @NotNull(message = "This field cannot be NULL")
    private Long expireDate;

    @NotNull(message = "This field cannot be NULL")
    private boolean active;

    @ManyToOne()
    @JoinColumn(name = "currencyId")
    private Currency currency;

    @NotNull(message = "This field cannot be NULL")
    private String accountNumber;

}




















