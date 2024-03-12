package rs.edu.raf.userservice.domains.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Getter
@Setter
public class AccountActivation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private int code;

    @Column(nullable = false)
    private Long date;

    @Column
    @ColumnDefault("true")
    private boolean active = true;

    public AccountActivation() {
        this.date = new Date().getTime();
        this.code = ThreadLocalRandom.current().nextInt(100000, 1000000);
    }
}