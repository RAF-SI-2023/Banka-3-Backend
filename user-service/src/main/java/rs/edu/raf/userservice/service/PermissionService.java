package rs.edu.raf.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.userservice.domain.model.Permission;
import rs.edu.raf.userservice.domain.model.enums.PermissionName;
import rs.edu.raf.userservice.repository.PermissionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    public Permission getPermissionByName(String permissionName) {
        return permissionRepository.findByPermissionName(PermissionName.valueOf(permissionName)).orElseThrow(() -> new RuntimeException("Permission not found"));
    }
}
