package com.example.bankservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@ToString
@Table(schema = "bank_service_schema")
public class CommissionFromCurrencyExchange implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commisionId;
    
    @NotNull(message = "This field cannot be NULL")
    private BigDecimal amount;
    
    @NotNull(message = "This field cannot be NULL")
    private int numberOfTransactions;
}
