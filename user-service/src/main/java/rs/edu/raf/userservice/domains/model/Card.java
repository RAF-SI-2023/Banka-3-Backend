package rs.edu.raf.userservice.domains.model;

import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@ToString
@Getter
@Setter
@Table(schema = "user_service_schema")
public class Card implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;

    @NotNull
    private Long userId;

    @NotNull
    @Size(min = 8, max = 8, message = "Currency mark must be exactly 3 characters long")
    private String cardNumber;

    @NotNull
    private String accountNumber; //broj racuna korisnika za koji se kartizza vezuje

    @NotNull
    private String cardName;

    @NotNull
    private Long creationDate;

    @NotNull
    private Long expireDate;

    @NotNull
    @Size(min = 3, max = 3, message = "Currency mark must be exactly 3 characters long")
    private String cvv;

    @NotNull
    private boolean status;

}
