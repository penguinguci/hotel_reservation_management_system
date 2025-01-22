package entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "reservation_details")
public class ReservationDetails {
    @Id
    @EqualsAndHashCode.Include
    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Id
    @EqualsAndHashCode.Include
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Id
    @EqualsAndHashCode.Include
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    private int quantity;

    @Column(name = "line_total_amount")
    private double lineTotalAmount;

    public double getLineTotalAmount() {
        double lineTotalAmount = 0;
        if (room != null && service == null) {
            lineTotalAmount = quantity * room.pricePerNight;
        } else if (room == null && service != null) {
            lineTotalAmount = quantity * service.getPrice();
        }
        return lineTotalAmount;
    }
}
