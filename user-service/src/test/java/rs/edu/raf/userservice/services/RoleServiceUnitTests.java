package rs.edu.raf.userservice.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.userservice.domain.model.Role;
import rs.edu.raf.userservice.domain.model.enums.RoleName;
import rs.edu.raf.userservice.repository.RoleRepository;
import rs.edu.raf.userservice.service.RoleService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

        Role clientAdvisorRole = new Role();
        clientAdvisorRole.setRoleName(RoleName.ROLE_CLIENT_ADVISOR);

        Role bankingOfficerRole = new Role();
        bankingOfficerRole.setRoleName(RoleName.ROLE_BANKING_OFFICER);

        Role creditOfficierRole = new Role();
        creditOfficierRole.setRoleName(RoleName.ROLE_CREDIT_OFFICER);

        List<Role> rolesList = List.of(adminRole, clientAdvisorRole, bankingOfficerRole, creditOfficierRole);

        given(roleRepository.findAll()).willReturn(rolesList);

        List<Role> roles = roleService.getAllRoles();
        assertEquals(rolesList, roles);
    }

    @Test
    public void testGetRoleByName() {
        Role adminRole = new Role();
        adminRole.setRoleName(RoleName.ROLE_ADMIN);

        given(roleRepository.findByRoleName(RoleName.valueOf("ROLE_ADMIN"))).willReturn(Optional.of(adminRole));

        Role role = roleService.getRoleByName("ROLE_ADMIN");

        assertEquals(adminRole.getRoleName(), role.getRoleName());
    }

    @Test
    void getRoleByName_WhenRoleNotFound_ShouldThrowRuntimeException() {
        // Arrange
        String roleName = "ROLE_ADMIN";

        given(roleRepository.findByRoleName(RoleName.valueOf(roleName))).willReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            roleService.getRoleByName(roleName);
        });

        assertEquals("Role not found", exception.getMessage());
        verify(roleRepository, times(1)).findByRoleName(RoleName.valueOf(roleName));
    }

}