    package entities;

    import jakarta.persistence.*;
    import lombok.Data;
    import lombok.EqualsAndHashCode;

    import java.io.Serializable;

    @Data
    @Entity
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @Table(name = "accounts")
    public class Account implements Serializable {
        private static final long serialVersionUID = 1L;
        @Id
        @EqualsAndHashCode.Include
        @Column(columnDefinition = "varchar(50)", nullable = false, unique = true)
        private String username;

        @Column(columnDefinition = "varchar(250)", nullable = false)
        private String password;

        //@OneToOne
       // @JoinColumn(name = "role", unique = true, nullable = false)
        @OneToOne
        @JoinColumn(name = "staff_id", unique = true)
        private Staff staff;

        @Enumerated(EnumType.STRING)
        private Role role;
    }
