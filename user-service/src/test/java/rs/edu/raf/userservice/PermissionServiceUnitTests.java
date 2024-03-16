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
    public void testGetPermissionByName(){
        Permission canBuyPermission = new Permission();
        canBuyPermission.setPermissionName(PermissionName.CAN_BUY);

        given(permissionRepository.findByPermissionName("CAN_BUY")).willReturn(canBuyPermission);

        Permission permission = permissionService.getPermissionByName("CAN_BUY");

        assertEquals(permission.getPermissionName(), canBuyPermission.getPermissionName());
    }

}
