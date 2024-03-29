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
@Entity
@ToString
public class Credit implements Serializable {

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
    private BigDecimal amount;

    @NotNull(message = "This field cannot be NULL")
    private int paymentPeriod;

    @NotNull(message = "This field cannot be NULL")
    private BigDecimal fee;

    @NotNull(message = "This field cannot be NULL")
    private Long startDate;

    @NotNull(message = "This field cannot be NULL")
    private Long endDate;

    @NotNull(message = "This field cannot be NULL")
    private BigDecimal monthlyFee;

    @NotNull(message = "This field cannot be NULL")
    private BigDecimal remainingAmount;

    @NotNull(message = "This field cannot be NULL")
    private String currencyMark;
}
