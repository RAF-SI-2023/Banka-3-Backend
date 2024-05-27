package com.example.bankservice.repository;

import com.example.bankservice.domain.model.Currency;
import com.example.bankservice.domain.model.accounts.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    Optional<List<UserAccount>> findAllByUserId(Long userId);

    UserAccount findByUserIdAndCurrency(Long userId, Currency currency);
}
