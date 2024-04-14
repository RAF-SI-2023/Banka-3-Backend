package rs.edu.raf.userservice.domains.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import rs.edu.raf.userservice.domains.model.enums.CurrencyName;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(schema = "user_service_schema")
public class Currency implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long currencyId;

    @Enumerated(EnumType.STRING)
    private CurrencyName name;

    @NotNull(message = "This field cannot be NULL")
    @Size(min = 3, max = 3, message = "Currency mark must be exactly 3 characters long")
    private String mark;
}
