package com.example.bankservice.repository;

import com.example.bankservice.domain.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<List<Account>> findByUserId(Long userId);

    Optional<Account> findByAccountNumber(String accountNumber);
}
