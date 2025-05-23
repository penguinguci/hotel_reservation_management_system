package entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "staffs")
public class Staff implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "staff_id")
    private String staffId;

    @Column(name = "first_name", columnDefinition = "nvarchar(50)", nullable = false)
    private String firstName;

    @Column(name = "last_name", columnDefinition = "nvarchar(50)", nullable = false)
    private String lastName;

    @Column(name = "gender")
    private boolean gender;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @ToString.Exclude
    @ElementCollection
    @CollectionTable(name = "phones", joinColumns = @JoinColumn(name = "staff_id"))
    @Column(name = "phones", nullable = false)
    private Set<String> phoneNumbers;

    @Column(name = "address", columnDefinition = "nvarchar(100)")
    private String address;

    @Column(name = "email", columnDefinition = "varchar(50)", unique = true)
    private String email;

    @Column(name = "staff_image", columnDefinition = "LONGTEXT")
    private String staffImage;

    @Column(name = "date_of_join")
    private Date dateOfJoin;

    @Column(columnDefinition = "bit", nullable = false)
    private boolean status;

    @OneToOne(mappedBy = "staff", cascade = CascadeType.ALL)
    private Account account;

    @ToString.Exclude
    @OneToMany(mappedBy = "staff")
    private List<Orders> orders;
}