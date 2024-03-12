package rs.edu.raf.userservice.domains.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    @JsonIgnore
    @OneToMany(mappedBy = "roles")
    private List<Employee> employees = new ArrayList<>();
}
