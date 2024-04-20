package rs.edu.raf.userservice.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import rs.edu.raf.userservice.domain.model.enums.PermissionName;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "permissions", schema = "user_service_schema")
public class Permission implements Serializable, GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permissionId;

    @Enumerated(EnumType.STRING)
    private PermissionName permissionName;

    @Override
    @JsonIgnore
    public String getAuthority() {
        return permissionName.toString();
    }
}
