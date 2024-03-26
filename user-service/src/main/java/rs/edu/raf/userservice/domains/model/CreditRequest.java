package rs.edu.raf.userservice.domains.model;

import lombok.*;
import rs.edu.raf.userservice.domains.model.enums.CreditRequestStatus;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@ToString
public class CreditRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "This field cannot be NULL")
    private Long userId;

    @NotNull(message = "This field cannot be NULL")
    private String name;

    @NotNull(message = "This field cannot be NULL")
    private Long accountNumber;

    @NotNull(message = "This field cannot be NULL")
    private Double amount;

    @NotNull(message = "This field cannot be NULL")
    private String applianceReason;

    @NotNull(message = "This field cannot be NULL")
    private Double monthlyPaycheck;

    @NotNull(message = "This field cannot be NULL")
    private Boolean employed;

    @NotNull(message = "This field cannot be NULL")
    private Long dateOfEmployment;

    @NotNull(message = "This field cannot be NULL")
    private int paymentPeriod;

    @NotNull(message = "This field cannot be NULL")
    private CreditRequestStatus status;

    @NotNull(message = "This field cannot be NULL")
    private String currencyMark;
}
