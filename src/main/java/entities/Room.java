package entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "rooms")
@Inheritance(strategy = InheritanceType.JOINED)
public class Room {
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "room_id")
    protected String roomId;

    @Column(name = "price", columnDefinition = "float")
    protected double price;

    protected int capacity;

//    @Column(columnDefinition = "nvarchar(1024)")
//    protected String description;

    protected int status;

    @ToString.Exclude
    @ElementCollection
    @CollectionTable(name = "amentities", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "amentities", nullable = false)
    protected List<String> amenities;

    @Column(name = "room_size")
    protected double roomSize;
//
//    @OneToOne
//    @JoinColumn(name = "promotion_id", unique = true, nullable = true)
//    protected Promotion promotion;

    @Column(name = "room_image", columnDefinition = "varchar(1024)")
    protected String roomImage;

    @OneToMany(mappedBy = "room")
    protected Set<OrderDetails> orderDetails;

    @OneToMany(mappedBy = "room")
    protected Set<ReservationDetails> reservationDetailsDetails;
}
