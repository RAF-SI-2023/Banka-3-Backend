package rs.edu.raf.userservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.userservice.domains.dto.UserDto;
import rs.edu.raf.userservice.domains.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByEmail (String email);
    public Optional<User> findByPhoneNumber(String mobileNumber);
    public Optional<User> findByJmbg(String jmbg);

}
