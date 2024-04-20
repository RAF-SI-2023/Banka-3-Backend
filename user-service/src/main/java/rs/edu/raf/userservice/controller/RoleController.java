package rs.edu.raf.userservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.userservice.service.RoleService;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/role")
public class RoleController {
    private final RoleService roleService;

    @GetMapping(path = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping(value = "/findByName", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByRoleName(@RequestParam(value = "roleName") String roleName){
        try {
            return ResponseEntity.ok(this.roleService.getRoleByName(roleName));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Couldn't find ROLE with given name: " + roleName);
        }
    }
}
