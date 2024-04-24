package rs.edu.raf.userservice.domain;

import rs.edu.raf.userservice.domain.model.Permission;
import rs.edu.raf.userservice.domain.model.Role;

import java.util.List;

public interface AuthenticationDetails {

    String getEmail();

    Role getRole();

    Long getId();

    List<Permission> getPermissions();
}
