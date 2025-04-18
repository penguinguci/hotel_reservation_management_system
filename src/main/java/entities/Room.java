package entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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

    protected int status;

    @Column(name = "floor")
    protected int floor;

    @ToString.Exclude
    @ElementCollection
    @CollectionTable(name = "amentities", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "amentities", nullable = false)
    @Fetch(FetchMode.JOIN)
    protected List<String> amenities;

    @Column(name = "room_size")
    protected double roomSize;

    @Column(name = "room_image", columnDefinition = "varchar(1024)")
    protected String roomImage;

    @ManyToOne
    @JoinColumn(name = "type_id")
    protected RoomType roomType;

    @OneToMany(mappedBy = "room")
    protected List<Orders> orders;

    @OneToMany(mappedBy = "room")
    protected List<Reservation> reservations;

    // Status constants
    public static final int STATUS_AVAILABLE = 0;
    public static final int STATUS_OCCUPIED = 1;
    public static final int STATUS_MAINTENANCE = 2;
    public static final int STATUS_RESERVED = 3;

    public boolean isAvailable() {
        return status == STATUS_AVAILABLE;
    }
}