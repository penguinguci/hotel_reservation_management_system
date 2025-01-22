package entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "services")
public class Service {
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "service_id")
    private int serviceId;

    @Column(columnDefinition = "varchar(50)")
    private String name;

    @Column(columnDefinition = "nvarchar(1024)")
    private String description;

    @Column(columnDefinition = "float", nullable = false)
    private double price;

    @Column(columnDefinition = "bit", nullable = false)
    private boolean availability;

    @OneToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @OneToMany(mappedBy = "service")
    private Set<OrderDetails> orderDetails;

    @OneToMany(mappedBy = "service")
    private Set<ReservationDetails> reservationDetailsDetails;
}
