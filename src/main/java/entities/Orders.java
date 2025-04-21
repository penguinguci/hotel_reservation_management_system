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
    private Integer numberOfNights;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "tax_amount")
    private double taxAmount;

    @Column(name = "service_fee")
    private double serviceFee;

    @Column(name = "overstay_fee", nullable = false, columnDefinition = "DOUBLE DEFAULT 0.0")
    private double overstayFee;

    @Column(name = "deposit_amount")
    private double depositAmount;

    @Column(name = "remaining_amount")
    private double remainingAmount;

    @Column(name = "check_in_date")
    private Date checkInDate;

    @Column(name = "check_out_date")
    private Date checkOutDate;

    @Column(name = "check_in_time")
    private Date checkInTime;

    @Column(name = "check_out_time")
    private Date checkOutTime;

    public double calculateTaxAmount() {
        // Thuế 10% dựa trên tổng tiền phòng và phụ phí trễ
        this.taxAmount = (totalPrice + overstayFee) * 0.1;
        return taxAmount;
    }

    public double calculateServiceFee() {
        // Phí dịch vụ 5% dựa trên tổng tiền dịch vụ từ orderDetails
        this.serviceFee = orderDetails.stream()
                .mapToDouble(OrderDetails::calculateLineTotal)
                .sum() * 0.05;
        return serviceFee;
    }

    public double calculateTotalPrice() {
        // Base room price
        this.totalPrice = calculateRoomPrice();
        // Service total
        double serviceTotal = orderDetails.stream()
                .mapToDouble(OrderDetails::calculateLineTotal)
                .sum();
        // Calculate tax and service fee
        calculateTaxAmount();
        calculateServiceFee();
        // Final total
        return totalPrice + overstayFee + taxAmount + serviceFee + serviceTotal;
    }

    public double calculateRemainingPayment() {
        // Số tiền khách phải trả còn lại = tổng tiền cuối cùng - tiền cọc
        return calculateTotalPrice() - depositAmount;
    }

    public double calculateRoomPrice() {
        if (numberOfNights != null && numberOfNights > 0) {
            return room.getPrice() * numberOfNights;
        } else if (checkInTime != null && checkOutTime != null) {
            long diffInMillis = checkOutTime.getTime() - checkInTime.getTime();
            int hours = (int) Math.ceil(diffInMillis / (60.0 * 60 * 1000));
            return room.calculateHourlyRate(checkInTime) * hours;
        }
        return room.getPrice();
    }

    public void recalculateTotalPrice(Reservation reservation) {
        this.totalPrice = calculateRoomPrice();
        if (reservation != null) {
            this.overstayFee = reservation.getOverstayFee();
            this.depositAmount = reservation.getDepositAmount();
            this.remainingAmount = reservation.getRemainingAmount();
        }
        calculateTaxAmount();
        calculateServiceFee();
    }
}