package rs.edu.raf.userservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.userservice.domains.model.ForeignAccount;

import java.util.List;
import java.util.Optional;

@Repository
public interface ForeignAccountRepository extends JpaRepository<ForeignAccount, Long>{
    Optional<ForeignAccount> findByAccountNumber(String accountNumber);
    Optional<List<ForeignAccount>> findByUser_UserId(Long userId);
}
