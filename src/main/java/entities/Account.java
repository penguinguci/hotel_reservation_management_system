package entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "accounts")
public class Account {
    @Id
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "varchar(50)", nullable = false, unique = true)
    private String username;

    @Column(columnDefinition = "varchar(50)", nullable = false)
    private String password;

    @OneToOne
    @JoinColumn(name = "staff_id", unique = true, nullable = false)
    private Staff staff;
}
