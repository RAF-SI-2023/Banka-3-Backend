package com.example.bankservice.domain.model.marginAccounts;

import com.example.bankservice.domain.model.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.type.CurrencyType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(schema = "bank_service_schema")
@Inheritance(strategy = InheritanceType.JOINED)
public class MarginAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long marginAccountId;

    //stanje na racunu, kolicina novca imamo
    @NotNull(message = "This field cannot be NULL")
    private BigDecimal initialMargin;

    //kolicina novca koju moramo imati na racunu, kako bi mogli da koristimo racun
    @NotNull(message = "This field cannot be NULL")
    private BigDecimal maintenanceMargin;

    //koliko smo duzni banci (na pocetku nula)
    @NotNull(message = "This field cannot be NULL")
    private BigDecimal loanValue=BigDecimal.ZERO;


    //deo koji dobijamo od banke, izrazeno u procentima
    @NotNull(message = "This field cannot be NULL")
    private Double bankParticipation;

    @NotNull(message = "This field cannot be NULL")
    @Size(min = 16, max = 16,message = "The account number must be 16 digits")
    private String accountNumber;

    //Uvek RSD
    @ManyToOne
    @JoinColumn(name = "currencyId")
    private Currency currency;

    //boolean da li je racun dostupan za koriscenje (default je true)
    @NotNull(message = "This field cannot be NULL")
    private boolean active;





}
