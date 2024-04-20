package rs.edu.raf.userservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.userservice.service.PermissionService;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/permission")
public class PermissionController {
    private final PermissionService permissionService;

    @GetMapping(path = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllRoles() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

    @GetMapping(value = "/findByName", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByRoleName(@RequestParam(value = "permissionName") String permissionName) {
        try {
            return ResponseEntity.ok(this.permissionService.getPermissionByName(permissionName));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Couldn't find PERMISSION with given name: " + permissionName);
        }
    }
}
