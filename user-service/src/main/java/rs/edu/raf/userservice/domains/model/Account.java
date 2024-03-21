package rs.edu.raf.userservice.domains.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
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
    private Double balance;

    @NotNull(message = "This field cannot be NULL")
    private Double availableBalance;

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




















