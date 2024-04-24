package com.example.bankservice.domain.model.accounts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(schema = "bank_service_schema")
public class CompanyAccount extends Account implements Serializable {

    private Long companyId;
}
