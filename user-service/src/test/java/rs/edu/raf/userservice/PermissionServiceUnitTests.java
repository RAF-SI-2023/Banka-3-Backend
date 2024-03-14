package rs.edu.raf.userservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.userservice.domains.model.Permission;
import rs.edu.raf.userservice.domains.model.enums.PermissionName;
import rs.edu.raf.userservice.repositories.PermissionRepository;
import rs.edu.raf.userservice.services.PermissionService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class PermissionServiceUnitTests {

    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private PermissionService permissionService;

    @Test
    public void testGetAllPermissions() {

        Permission addPermission = new Permission();
        addPermission.setPermissionName(PermissionName.ADD_USERS);
        Permission readPermission = new Permission();
        readPermission.setPermissionName(PermissionName.READ_USERS);
        Permission createPermission = new Permission();
        createPermission.setPermissionName(PermissionName.CREATE_USERS);
        Permission updatePermission = new Permission();
        updatePermission.setPermissionName(PermissionName.UPDATE_USERS);
        Permission deletePermission = new Permission();
        deletePermission.setPermissionName(PermissionName.DELETE_USERS);

        List<Permission> permissionList = List.of(addPermission, readPermission, createPermission, updatePermission, deletePermission);

        given(permissionRepository.findAll()).willReturn(permissionList);

        List<Permission> permissions = permissionService.getAllPermissions();
        assertEquals(permissionList, permissions);
    }
}
