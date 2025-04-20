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

//    @ManyToOne
//    @JoinColumn(name = "tax_id")
//    private Tax tax;
//
//    @Column(name = "tax_price")
//    private double taxPrice;
//
//    @Column(name = "promotion_price")
//    private double promotionPrice;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "tax_amount")
    private double taxAmount;

    @Column(name = "service_fee")
    private double serviceFee;

//    public double calculateTaxPrice() {
//        double line_total_amount= 0;
//        for (OrderDetails orderDetail : orderDetails) {
//            line_total_amount += orderDetail.calculateLineTotalAmount();
//        }
//        return line_total_amount * tax.getTaxRate();
//    }

//    public double calculatePromotionPrice() {
//        double pro_price_room = 0;
//        double pro_price_service = 0;
//        for (OrderDetails orderDetail : orderDetails) {
//            if (orderDetail.getRoom() != null || orderDetail.getService() != null) {
//                pro_price_room = orderDetail.calculateLineTotalAmount() * orderDetail.getRoom().getPromotion().getDiscountPercentage();
//                pro_price_service = orderDetail.calculateLineTotalAmount() * orderDetail.getService().getPromotion().getDiscountPercentage();
//            }
//        }
//        return pro_price_room + pro_price_service;
//    }

    public double calculateTotalPrice() {
        if (orderDetails == null || orderDetails.isEmpty()) {
            return room.getPrice() * numberOfNights;
        }

        double total = room.getPrice() * numberOfNights + orderDetails.stream()
                .mapToDouble(OrderDetails::calculateLineTotal)
                .sum();

        this.totalPrice = total;
        return total;
    }

    public double calculateTaxAmount() {
        this.taxAmount = totalPrice * 0.1;
        return taxAmount;
    }

    // Tính phí dịch vụ (ví dụ: 5% tổng tiền)
    public double calculateServiceFee() {
        this.serviceFee = totalPrice * 0.05;
        return serviceFee;
    }

    // Tính tổng tiền thanh toán (bao gồm thuế và phí)
    public double calculateFinalTotalPrice() {
        return totalPrice + taxAmount + serviceFee;
    }
}
