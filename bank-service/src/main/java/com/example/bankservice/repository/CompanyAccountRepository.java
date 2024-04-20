package com.example.bankservice.repository;

import com.example.bankservice.domain.model.CompanyAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyAccountRepository extends JpaRepository<CompanyAccount, Long> {
}
