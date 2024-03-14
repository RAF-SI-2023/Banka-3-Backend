package rs.edu.raf.userservice.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.userservice.domains.model.Permission;
import rs.edu.raf.userservice.repositories.PermissionRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class PermissionService {

    private PermissionRepository permissionRepository;

    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

}
