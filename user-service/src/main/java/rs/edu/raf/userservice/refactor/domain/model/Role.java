package rs.edu.raf.userservice.refactor.domain.model;

import javax.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import rs.edu.raf.userservice.refactor.domain.model.enums.RoleName;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "roles", schema = "user_service_schema")
public class Role implements Serializable, GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    @Override
    public String getAuthority() {
        return roleName.toString();
    }
}
