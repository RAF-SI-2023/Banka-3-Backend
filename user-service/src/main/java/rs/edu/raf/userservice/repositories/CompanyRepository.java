package rs.edu.raf.userservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.edu.raf.userservice.domains.model.Company;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByPIB(int PIB);
    Optional<Company> findByMatBr(int matBr);
    Optional<Company> findByEmail(String email);
    Optional<Company> findByNumber(String number);

    @Query("SELECT c FROM Company c WHERE " +
            "(:sifraDelatnosti IS NULL OR c.sifraDelatnosti = :sifraDelatnosti)")
    Optional<List<Company>> findCompanies(@Param("sifraDelatnosti") int sifraDelatnosti);
}
