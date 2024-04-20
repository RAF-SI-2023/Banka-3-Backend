package com.example.bankservice.domain.model.accounts;

import com.example.bankservice.domain.model.Currency;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Entity
@Table(schema = "bank_service_schema")
public class UserAccount extends Account implements Serializable {

    private Long userId;

    private String accountType;
}
