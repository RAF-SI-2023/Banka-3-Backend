package rs.edu.raf.userservice.domains.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import rs.edu.raf.userservice.domains.model.enums.RoleName;

import java.io.Serializable;
import java.util.ArrayList;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name="roles")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "roleId"),
            inverseJoinColumns = @JoinColumn(name = "userId")
    )
    private List<User> users = new ArrayList<>();
}
