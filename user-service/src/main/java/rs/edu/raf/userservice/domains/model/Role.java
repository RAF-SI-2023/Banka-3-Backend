package rs.edu.raf.userservice.domains.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import rs.edu.raf.userservice.domains.model.enums.RoleName;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@ToString
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    @JsonIgnore
    @OneToMany(mappedBy = "role")
    private List<Employee> employees = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "role")
    private List<User> users = new ArrayList<>();

}
