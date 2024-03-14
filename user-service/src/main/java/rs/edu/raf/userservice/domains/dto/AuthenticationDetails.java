package rs.edu.raf.userservice.domains.dto;

import rs.edu.raf.userservice.domains.model.Permission;
import rs.edu.raf.userservice.domains.model.Role;

import java.util.List;

public interface AuthenticationDetails {
    String getEmail();
    Role getRole();
    Long getId();
    List<Permission> getPermissions();
}
