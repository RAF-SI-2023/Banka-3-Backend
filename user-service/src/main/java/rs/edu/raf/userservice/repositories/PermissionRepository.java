package rs.edu.raf.userservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.userservice.domains.model.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
