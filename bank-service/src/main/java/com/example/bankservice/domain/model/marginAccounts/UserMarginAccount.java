package com.example.bankservice.domain.model.marginAccounts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
@Data
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Entity
@Table(schema = "bank_service_schema")
public class UserMarginAccount extends MarginAccount implements Serializable {


    //user/employee mogu imati samo jedan racun
    @Column(unique = true)
    private Long userId;


}
