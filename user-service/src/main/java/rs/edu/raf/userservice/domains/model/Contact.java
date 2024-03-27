package rs.edu.raf.userservice.domains.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@ToString
public class Contact implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @NotNull(message = "This field cannot be NULL")
    private String myName;

    @NotNull(message = "This field cannot be NULL")
    private String name;

    @NotNull(message = "This field cannot be NULL")
    private String accountNumber;

}
