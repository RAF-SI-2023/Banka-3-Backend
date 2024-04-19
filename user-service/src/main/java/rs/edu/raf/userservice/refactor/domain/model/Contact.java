package rs.edu.raf.userservice.refactor.domain.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "contacts", schema = "user_service_schema")
public class Contact implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contactId;

    @NotNull(message = "myName field cannot be NULL")
    @Size(min = 1, max = 25)
    private String myName;

    @NotNull(message = "name field cannot be NULL")
    @Size(min = 1, max = 25)
    private String name;

    @NotNull(message = "accountNumber field cannot be NULL")
    private String accountNumber;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
