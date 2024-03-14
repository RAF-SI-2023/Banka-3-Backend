package rs.edu.raf.userservice.bootstrap;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.edu.raf.userservice.domains.model.Employee;
import rs.edu.raf.userservice.domains.model.Permission;
import rs.edu.raf.userservice.domains.model.Role;
import rs.edu.raf.userservice.domains.model.User;
import rs.edu.raf.userservice.domains.model.enums.PermissionName;
import rs.edu.raf.userservice.domains.model.enums.RoleName;
import rs.edu.raf.userservice.repositories.EmployeeRepository;
import rs.edu.raf.userservice.repositories.PermissionRepository;
import rs.edu.raf.userservice.repositories.RoleRepository;
import rs.edu.raf.userservice.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;


@Component
@AllArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        Permission addUserPermission = Permission.builder()
                .permissionName(PermissionName.ADD_USERS)
                .build();

        Permission readUsersPermission = Permission.builder()
                .permissionName(PermissionName.READ_USERS)
                .build();
        permissionRepository.save(addUserPermission);
        permissionRepository.save(readUsersPermission);

        List<Permission> permissions = new ArrayList<>();
        permissions.add(addUserPermission);
        permissions.add(readUsersPermission);

        Role role = Role.builder()
                .roleName(RoleName.ADMIN)
                .permissions(permissions)
                .build();

        roleRepository.save(role);

        User admin = User.builder()
                .firstName("User1")
                .lastName("User1")
                .email("user@user.com")
                .jmbg("1111111111")
                .phoneNumber("063111111111")
                .password(passwordEncoder.encode("user1234"))
                .isActive(true)
                .gender("male")
                .dateOfBirth(1710274123787L)
                .build();

        Employee employee = Employee.builder()
                .firstName("Admin")
                .lastName("Admin")
                .username("admin")
                .email("admin@admin.com")
                .jmbg("1111111111")
                .phoneNumber("063111111111")
                .password(passwordEncoder.encode("admin1234"))
                .isActive(true)
                .gender("male")
                .dateOfBirth(1710274123787L)
                .role(role)
                .build();

        userRepository.save(admin);
        employeeRepository.save(employee);
    }
}
