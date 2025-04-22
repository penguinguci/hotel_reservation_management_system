package entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "order_details")
public class OrderDetails implements Serializable {
    @Id
    @EqualsAndHashCode.Include
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders orders;

    @Id
    @EqualsAndHashCode.Include
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    private int quantity;

    @Column(name = "line_total_amount")
    private double lineTotalAmount;

    public double calculateLineTotal() {
        double total = 0;
        if (service != null) {
            total += quantity * service.getPrice();
        }
        this.lineTotalAmount = total;
        return total;
    }
}
