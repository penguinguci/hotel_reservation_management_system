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
    private Date checkInDate;

    @Column(name = "check_out_date")
    private Date checkOutDate;

    @Column(columnDefinition = "bit", nullable = false)
    private boolean status;

    @ToString.Exclude
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ReservationDetails> reservationDetails;

    @Column(name = "total_price")
    private double totalPrice;

    @Enumerated(EnumType.STRING)
    private BookingMethod bookingMethod;

    @Column(name = "deposit_amount")
    private double depositAmount;

    @Column(name = "remaining_amount")
    private double remainingAmount;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "number_of_nights")
    private int numberOfNights = 1;

    // Tính toán tổng tiền từ chi tiết đặt phòng
    public double calculateTotalPrice() {
        if (reservationDetails == null || reservationDetails.isEmpty()) {
            this.totalPrice = room.getPrice() * numberOfNights;
            return room.getPrice() * numberOfNights;
        }

        double total = room.getPrice() * numberOfNights + reservationDetails.stream()
                .mapToDouble(ReservationDetails::calculateLineTotal)
                .sum();

        this.totalPrice = total;
        return total;
    }

    // Tính tiền cọc dựa trên hình thức đặt
    public double calculateDepositAmount() {
        if (bookingMethod == BookingMethod.AT_THE_COUNTER) {
            this.depositAmount = 0;
        } else {
            this.depositAmount = totalPrice * 0.5; // 50% cho đặt liên hệ
        }
        return depositAmount;
    }

    // Tính tiền còn lại
    public double calculateRemainingAmount() {
        this.remainingAmount = totalPrice - depositAmount;
        return remainingAmount;
    }
}