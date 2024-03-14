package rs.edu.raf.userservice;

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
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class RoleServiceUnitTests {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    public void testGetAllRoles() {

        Role userRole = new Role();
        userRole.setRoleName(RoleName.USER);
        Role adminRole = new Role();
        userRole.setRoleName(RoleName.ADMIN);
        Role employeeRole = new Role();
        userRole.setRoleName(RoleName.EMPLOYEE);

        List<Role> rolesList = List.of(userRole, adminRole, employeeRole);

        given(roleRepository.findAll()).willReturn(rolesList);

        List<Role> roles = roleService.getAllRoles();
        assertEquals(rolesList, roles);
    }
}
