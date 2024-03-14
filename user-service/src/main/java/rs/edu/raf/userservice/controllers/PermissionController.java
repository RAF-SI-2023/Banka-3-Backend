package rs.edu.raf.userservice.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.edu.raf.userservice.services.PermissionService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/permission")
@CrossOrigin
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping(path = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllRoles() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }
}
