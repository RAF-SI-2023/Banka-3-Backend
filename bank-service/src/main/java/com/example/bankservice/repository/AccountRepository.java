package com.example.bankservice.repository;

import com.example.bankservice.domain.model.accounts.Account;
import com.example.bankservice.domain.model.accounts.CompanyAccount;
import com.example.bankservice.domain.model.accounts.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<List<UserAccount>> findUserAccountByUserId(Long userId);

    Optional<List<CompanyAccount>> findCompanyAccountByCompanyId(Long companyId);

    Optional<Account> findByAccountNumber(String accountNumber);
}
