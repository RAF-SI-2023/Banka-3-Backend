package rs.edu.raf.userservice.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.userservice.domain.model.Permission;
import rs.edu.raf.userservice.domain.model.enums.PermissionName;
import rs.edu.raf.userservice.repository.PermissionRepository;
import rs.edu.raf.userservice.service.PermissionService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PermissionServiceUnitTests {

    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private PermissionService permissionService;

    @Test
    public void testGetAllPermissions() {

        Permission canBuyPermission = new Permission();
        canBuyPermission.setPermissionName(PermissionName.CAN_BUY);
        Permission canWatchPermission = new Permission();
        canWatchPermission.setPermissionName(PermissionName.CAN_WATCH);
        Permission canTradePermission = new Permission();
        canTradePermission.setPermissionName(PermissionName.CAN_TRADE);
        Permission canSellPermission = new Permission();
        canSellPermission.setPermissionName(PermissionName.CAN_SELL);

        List<Permission> permissionList = List.of(canBuyPermission, canWatchPermission, canTradePermission, canSellPermission);

        given(permissionRepository.findAll()).willReturn(permissionList);

        List<Permission> permissions = permissionService.getAllPermissions();
        assertEquals(permissionList, permissions);
    }

    @Test
    public void testGetPermissionByName() {
        Permission canBuyPermission = new Permission();
        canBuyPermission.setPermissionName(PermissionName.CAN_BUY);

        given(permissionRepository.findByPermissionName(PermissionName.valueOf("CAN_BUY"))).willReturn(Optional.of(canBuyPermission));

        Permission permission = permissionService.getPermissionByName("CAN_BUY");

        assertEquals(permission.getPermissionName(), canBuyPermission.getPermissionName());
    }

    @Test
    void getRoleByName_WhenRoleNotFound_ShouldThrowRuntimeException() {
        // Arrange
        String permissionName = "CAN_BUY";

        given(permissionRepository.findByPermissionName(PermissionName.valueOf(permissionName))).willReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            permissionService.getPermissionByName(permissionName);
        });

        assertEquals("Permission not found", exception.getMessage());
        verify(permissionRepository, times(1)).findByPermissionName(PermissionName.valueOf(permissionName));
    }

}