package rs.edu.raf.userservice.bootstrap;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.edu.raf.userservice.domains.model.*;
import rs.edu.raf.userservice.domains.model.enums.*;
import rs.edu.raf.userservice.repositories.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
    private final AccountRepository accountRepository;
    private final CreditRequestRepository creditRequestRepository;

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
                .permissions(List.of(canSell, canWatch))
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
                .permissions(List.of(canSell, canWatch))
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
                .permissions(List.of(canTrade, canBuy))
                .build();

        Employee employee4 = Employee.builder()
                .firstName("Ognjen")
                .lastName("Stojanovic")
                .username("ostojanovic")
                .email("ostojanovic10021rn@raf.rs")
                .jmbg("1111111111")
                .phoneNumber("063555555")
                .password(passwordEncoder.encode("Abc12345"))
                .isActive(true)
                .gender("M")
                .dateOfBirth(1710274123787L)
                .role(adminRole)
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
                .password(passwordEncoder.encode("user1234"))
                .jmbg("1111111111")
                .phoneNumber("063111111111")
                .isActive(true)
                .gender("M")
                .dateOfBirth(1710274123787L)
                .codeActive(true)
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
                .codeActive(true)
                .build();

        User user3 = User.builder()
                .firstName("Strahinja")
                .lastName("Ljubicic")
                .email("sljubicic7120rn@raf.rs")
                .jmbg("1111111111")
                .phoneNumber("063555555")
                .isActive(true)
                .gender("M")
                .dateOfBirth(1710274123787L)
                .password(passwordEncoder.encode("strahinja1234"))
                .codeActive(true)
                .build();

        User user4 = User.builder()
                .firstName("Ognjen")
                .lastName("Stojanovic")
                .email("ostojanovic10021rn@raf.rs")
                .jmbg("1111111111")
                .phoneNumber("063555555")
                .isActive(true)
                .gender("M")
                .dateOfBirth(1710274123787L)
                .codeActive(false)
                .build();

        User user5 = User.builder()
                .firstName("User")
                .lastName("BezSifre")
                .email("userBezSifre@gmail.com")
                .jmbg("1111111111")
                .phoneNumber("063555555")
                .isActive(true)
                .gender("M")
                .dateOfBirth(1710274123787L)
                .codeActive(false)
                .build();

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        users.add(user5);


        userRepository.saveAll(users);

        Currency currency1 = Currency.builder()
                .mark("RSD")
                .name(CurrencyName.DINAR)
                .build();
        Currency currency2 = Currency.builder()
                .mark("EUR")
                .name(CurrencyName.EURO)
                .build();
        AccountType accountType1 = AccountType.builder()
                .accountType(AccountTypeName.ZA_MLADE)
                .monthlyFee(0)
                .build();

        AccountType accountType2 = AccountType.builder()
                .accountType(AccountTypeName.LICNI)
                .monthlyFee(0)
                .build();
        accountTypeRepository.save(accountType1);
        accountTypeRepository.save(accountType2);

        currencyRepository.save(currency1);
        currencyRepository.save(currency2);

        Account account1 = new Account();
        account1.setUser(user1);
        account1.setEmployee(employee3);
        account1.setReservedAmount(new BigDecimal(20000.0));
        account1.setAvailableBalance(new BigDecimal(100000.0));
        account1.setAccountNumber("22222222222");
        account1.setCreationDate(new Date().getTime());
        account1.setExpireDate(new Date().getTime() + 1000 * 3600);
        account1.setActive(true);
        account1.setCurrency(currency1);
        account1.setAccountType(accountType1);

        Account account2 = new Account();
        account2.setUser(user3);
        account2.setEmployee(employee3);
        account2.setReservedAmount(new BigDecimal(20000.0));
        account2.setAvailableBalance(new BigDecimal(100000.0));
        account2.setAccountNumber("11111111111");
        account2.setCreationDate(new Date().getTime());
        account2.setExpireDate(new Date().getTime() + 1000 * 3600);
        account2.setActive(true);
        account2.setCurrency(currency1);
        account2.setAccountType(accountType2);

        Account account3 = new Account();
        account3.setUser(user3);
        account3.setEmployee(employee3);
        account3.setReservedAmount(new BigDecimal(20000.0));
        account3.setAvailableBalance(new BigDecimal(100000.0));
        account3.setAccountNumber("33333333333");
        account3.setCreationDate(new Date().getTime());
        account3.setExpireDate(new Date().getTime() + 1000 * 3600);
        account3.setActive(true);
        account3.setCurrency(currency2);
        account3.setAccountType(accountType2);

        accountRepository.save(account1);
        accountRepository.save(account2);
        accountRepository.save(account3);

        CreditRequest creditRequest = new CreditRequest();
        creditRequest.setUser(user1);
        creditRequest.setName("name");
        creditRequest.setAccountNumber("123");
        creditRequest.setAmount(1000.00);
        creditRequest.setApplianceReason("reason");
        creditRequest.setMonthlyPaycheck(100.00);
        creditRequest.setEmployed(true);
        creditRequest.setDateOfEmployment(100L);
        creditRequest.setPaymentPeriod(50);
        creditRequest.setStatus(CreditRequestStatus.PROCESSING);
        creditRequest.setCurrencyMark("rsd");

        creditRequestRepository.save(creditRequest);

    }
}
