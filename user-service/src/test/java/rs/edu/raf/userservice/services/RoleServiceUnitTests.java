package rs.edu.raf.userservice.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.userservice.domains.model.Role;
import rs.edu.raf.userservice.domains.model.enums.RoleName;
import rs.edu.raf.userservice.repositories.RoleRepository;
import rs.edu.raf.userservice.services.RoleService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class RoleServiceUnitTests {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    public void testGetAllRoles() {

        Role adminRole = new Role();
        adminRole.setRoleName(RoleName.ROLE_ADMIN);

        Role loanOfficierRole = new Role();
        loanOfficierRole.setRoleName(RoleName.ROLE_LOAN_OFFICIER);

        Role clientAdvisorRole = new Role();
        clientAdvisorRole.setRoleName(RoleName.ROLE_CLIENT_ADVISOR);

        Role bankingOfficerRole = new Role();
        bankingOfficerRole.setRoleName(RoleName.ROLE_BANKING_OFFICER);

        Role creditOfficierRole = new Role();
        creditOfficierRole.setRoleName(RoleName.ROLE_CREDIT_OFFICER);

        List<Role> rolesList = List.of(adminRole, loanOfficierRole, clientAdvisorRole, bankingOfficerRole, creditOfficierRole);

        given(roleRepository.findAll()).willReturn(rolesList);

        List<Role> roles = roleService.getAllRoles();
        assertEquals(rolesList, roles);
    }

    @Test
    public void testGetRoleByName(){
        Role adminRole = new Role();
        adminRole.setRoleName(RoleName.ROLE_ADMIN);

        given(roleRepository.findByRoleName("ROLE_ADMIN")).willReturn(adminRole);

        Role role = roleService.getRoleByName("ROLE_ADMIN");

        assertEquals(adminRole.getRoleName(), role.getRoleName());
    }

}
