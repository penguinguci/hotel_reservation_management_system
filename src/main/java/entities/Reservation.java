package entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "reservations")
public class Reservation {
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "reservation_id")
    private String reservationId;

    @EqualsAndHashCode.Include
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @EqualsAndHashCode.Include
    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;


    @Column(name = "check_in_date")
    private Date CheckInDate;

    @Column(name = "check_out_date")
    private Date CheckOutDate;

    @Column(columnDefinition = "bit", nullable = false)
    private boolean status;

    @ToString.Exclude
    @OneToMany(mappedBy = "reservation")
    private Set<ReservationDetails> reservationDetails;

    @Column(name = "total_price")
    private double totalPrice;

    @Transient
    private double subTotal;

    @Transient
    private double taxAmount;

    @Transient
    private double finalTotal;

//    public void calculatePrices() {
//        this.subTotal = getTotalPrice();
//        this.taxAmount = subTotal * taxRate;
//        this.finalTotal = subTotal + taxAmount - discountAmount;
//    }

    private double getTotalPrice() {
        double total = 0;
        for (ReservationDetails reservationDetail : reservationDetails) {
            total += reservationDetail.getLineTotalAmount();
        }
        this.totalPrice = total;
        return total;
    }

    // Getters for transient fields
    public double getSubTotal() {
        return subTotal;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public double getFinalTotal() {
        return finalTotal;
    }
}
