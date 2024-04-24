package rs.edu.raf.exchangeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import rs.edu.raf.exchangeservice.domain.model.Actuary;

import java.util.List;
import java.util.Optional;

public interface ActuaryRepository extends JpaRepository<Actuary, Long> {

    Optional<Actuary> findByActuaryId (Long id);

    Actuary findByEmployeeId(Long id);

    List<Actuary> findByRole (String role);

    @Modifying
    @Transactional
    @Query("UPDATE Actuary e SET e.limitUsed = 0")
    void updateLimitToZeroForAllEmployees();
}
