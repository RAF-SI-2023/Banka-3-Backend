package rs.edu.raf.userservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.userservice.domains.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
