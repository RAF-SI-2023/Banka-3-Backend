package rs.edu.raf.userservice.domains.model;
<<<<<<< HEAD

import lombok.*;
import rs.edu.raf.userservice.domains.model.enums.CreditRequestStatus;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
=======
import lombok.*;
import rs.edu.raf.userservice.domains.model.enums.CreditRequestStatus;

import javax.persistence.*;
>>>>>>> d809f3142c956df9112f7a4ff9dc2c694fbfb0d9
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

<<<<<<< HEAD
    @NotNull(message = "This field cannot be NULL")
    private Long userId;
=======
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
>>>>>>> d809f3142c956df9112f7a4ff9dc2c694fbfb0d9

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
<<<<<<< HEAD
=======
    @Enumerated(EnumType.STRING)
>>>>>>> d809f3142c956df9112f7a4ff9dc2c694fbfb0d9
    private CreditRequestStatus status;

    @NotNull(message = "This field cannot be NULL")
    private String currencyMark;
}
