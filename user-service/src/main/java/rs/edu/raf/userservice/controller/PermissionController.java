package rs.edu.raf.userservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.userservice.domain.model.Permission;
import rs.edu.raf.userservice.service.PermissionService;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/permission")
public class PermissionController {
    private final PermissionService permissionService;

    @Cacheable(value = "allPermissions")
    @GetMapping(path = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Permission> getAllRoles() {
        return permissionService.getAllPermissions();
    }

    @Cacheable(value = "permissionByName", key = "#permissionName")
    @GetMapping(value = "/findByName", produces = MediaType.APPLICATION_JSON_VALUE)
    public Permission findByRoleName(@RequestParam(value = "permissionName") String permissionName) {
        try {
            return permissionService.getPermissionByName(permissionName);
        }catch (Exception e){
            return null;
        }
    }
}
