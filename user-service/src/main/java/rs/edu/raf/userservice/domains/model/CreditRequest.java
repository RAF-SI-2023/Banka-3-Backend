package rs.edu.raf.userservice.domains.model;

import lombok.*;
import rs.edu.raf.userservice.domains.model.enums.CreditRequestStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@ToString
public class CreditRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @NotNull(message = "This field cannot be NULL")
    private String name;

    @NotNull(message = "This field cannot be NULL")
    private String accountNumber;

    @NotNull(message = "This field cannot be NULL")
    private Double amount;

    @NotNull(message = "This field cannot be NULL")
    private String applianceReason;

    private Double monthlyPaycheck;

    @NotNull(message = "This field cannot be NULL")
    private Boolean employed;

    @NotNull(message = "This field cannot be NULL")
    private Long dateOfEmployment;

    @NotNull(message = "This field cannot be NULL")
    private int paymentPeriod;

    @NotNull(message = "This field cannot be NULL")
    @Enumerated(EnumType.STRING)
    private CreditRequestStatus status;

    @NotNull(message = "This field cannot be NULL")
    private String currencyMark;
}
