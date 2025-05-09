package entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@Setter
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "reservation_details")
public class ReservationDetails implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @EqualsAndHashCode.Include
    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;


    @Id
    @EqualsAndHashCode.Include
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    private int quantity;

    @Column(name = "line_total_amount")
    private double lineTotalAmount;


    @Column(name = "note")
    private String note;

    public double calculateLineTotal() {
        double total = 0;
        if (service != null) {
            total += quantity * service.getPrice();
        }
        this.lineTotalAmount = total;
        return total;
    }
}