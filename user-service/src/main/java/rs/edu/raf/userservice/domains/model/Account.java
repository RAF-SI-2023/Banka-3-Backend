package rs.edu.raf.userservice.domains.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Entity
@Table(schema = "user_service_schema")
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @NotNull(message = "This field cannot be NULL")
    private String accountNumber;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @NotNull(message = "This field cannot be NULL")
    private BigDecimal reservedAmount;

    @NotNull(message = "This field cannot be NULL")
    private BigDecimal availableBalance;

    @ManyToOne
    @JoinColumn(name = "employeeId")
    private Employee employee;

    @NotNull(message = "This field cannot be NULL")
    private Long creationDate;

    @NotNull(message = "This field cannot be NULL")
    private Long expireDate;

    @NotNull(message = "This field cannot be NULL")
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "currencyId")
    private Currency currency;

    @ManyToOne
    @JoinColumn(name = "accountTypeId")
    private AccountType accountType;
}
