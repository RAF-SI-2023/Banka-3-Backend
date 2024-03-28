package rs.edu.raf.userservice.domains.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import rs.edu.raf.userservice.domains.model.enums.AccountTypeName;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
public class AccountType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountTypeId;

    @Enumerated(EnumType.STRING)
    private AccountTypeName accountType;

    @NotNull(message = "This field cannot be NULL")
    private double monthlyFee;


}
