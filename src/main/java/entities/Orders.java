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
@Table(name = "orders")
public class Orders {
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "order_id")
    private String orderId;

    @EqualsAndHashCode.Include
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @EqualsAndHashCode.Include
    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(columnDefinition = "bit", nullable = false)
    private int status;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @ToString.Exclude
    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderDetails> orderDetails;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "number_of_nights")
    private int numberOfNights = 1;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "tax_amount")
    private double taxAmount;

    @Column(name = "service_fee")
    private double serviceFee;

    @Column(name = "overstay_fee")
    private double overstayFee;


    public double calculateTaxAmount() {
        this.taxAmount = totalPrice * 0.1;
        return taxAmount;
    }

    public double calculateServiceFee() {
        this.serviceFee = orderDetails.stream().mapToDouble(OrderDetails::calculateLineTotal).sum() * 0.05;
        return serviceFee;
    }

    public double calculateFinalTotalPrice() {
        return totalPrice + taxAmount + serviceFee + overstayFee;
    }
}