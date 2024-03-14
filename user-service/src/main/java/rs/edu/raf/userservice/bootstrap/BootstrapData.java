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

        List<Permission> permissions = new ArrayList<>();
        permissions.add(addUserPermission);
        permissions.add(readUsersPermission);
        permissionRepository.saveAll(permissions);

        Role adminRole = Role.builder()
                .roleName(RoleName.ADMIN).build();

        Role normalUserRole = Role.builder()
                .roleName(RoleName.USER)
                .build();

        List<Role> roles = new ArrayList<>();
        roles.add(adminRole);
        roles.add(normalUserRole);

        roleRepository.saveAll(roles);

        User user1 = User.builder()
                .firstName("User1")
                .lastName("User1")
                .email("user@user.com")
                .jmbg("1111111111")
                .phoneNumber("063111111111")
                .password(passwordEncoder.encode("user1234"))
                .isActive(true)
                .gender("male")
                .dateOfBirth(1710274123787L)
                .role(normalUserRole)
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
                .role(adminRole)
                .permissions(permissions)
                .build();

        userRepository.save(user1);
        employeeRepository.save(employee);
    }
}
