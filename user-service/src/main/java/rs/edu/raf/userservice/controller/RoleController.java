package rs.edu.raf.userservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.userservice.domain.model.Role;
import rs.edu.raf.userservice.service.RoleService;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/role")
public class RoleController {
    private final RoleService roleService;

    @Cacheable(value = "allRoles")
    @GetMapping(path = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @Cacheable(value = "roleByName", key = "#roleName")
    @GetMapping(value = "/findByName", produces = MediaType.APPLICATION_JSON_VALUE)
    public Role findByRoleName(@RequestParam(value = "roleName") String roleName){
        try {
            return this.roleService.getRoleByName(roleName);
        }catch (Exception e){
            return null;
        }
    }
}
