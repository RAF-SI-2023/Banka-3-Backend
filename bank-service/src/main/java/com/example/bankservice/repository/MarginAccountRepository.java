package com.example.bankservice.repository;

import com.example.bankservice.domain.model.accounts.Account;
import com.example.bankservice.domain.model.accounts.CompanyAccount;
import com.example.bankservice.domain.model.accounts.UserAccount;
import com.example.bankservice.domain.model.marginAccounts.CompanyMarginAccount;
import com.example.bankservice.domain.model.marginAccounts.MarginAccount;
import com.example.bankservice.domain.model.marginAccounts.UserMarginAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarginAccountRepository extends JpaRepository<MarginAccount,Long> {

    Optional<UserMarginAccount> findUserMarginAccountByUserId(Long userId);

    Optional<CompanyMarginAccount> findCompanyMarginAccountByCompanyId(Long companyId);

    Optional<MarginAccount> findByAccountNumber(String accountNumber);
}
