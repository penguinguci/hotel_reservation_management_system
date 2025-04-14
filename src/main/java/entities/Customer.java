package entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "customers")
public class Customer {
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "first_name", columnDefinition = "nvarchar(50)", nullable = false)
    private String firstName;
    @Column(name = "last_name", columnDefinition = "nvarchar(50)", nullable = false)
    private String lastName;

    @Column(name = "gender")
    private boolean gender;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(columnDefinition = "varchar(50)", nullable = true, unique = true)
    private String email;

    @Column(name = "cccd", columnDefinition = "varchar(20)", nullable = true, unique = true)
    private String CCCD;

    @Column(name = "address")
    private String address;

    @Column(name = "point")
    private double bonusPoint;

    @ToString.Exclude
    @OneToMany(mappedBy = "customer")
    private Set<Orders> orders;

    @ToString.Exclude
    @OneToMany(mappedBy = "customer")
    private Set<Reservation> reservations;
}
