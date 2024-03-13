package rs.edu.raf.userservice.domains.dto;

import rs.edu.raf.userservice.domains.model.Role;

public interface AuthenticationDetails {
    String getEmail();

    Role getRole();
}
