package rs.edu.raf.userservice.bootstrap;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.edu.raf.userservice.domains.model.User;
import rs.edu.raf.userservice.repositories.UserRepository;


@Component
@AllArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        User admin = User.builder()
                .firstName("Admin")
                .lastName("Admin")
                .email("admin@admin.com")
                .jmbg("1111111111")
                .phoneNumber("063111111111")
                .password(passwordEncoder.encode("admin1234"))
                .isActive(true)
                .gender("male")
                .dateOfBirth(1710274123787L)
                .build();


        userRepository.save(admin);

    }
}
