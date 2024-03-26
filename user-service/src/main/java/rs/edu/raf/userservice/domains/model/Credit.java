package rs.edu.raf.userservice.domains.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@ToString
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @NotNull(message = "This field cannot be NULL")
    private String name;

    @NotNull(message = "This field cannot be NULL")
    private Long accountNumber;

    @NotNull(message = "This field cannot be NULL")
    private Double amount;

    @NotNull(message = "This field cannot be NULL")
    private int paymentPeriod;

    @NotNull(message = "This field cannot be NULL")
    private double fee;

    @NotNull(message = "This field cannot be NULL")
    private Long startDate;

    @NotNull(message = "This field cannot be NULL")
    private Long endDate;

    @NotNull(message = "This field cannot be NULL")
    private Double monthlyFee;

    @NotNull(message = "This field cannot be NULL")
    private Double remainingAmount;

    @NotNull(message = "This field cannot be NULL")
    private String currencyMark;
}
