package rs.edu.raf.userservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.userservice.domains.model.AccountType;
import rs.edu.raf.userservice.domains.model.enums.AccountTypeName;

import java.util.Optional;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountType, Long> {

    Optional<AccountType> findByAccountType(AccountTypeName name);
}
