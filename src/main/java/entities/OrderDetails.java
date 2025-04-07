package entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "order_details")
public class OrderDetails {
    @Id
    @EqualsAndHashCode.Include
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders orders;

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

    public double calculateLineTotalAmount() {
        double lineTotalAmount = 0;
        if (room != null && service == null) {
            lineTotalAmount = quantity * room.getPrice();
        } else if (room == null && service != null) {
            lineTotalAmount = quantity * service.getPrice();
        }
        return lineTotalAmount;
    }
}
