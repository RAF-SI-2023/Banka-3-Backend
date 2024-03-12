package rs.edu.raf.userservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.userservice.domains.dto.EmployeeCreateDto;
import rs.edu.raf.userservice.domains.model.Employee;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {


    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByUsername(String username);

    Optional<Employee> findByPhoneNumber(String phoneNumber);

    Optional<Employee> findByJmbg(String jmbg);

    List<Employee> findByPosition(String position);
}
