package rs.edu.raf.userservice.bootstrap;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.edu.raf.userservice.domain.model.*;
import rs.edu.raf.userservice.domain.model.enums.PermissionName;
import rs.edu.raf.userservice.domain.model.enums.RoleName;
import rs.edu.raf.userservice.repository.*;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Component
public class BootstrapData implements CommandLineRunner {
    private final CompanyRepository companyRepository;
    private final ContactRepository contactRepository;
    private final EmployeeRepository employeeRepository;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (employeeRepository.count() == 0){
            //ROLES
            Role adminRole = Role.builder().roleName(RoleName.ROLE_ADMIN).build();
            Role bankingOfficer = Role.builder().roleName(RoleName.ROLE_BANKING_OFFICER).build();
            Role clientAdvisor = Role.builder().roleName(RoleName.ROLE_CLIENT_ADVISOR).build();
            Role creditOfficer = Role.builder().roleName(RoleName.ROLE_CREDIT_OFFICER).build();
            Role exchangeSupervisor = Role.builder().roleName(RoleName.ROLE_SUPERVISOR).build();
            Role exchangeAgent = Role.builder().roleName(RoleName.ROLE_AGENT).build();
            roleRepository.saveAll(Arrays.asList(adminRole, bankingOfficer, clientAdvisor, creditOfficer, exchangeAgent, exchangeSupervisor));

            //PERMISSIONS
            Permission canTrade = Permission.builder().permissionName(PermissionName.CAN_TRADE).build();
            Permission canWatch = Permission.builder().permissionName(PermissionName.CAN_WATCH).build();
            Permission canSell = Permission.builder().permissionName(PermissionName.CAN_SELL).build();
            Permission canBuy = Permission.builder().permissionName(PermissionName.CAN_BUY).build();
            permissionRepository.saveAll(Arrays.asList(canTrade, canWatch, canSell, canBuy));

            //ADMIN
            Employee employee1 = Employee.builder()
                    .firstName("Admin")
                    .lastName("Admin")
                    .username("admin")
                    .email("admin@admin.com")
                    .jmbg("2907002710058")
                    .phoneNumber("063128825")
                    .password(passwordEncoder.encode("admin1234"))
                    .isActive(true)
                    .gender("M")
                    .dateOfBirth(1710274123787L)
                    .role(adminRole)
                    .permissions(List.of(canTrade, canBuy, canSell, canWatch))
                    .build();

            //BANKING OFFICER
            Employee employee2 = Employee.builder()
                    .firstName("Sladjana")
                    .lastName("Kovacevic")
                    .username("salter1")
                    .email("salter@banka.com")
                    .jmbg("0612994613312")
                    .phoneNumber("063555555")
                    .password(passwordEncoder.encode("employee1234"))
                    .isActive(true)
                    .gender("F")
                    .dateOfBirth(1710274123787L)
                    .role(bankingOfficer)
                    .permissions(List.of(canSell, canWatch))
                    .build();

            //CLIENT ADVISOR
            Employee employee3 = Employee.builder()
                    .firstName("Jelena")
                    .lastName("Drljan")
                    .username("jace123")
                    .email("jdrljan@banka.com")
                    .jmbg("2310017309221")
                    .phoneNumber("061182237")
                    .password(passwordEncoder.encode("employee1234"))
                    .isActive(true)
                    .gender("F")
                    .dateOfBirth(1710274123787L)
                    .role(clientAdvisor)
                    .permissions(List.of(canSell, canWatch))
                    .build();

            //CREDIT OFFICER
            Employee employee4 = Employee.builder()
                    .firstName("Ninoslav")
                    .lastName("Zlatanovic")
                    .username("nino")
                    .email("credit@banka.com")
                    .jmbg("1308001774106")
                    .phoneNumber("063527544")
                    .password(passwordEncoder.encode("employee1234"))
                    .isActive(true)
                    .gender("M")
                    .dateOfBirth(1710274123787L)
                    .role(creditOfficer)
                    .permissions(List.of(canTrade, canBuy))
                    .build();

            //SUPERVISOR
            Employee employee5 = Employee.builder()
                    .firstName("Janko")
                    .lastName("Ristic")
                    .username("jane")
                    .email("jristic@banka.com")
                    .jmbg("2006001710058")
                    .phoneNumber("062224121")
                    .password(passwordEncoder.encode("employee1234"))
                    .isActive(true)
                    .gender("M")
                    .dateOfBirth(1710274123787L)
                    .role(exchangeSupervisor)
                    .permissions(List.of(canTrade, canBuy, canWatch, canBuy))
                    .build();

            //AGENT
            Employee employee6 = Employee.builder()
                    .firstName("Mina")
                    .lastName("Ljubinkovic")
                    .username("mina")
                    .email("mljubinkovic@banka.com")
                    .jmbg("1503995840063")
                    .phoneNumber("066732011")
                    .password(passwordEncoder.encode("employee1234"))
                    .isActive(true)
                    .gender("F")
                    .dateOfBirth(1710274123787L)
                    .role(exchangeAgent)
                    .permissions(List.of(canTrade, canBuy))
                    .build();

            employeeRepository.saveAll(Arrays.asList(employee1, employee2, employee3, employee4, employee5, employee6));

            User user1 = User.builder()
                    .firstName("Janko")
                    .lastName("Ristic")
                    .email("jristic3620rn@raf.rs")
                    .password(passwordEncoder.encode("user1234"))
                    .jmbg("0101001710058")
                    .phoneNumber("061445109")
                    .isActive(true)
                    .gender("M")
                    .dateOfBirth(1710274123787L)
                    .codeActive(true)
                    .build();

            User user2 = User.builder()
                    .firstName("Strahinja")
                    .lastName("Ljubicic")
                    .email("sljubicic7120rn@raf.rs")
                    .jmbg("2211001710058")
                    .phoneNumber("064132211")
                    .isActive(true)
                    .gender("M")
                    .dateOfBirth(1710274123787L)
                    .password(passwordEncoder.encode("user1234"))
                    .codeActive(true)
                    .build();

            User user3 = User.builder()
                    .firstName("Ognjen")
                    .lastName("Stojanovic")
                    .email("ostojanovic10021rn@raf.rs")
                    .jmbg("1503001713368")
                    .phoneNumber("060157799")
                    .isActive(true)
                    .gender("M")
                    .dateOfBirth(1710274123787L)
                    .codeActive(false)
                    .build();

            User user4 = User.builder()
                    .firstName("Milos")
                    .lastName("Krasic")
                    .email("jane06.ristic@gmail.com")
                    .jmbg("1808965710191")
                    .phoneNumber("0668874984")
                    .isActive(true)
                    .gender("M")
                    .dateOfBirth(1710274123787L)
                    .password(passwordEncoder.encode("user1234"))
                    .codeActive(true)
                    .build();

            userRepository.saveAll(Arrays.asList(user1, user2, user3, user4));

            //CONTACTS
            Contact contact1 = Contact.builder()
                    .user(user1)
                    .name("Strahinja Ljubicic")
                    .myName("strale dinarski")
                    .accountNumber("2222222222222222")
                    .build();

            Contact contact2 = Contact.builder()
                    .user(user1)
                    .name("Strahinja Ljubicic")
                    .myName("strale eurski")
                    .accountNumber("3213213213213213")
                    .build();

            Contact contact3 = Contact.builder()
                    .user(user2)
                    .name("Janko Ristic")
                    .myName("janko dinarski")
                    .accountNumber("1111111111111111").build();

            Contact contact4 = Contact.builder()
                    .user(user2)
                    .name("Janko Ristic")
                    .myName("janko eurski")
                    .accountNumber("1231231231231231").build();

            contactRepository.saveAll(Arrays.asList(contact1, contact2, contact3, contact4));

            //BANK
            Company bank = Company.builder()
                    .title("Banka3")
                    .number("011256481")
                    .PIB(123123123)
                    .maticniBroj(98989898)
                    .sifraDelatnosti(6411)
                    .email("banak.tri@gmail.com")
                    .password(passwordEncoder.encode("Banka3najbolja"))
                    .codeActive(true)
                    .isActive(true)
                    .build();

            //EXCHANGE
            Company stock = Company.builder()
                    .title("Exchange")
                    .number("011316107")
                    .PIB(456456456)
                    .maticniBroj(78787878)
                    .sifraDelatnosti(5533)
                    .email("exchange@gmail.com")
                    .password(passwordEncoder.encode("exchange1234"))
                    .codeActive(true)
                    .isActive(true)
                    .build();

            //GOOD COMPANY
            Company goodCompany = Company.builder()
                    .title("GoodCompany")
                    .number("0113161088")
                    .PIB(456456411)
                    .maticniBroj(88997711)
                    .sifraDelatnosti(5533)
                    .email("goodcompany@gmail.com")
                    .password(passwordEncoder.encode("goodcompany1234"))
                    .codeActive(true)
                    .isActive(true)
                    .build();

            //BAD COMPANY
            Company badCompany = Company.builder()
                    .title("BadCompany")
                    .number("0113161099")
                    .PIB(456456441)
                    .maticniBroj(88997722)
                    .sifraDelatnosti(5533)
                    .email("badcompany@gmail.com")
                    .password(passwordEncoder.encode("badcompany1234"))
                    .codeActive(true)
                    .isActive(true)
                    .build();

            //BANKA 4
            Company bank4 = Company.builder()
                    .title("Banka4")
                    .number("0113161099")
                    .PIB(456456441)
                    .maticniBroj(88997722)
                    .sifraDelatnosti(5533)
                    .email("banka4@banka.com")
                    .password(passwordEncoder.encode("banka41234"))
                    .codeActive(true)
                    .isActive(true)
                    .build();

            companyRepository.saveAll(Arrays.asList(bank, stock, goodCompany, badCompany, bank4));
        }
    }
}
