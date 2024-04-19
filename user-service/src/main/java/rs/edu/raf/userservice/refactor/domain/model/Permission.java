package rs.edu.raf.userservice.refactor.domain.model;

import javax.persistence.*;
import lombok.*;
import rs.edu.raf.userservice.refactor.domain.model.enums.PermissionName;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "permissions", schema = "user_service_schema")
public class Permission implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permissionId;

    @Enumerated(EnumType.STRING)
    private PermissionName permissionName;
}
