package rs.edu.raf.userservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.userservice.domains.model.CompanyAccount;

import java.util.List;
import java.util.Optional;

public interface CompanyAccountRepository extends JpaRepository<CompanyAccount, Long>{
    Optional<List<CompanyAccount>> findByCompany_CompanyId(Long companyId);
    CompanyAccount findByAccountNumber(String accountNumber);
}
