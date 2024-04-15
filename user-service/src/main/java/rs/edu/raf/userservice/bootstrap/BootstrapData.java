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
    private final CompanyRepository companyRepository;
    private final CompanyAccountRepository companyAccountRepository;

    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    private final CreditRequestRepository creditRequestRepository;
    private final CardRepository cardRepository;

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

        Role exchangeAgent = Role.builder()
                .roleName(RoleName.ROLE_AGENT).build();

        Role exchangeSupervisor= Role.builder()
                .roleName(RoleName.ROLE_SUPERVISOR).build();

        List<Role> roles = new ArrayList<>();
        roles.add(adminRole);
        roles.add(bankingOfficer);
        roles.add(clientAdvisor);
        roles.add(loanOfficer);
        roles.add(creditOfficer);
        roles.add(exchangeAgent);
        roles.add(exchangeSupervisor);

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
                .permissions(List.of(canTrade, canBuy))
                .build();

        Employee employee5 = Employee.builder()
                .firstName("Janko")
                .lastName("Ristic")
                .username("Janko")
                .email("salter3@salter.com")
                .jmbg("1111111111")
                .phoneNumber("063555555")
                .password(passwordEncoder.encode("admin1234"))
                .isActive(true)
                .gender("M")
                .dateOfBirth(1710274123787L)
                .role(exchangeSupervisor)
                .permissions(List.of(canTrade, canBuy))
                .build();

        Employee employee6 = Employee.builder()
                .firstName("Damir")
                .lastName("Ceh")
                .username("Ceh")
                .email("dceh9121rn@raf.rs")
                .jmbg("1111111111")
                .phoneNumber("063555555")
                .password(passwordEncoder.encode("Abc12345"))
                .isActive(true)
                .gender("M")
                .dateOfBirth(1710274123787L)
                .role(exchangeAgent)
                .permissions(List.of(canTrade, canBuy))
                .build();

        Employee employee7 = Employee.builder()
                .firstName("Mina")
                .lastName("Ljubinkovic")
                .username("mina")
                .email("mljubinkovic9120rn@raf.rs")
                .jmbg("1011021030")
                .phoneNumber("066555555")
                .password(passwordEncoder.encode("5a5a5a5a5a"))
                .isActive(true)
                .gender("F")
                .dateOfBirth(1710274123787L)
                .role(bankingOfficer)
                .permissions(List.of(canTrade, canBuy))
                .build();

        List<Employee> employees = new ArrayList<>();
        employees.add(employee1);
        employees.add(employee2);
        employees.add(employee3);
        employees.add(employeeJ);
        employees.add(employee4);
        employees.add(employee5);
        employees.add(employee6);
        employees.add(employee7);

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
        Currency dolar = Currency.builder()
                .mark("USD")
                .name(CurrencyName.DOLAR)
                .build();
        Currency funta = Currency.builder()
                .mark("GBP")
                .name(CurrencyName.FUNTA)
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
        currencyRepository.save(funta);
        currencyRepository.save(dolar);

        Account account1 = new Account();
        account1.setUser(user1);
        account1.setEmployee(employee3);
        account1.setReservedAmount(new BigDecimal(20000.0));
        account1.setAvailableBalance(new BigDecimal(100000.0));
        account1.setAccountNumber("2222222222275396");
        account1.setCreationDate(new Date().getTime());
        account1.setExpireDate(new Date().getTime() + 1000 * 3600);
        account1.setActive(true);
        account1.setCurrency(currency1);
        account1.setAccountType(accountType1);

        Account account1Devizni = new Account();
        account1Devizni.setUser(user1);
        account1Devizni.setEmployee(employee3);
        account1Devizni.setReservedAmount(new BigDecimal(20000.0));
        account1Devizni.setAvailableBalance(new BigDecimal(100000.0));
        account1Devizni.setAccountNumber("8888888888871239");
        account1Devizni.setCreationDate(new Date().getTime());
        account1Devizni.setExpireDate(new Date().getTime() + 1000 * 3600);
        account1Devizni.setActive(true);
        account1Devizni.setCurrency(currency2);
        account1Devizni.setAccountType(accountType1);

        Account account2 = new Account();
        account2.setUser(user3);
        account2.setEmployee(employee3);
        account2.setReservedAmount(new BigDecimal(20000.0));
        account2.setAvailableBalance(new BigDecimal(100000.0));
        account2.setAccountNumber("1111111111174963");
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
        account3.setAccountNumber("3333333333321056");
        account3.setCreationDate(new Date().getTime());
        account3.setExpireDate(new Date().getTime() + 1000 * 3600);
        account3.setActive(true);
        account3.setCurrency(currency2);
        account3.setAccountType(accountType2);

        Account account4 = new Account();
        account4.setUser(user2);
        account4.setEmployee(employee7);
        account4.setReservedAmount(new BigDecimal(20000.0));
        account4.setAvailableBalance(new BigDecimal(100000.0));
        account4.setAccountNumber("9999999999999999");
        account4.setCreationDate(new Date().getTime());
        account4.setExpireDate(new Date().getTime() + 1000 * 3600);
        account4.setActive(true);
        account4.setCurrency(currency1);
        account4.setAccountType(accountType2);

        Account account5 = new Account();
        account5.setUser(user4);
        account5.setEmployee(employee3);
        account5.setReservedAmount(new BigDecimal(200000.0));
        account5.setAvailableBalance(new BigDecimal(0.0));
        account5.setAccountNumber("1258963400124583");
        account5.setCreationDate(new Date().getTime());
        account5.setExpireDate(new Date().getTime() + 1000 * 3600);
        account5.setActive(true);
        account5.setCurrency(currency1);
        account5.setAccountType(accountType2);

        Account account6 = new Account();
        account6.setUser(user4);
        account6.setEmployee(employee3);
        account6.setReservedAmount(new BigDecimal(200000.0));
        account6.setAvailableBalance(new BigDecimal(0.0));
        account6.setAccountNumber("4489205063987420");
        account6.setCreationDate(new Date().getTime());
        account6.setExpireDate(new Date().getTime() + 1000 * 3600);
        account6.setActive(true);
        account6.setCurrency(currency2);
        account6.setAccountType(accountType2);

        accountRepository.save(account1);
        accountRepository.save(account1Devizni);
        accountRepository.save(account2);
        accountRepository.save(account3);
        accountRepository.save(account4);
        accountRepository.save(account5);
        accountRepository.save(account6);

        Company banka = new Company();
        banka.setTitle("banka 3");
        banka.setNumber("0113161088");
        banka.setPib(123123123);
        banka.setMaticniBroj(98989898);
        banka.setSifraDelatnosti(6411);
        banka.setEmail("dpopovic10720rn@raf.rs");

        CompanyAccount bankaRsd = new CompanyAccount();
        bankaRsd.setCompany(banka);
        bankaRsd.setAccountNumber("1010101010101010");
        bankaRsd.setBalance(new BigDecimal("10000000000000000.0"));
        bankaRsd.setAvailableBalance(new BigDecimal("10000000000000000.0"));
        bankaRsd.setReservedAmount(new BigDecimal("0.0"));
        bankaRsd.setEmployee(employee3);
        bankaRsd.setCreationDate(new Date().getTime());
        bankaRsd.setExpireDate(new Date().getTime() + 1000 * 3600);
        bankaRsd.setActive(true);
        bankaRsd.setCurrency(currency1);

        CompanyAccount bankaEur = new CompanyAccount();
        bankaEur.setCompany(banka);
        bankaEur.setAccountNumber("2020202020202020");
        bankaEur.setBalance(new BigDecimal("10000000000000000.0"));
        bankaEur.setAvailableBalance(new BigDecimal("10000000000000000.0"));
        bankaEur.setReservedAmount(new BigDecimal("0.0"));
        bankaEur.setEmployee(employee3);
        bankaEur.setCreationDate(new Date().getTime());
        bankaEur.setExpireDate(new Date().getTime() + 1000 * 3600);
        bankaEur.setActive(true);
        bankaEur.setCurrency(currency2);

        CompanyAccount bankaUsd = new CompanyAccount();
        bankaUsd.setCompany(banka);
        bankaUsd.setAccountNumber("3030303030303030");
        bankaUsd.setBalance(new BigDecimal("10000000000000000.0"));
        bankaUsd.setAvailableBalance(new BigDecimal("10000000000000000.0"));
        bankaUsd.setReservedAmount(new BigDecimal("0.0"));
        bankaUsd.setEmployee(employee3);
        bankaUsd.setCreationDate(new Date().getTime());
        bankaUsd.setExpireDate(new Date().getTime() + 1000 * 3600);
        bankaUsd.setActive(true);
        bankaUsd.setCurrency(dolar);

        CompanyAccount bankaGbp = new CompanyAccount();
        bankaGbp.setCompany(banka);
        bankaGbp.setAccountNumber("6060606060606060");
        bankaGbp.setBalance(new BigDecimal("10000000000000000.0"));
        bankaGbp.setAvailableBalance(new BigDecimal("10000000000000000.0"));
        bankaGbp.setReservedAmount(new BigDecimal("0.0"));
        bankaGbp.setEmployee(employee3);
        bankaGbp.setCreationDate(new Date().getTime());
        bankaGbp.setExpireDate(new Date().getTime() + 1000 * 3600);
        bankaGbp.setActive(true);
        bankaGbp.setCurrency(funta);

        Company stock = new Company();
        stock.setTitle("stock");
        stock.setNumber("0113161077");
        stock.setPib(456456456);
        stock.setMaticniBroj(78787878);
        stock.setSifraDelatnosti(5555);
        stock.setEmail("mljubinkovic9120rn@raf.rs");

        CompanyAccount stockRsd = new CompanyAccount();
        stockRsd.setCompany(stock);
        stockRsd.setAccountNumber("1111111111111111");
        stockRsd.setBalance(BigDecimal.valueOf(10000000000000000.0));
        stockRsd.setAvailableBalance(BigDecimal.valueOf(10000000000000000.0));
        stockRsd.setReservedAmount(BigDecimal.ZERO);
        stockRsd.setEmployee(employee7);
        stockRsd.setCreationDate(new Date().getTime());
        stockRsd.setExpireDate(new Date().getTime() + 1000 * 3600);
        stockRsd.setActive(true);
        stockRsd.setCurrency(currency1);

        CompanyAccount stockEur = new CompanyAccount();
        stockEur.setCompany(stock);
        stockEur.setAccountNumber("2222222222222222");
        stockEur.setBalance(BigDecimal.valueOf(10000000000000000.0));
        stockEur.setAvailableBalance(BigDecimal.valueOf(10000000000000000.0));
        stockEur.setReservedAmount(BigDecimal.ZERO);
        stockEur.setEmployee(employee7);
        stockEur.setCreationDate(new Date().getTime());
        stockEur.setExpireDate(new Date().getTime() + 1000 * 3600);
        stockEur.setActive(true);
        stockEur.setCurrency(currency2);

        CompanyAccount stockUsd = new CompanyAccount();
        stockUsd.setCompany(stock);
        stockUsd.setAccountNumber("3333333333333333");
        stockUsd.setBalance(BigDecimal.valueOf(10000000000000000.0));
        stockUsd.setAvailableBalance(BigDecimal.valueOf(10000000000000000.0));
        stockUsd.setReservedAmount(BigDecimal.ZERO);
        stockUsd.setEmployee(employee7);
        stockUsd.setCreationDate(new Date().getTime());
        stockUsd.setExpireDate(new Date().getTime() + 1000 * 3600);
        stockUsd.setActive(true);
        stockUsd.setCurrency(dolar);

        CompanyAccount stockGbp = new CompanyAccount();
        stockGbp.setCompany(stock);
        stockGbp.setAccountNumber("6666666666666666");
        stockGbp.setBalance(BigDecimal.valueOf(10000000000000000.0));
        stockGbp.setAvailableBalance(BigDecimal.valueOf(10000000000000000.0));
        stockGbp.setReservedAmount(BigDecimal.ZERO);
        stockGbp.setEmployee(employee7);
        stockGbp.setCreationDate(new Date().getTime());
        stockGbp.setExpireDate(new Date().getTime() + 1000 * 3600);
        stockGbp.setActive(true);
        stockGbp.setCurrency(funta);

        companyRepository.save(banka);
        companyAccountRepository.save(bankaRsd);
        companyAccountRepository.save(bankaEur);
        companyAccountRepository.save(bankaUsd);
        companyAccountRepository.save(bankaGbp);

        companyRepository.save(stock);
        companyAccountRepository.save(stockRsd);
        companyAccountRepository.save(stockEur);
        companyAccountRepository.save(stockUsd);
        companyAccountRepository.save(stockGbp);


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

        Card card1 = new Card();
        card1.setAccountNumber(account5.getAccountNumber());
        card1.setCardName("Visa");
        card1.setCardNumber("12345678");
        card1.setCreationDate(new Date().getTime());
        card1.setCvv("123");
        card1.setExpireDate(new Date(2026,12,30).getTime());
        card1.setStatus(true);

        cardRepository.save(card1);

    }
}
