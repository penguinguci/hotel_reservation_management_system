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
    @OneToMany(mappedBy = "orders")
    private Set<OrderDetails> orderDetails;

    @ManyToOne
    @JoinColumn(name = "tax_id")
    private Tax tax;

    @Column(name = "tax_price")
    private double taxPrice;

    @Column(name = "promotion_price")
    private double promotionPrice;

    @Column(name = "total_price")
    private double totalPrice;

    public double calculateTaxPrice() {
        double line_total_amount= 0;
        for (OrderDetails orderDetail : orderDetails) {
            line_total_amount += orderDetail.calculateLineTotalAmount();
        }
        return line_total_amount * tax.getTaxRate();
    }

    public double calculatePromotionPrice() {
        double pro_price_room = 0;
        double pro_price_service = 0;
        for (OrderDetails orderDetail : orderDetails) {
            if (orderDetail.getRoom() != null || orderDetail.getService() != null) {
                pro_price_room = orderDetail.calculateLineTotalAmount() * orderDetail.getRoom().getPromotion().getDiscountPercentage();
                pro_price_service = orderDetail.calculateLineTotalAmount() * orderDetail.getService().getPromotion().getDiscountPercentage();
            }
        }
        return pro_price_room + pro_price_service;
    }

    public double calculateTotalPrice() {
        double line_total_amount = 0;
        for (OrderDetails orderDetail : orderDetails) {
            line_total_amount += orderDetail.getLineTotalAmount();
        }
        return line_total_amount + calculateTaxPrice() - calculatePromotionPrice();
    }
}
