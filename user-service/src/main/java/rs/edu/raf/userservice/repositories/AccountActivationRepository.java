package rs.edu.raf.userservice.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.userservice.domains.model.AccountActivation;

import java.util.Optional;

public interface AccountActivationRepository extends JpaRepository<AccountActivation, Long> {
    Optional<AccountActivation> findById(Long id);
    Optional<AccountActivation> findByEmailAndCode(String email, int code);
}
