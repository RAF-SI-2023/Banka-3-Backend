package rs.edu.raf.userservice.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.userservice.services.RoleService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/role")
@CrossOrigin
public class RoleController {

    private final RoleService roleService;

    @GetMapping(path = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping(value = "/findByRoleName", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByRoleName(@RequestParam(value = "roleName") String roleName){
        return ResponseEntity.ok(this.roleService.getRoleByName(roleName));
    }

}
