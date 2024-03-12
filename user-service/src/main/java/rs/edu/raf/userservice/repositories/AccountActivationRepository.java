package zews.Email_Service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zews.Email_Service.entity.AccountActivation;

import java.util.Optional;

public interface AccountActivationRepository extends JpaRepository<AccountActivation, Long> {
    Optional<AccountActivation> findById(Long id);
    Optional<AccountActivation> findByEmailAndCode(String email, int code);
}
