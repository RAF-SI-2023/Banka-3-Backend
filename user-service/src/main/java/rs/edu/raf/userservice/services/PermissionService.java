package rs.edu.raf.userservice.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.userservice.repositories.PermissionRepository;

@Service
@AllArgsConstructor
public class PermissionService {

    private PermissionRepository permissionRepository;

}
