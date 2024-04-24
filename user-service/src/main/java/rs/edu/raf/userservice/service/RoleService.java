package rs.edu.raf.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.userservice.domain.model.Role;
import rs.edu.raf.userservice.domain.model.enums.RoleName;
import rs.edu.raf.userservice.repository.RoleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleByName(String roleName) {
        return roleRepository.findByRoleName(RoleName.valueOf(roleName)).orElseThrow(() -> new RuntimeException("Role not found"));
    }
}
