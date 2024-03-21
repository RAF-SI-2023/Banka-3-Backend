package rs.edu.raf.userservice.bootstrap;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.edu.raf.userservice.domains.model.*;
import rs.edu.raf.userservice.domains.model.enums.AccountTypeName;
import rs.edu.raf.userservice.domains.model.enums.CurrencyName;
import rs.edu.raf.userservice.domains.model.enums.PermissionName;
import rs.edu.raf.userservice.domains.model.enums.RoleName;
import rs.edu.raf.userservice.repositories.*;

import java.util.ArrayList;
import java.util.List;


@Component
@AllArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final AccountTypeRepository accountTypeRepository;

    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {

        Role adminRole = Role.builder()
                .roleName(RoleName.ROLE_ADMIN).build();

        Role bankingOfficer = Role.builder()
                .roleName(RoleName.ROLE_BANKING_OFFICER).build();

        Role clientAdvisor = Role.builder()
                .roleName(RoleName.ROLE_CLIENT_ADVISOR).build();

        Role loanOfficer = Role.builder()
                .roleName(RoleName.ROLE_LOAN_OFFICIER).build();

        Role creditOfficer = Role.builder()
                .roleName(RoleName.ROLE_CREDIT_OFFICER).build();

        List<Role> roles = new ArrayList<>();
        roles.add(adminRole);
        roles.add(bankingOfficer);
        roles.add(clientAdvisor);
        roles.add(loanOfficer);
        roles.add(creditOfficer);

        roleRepository.saveAll(roles);

        Permission canTrade = Permission.builder()
                .permissionName(PermissionName.CAN_TRADE).build();

        Permission canWatch = Permission.builder()
                .permissionName(PermissionName.CAN_WATCH).build();

        Permission canSell = Permission.builder()
                .permissionName(PermissionName.CAN_SELL).build();

        Permission canBuy = Permission.builder()
                .permissionName(PermissionName.CAN_BUY).build();

        List<Permission> permissions = new ArrayList<>();
        permissions.add(canTrade);
        permissions.add(canSell);
        permissions.add(canWatch);
        permissions.add(canBuy);

        permissionRepository.saveAll(permissions);

        Employee employee1 = Employee.builder()
                .firstName("Admin")
                .lastName("Admin")
                .username("admin")
                .email("admin@admin.com")
                .jmbg("1111111111")
                .phoneNumber("063111111111")
                .password(passwordEncoder.encode("admin1234"))
                .isActive(true)
                .gender("M")
                .dateOfBirth(1710274123787L)
                .role(adminRole)
                .build();

        Employee employee2 = Employee.builder()
                .firstName("Sladjana")
                .lastName("Kovacevic")
                .username("salter1")
                .email("salter1@salter.com")
                .jmbg("1111111111")
                .phoneNumber("063555555")
                .password(passwordEncoder.encode("admin1234"))
                .isActive(true)
                .gender("F")
                .dateOfBirth(1710274123787L)
                .role(bankingOfficer)
                .permissions(List.of(canSell,canWatch))
                .build();

        Employee employeeJ = Employee.builder()
                .firstName("Jelena")
                .lastName("Drljan")
                .username("jace123")
                .email("jdrljan4620rn@raf.rs")
                .jmbg("1111111111")
                .phoneNumber("063555555")
                .password(passwordEncoder.encode("jace1234"))
                .isActive(true)
                .gender("F")
                .dateOfBirth(1710274123787L)
                .role(bankingOfficer)
                .permissions(List.of(canSell,canWatch))
                .build();

        Employee employee3 = Employee.builder()
                .firstName("Ninoslav")
                .lastName("Zlatanovic")
                .username("nino")
                .email("salter2@salter.com")
                .jmbg("1111111111")
                .phoneNumber("063555555")
                .password(passwordEncoder.encode("admin1234"))
                .isActive(true)
                .gender("M")
                .dateOfBirth(1710274123787L)
                .role(creditOfficer)
                .permissions(List.of(canTrade,canBuy))
                .build();

        Employee employee4 = Employee.builder()
                .firstName("Strahinja")
                .lastName("Ljubicic")
                .username("nino")
                .email("sljubicic7120rn@raf.rs")
                .jmbg("1111111111")
                .phoneNumber("063555555")
                .password(passwordEncoder.encode("admin1234"))
                .isActive(true)
                .gender("M")
                .dateOfBirth(1710274123787L)
                .role(creditOfficer)
                .permissions(List.of(canTrade,canBuy))
                .build();

        List<Employee> employees = new ArrayList<>();
        employees.add(employee1);
        employees.add(employee2);
        employees.add(employee3);
        employees.add(employeeJ);
        employees.add(employee4);

        employeeRepository.saveAll(employees);


        User user1 = User.builder()
                .firstName("User1")
                .lastName("User1")
                .email("user@user.com")
                .jmbg("1111111111")
                .phoneNumber("063111111111")
                .isActive(true)
                .gender("M")
                .dateOfBirth(1710274123787L)
                .build();

        User user2 = User.builder()
                .firstName("Pera")
                .lastName("Pera")
                .email("pera@user.com")
                .jmbg("1111111111")
                .phoneNumber("063111111111")
                .password(passwordEncoder.encode("user1234"))
                .isActive(true)
                .gender("M")
                .dateOfBirth(1710274123787L)
                .build();

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        userRepository.saveAll(users);
        Currency currency = Currency.builder()
                .mark("RSD")
                .name(CurrencyName.DINAR)
                .build();
        AccountType accountType = AccountType.builder()
                        .accountType(AccountTypeName.ZA_MLADE)
                        .monthlyFee(0)
                        .build();
        accountTypeRepository.save(accountType);

        currencyRepository.save(currency);

    }
}
