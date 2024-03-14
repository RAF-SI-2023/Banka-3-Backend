package rs.edu.raf.userservice.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.userservice.domains.model.Role;
import rs.edu.raf.userservice.repositories.RoleRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleService {

    private RoleRepository roleRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
