package com.example.bankservice.repository;

import com.example.bankservice.domain.model.accounts.CompanyAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyAccountRepository extends JpaRepository<CompanyAccount, Long> {

    Optional<List<CompanyAccount>> findAllByCompanyId(Long companyId);

    Optional<CompanyAccount> findByCompanyAccountNumber(String companyAccountNumber);


    CompanyAccount findByCompanyIdAndCurrency(Long companyId, Long currencyId);

}
