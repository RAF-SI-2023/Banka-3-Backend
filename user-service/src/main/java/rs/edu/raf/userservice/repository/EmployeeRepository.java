package rs.edu.raf.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.edu.raf.userservice.domain.model.Employee;
import rs.edu.raf.userservice.domain.model.enums.RoleName;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByUsername(String username);

    @Query("SELECT e FROM Employee e WHERE e.role.roleName = 'ROLE_SUPERVISOR' OR e.role.roleName = 'ROLE_AGENT'")
    Optional<List<Employee>> findSupervisorsAndAgents();

    @Query("SELECT e FROM Employee e WHERE " +
            "(:firstName IS NULL OR LOWER(e.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND " +
            "(:lastName IS NULL OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) AND " +
            "(:email IS NULL OR LOWER(e.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
            "(:role IS NULL OR e.role.roleName = :role)")
    Optional<List<Employee>> findEmployees(@Param("firstName") String firstName,
                                           @Param("lastName") String lastName,
                                           @Param("email") String email,
                                           @Param("role") RoleName role);
}
