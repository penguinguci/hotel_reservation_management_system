package entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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

    @Column(name = "booking_date")
    private Date bookingDate;

    @Column(columnDefinition = "bit", nullable = false)
    private boolean status;

    @ToString.Exclude
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationDetails> reservationDetails;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_type")
    private BookingType bookingType = BookingType.NIGHT; // Mặc định là đặt theo đêm

    @Column(name = "check_in_time")
    private Date checkInTime; // Thời gian check-in chi tiết đến giờ phút

    @Column(name = "check_out_time")
    private Date checkOutTime; // Thời gian check-out chi tiết đến giờ phút

    @Column(name = "duration_hours")
    private int durationHours = 1; // Số giờ đặt (chỉ dùng cho đặt theo giờ)

    @Column(name = "hourly_rate")
    private double hourlyRate; // Giá theo giờ

    // Enum cho loại đặt phòng
    public enum BookingType {
        NIGHT, // Đặt theo đêm
        HOUR   // Đặt theo giờ
    }


    // Phương thức mới để tính toán thời gian sử dụng theo giờ
    public void calculateDurationHours() {
        if (checkInTime != null && checkOutTime != null) {
            long diffInMillis = checkOutTime.getTime() - checkInTime.getTime();
            durationHours = (int) Math.ceil(diffInMillis / (60.0 * 60 * 1000));
        }
    }

    // Phương thức mới để kiểm tra xem đặt phòng có hợp lệ không
    public boolean isValidHourlyBooking() {
        if (bookingType != BookingType.HOUR) return false;

        // Kiểm tra thời gian tối thiểu và tối đa
        if (room != null) {
            if (durationHours < room.getMinHours() || durationHours > room.getMaxHours()) {
                return false;
            }
        }

        return checkInTime != null && checkOutTime != null &&
                checkInTime.before(checkOutTime) && durationHours > 0;
    }

    // Phương thức mới để tính phí phụ trội nếu khách check-out trễ
    public double calculateOverstayFee(Date actualCheckOutTime) {
        if (bookingType == BookingType.HOUR && actualCheckOutTime != null) {
            long actualDurationMs = actualCheckOutTime.getTime() - checkInTime.getTime();
            int actualHours = (int) Math.ceil(actualDurationMs / (60.0 * 60 * 1000));

            if (actualHours > durationHours) {
                int extraHours = actualHours - durationHours;
                // Tính phí phụ trội (ví dụ: 120% giá theo giờ)
                return extraHours * hourlyRate * 1.2;
            }
        }
        return 0.0;
    }

    // Tính toán tổng tiền từ chi tiết đặt phòng
    public double calculateTotalPrice() {
        double roomPrice = 0;

        if (bookingType == BookingType.NIGHT) {
            roomPrice = room.getPrice() * numberOfNights;
        } else if (bookingType == BookingType.HOUR) {
            roomPrice = hourlyRate * durationHours;
        }

        if (reservationDetails == null || reservationDetails.isEmpty()) {
            this.totalPrice = roomPrice;
            return roomPrice;
        }

        double total = roomPrice + reservationDetails.stream()
                .mapToDouble(ReservationDetails::calculateLineTotal)
                .sum();

        this.totalPrice = total;
        return total;
    }

    // Tính tiền cọc dựa trên hình thức đặt
    public double calculateDepositAmount() {
        if (bookingMethod == BookingMethod.AT_THE_COUNTER) {
            this.depositAmount = Math.min(1000000, totalPrice * 0.3); // 30% cho đặt tại quầy
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

    // tính tổng tiền dịch vụ
    public double calculateTotalServicePrice() {
        if (reservationDetails == null || reservationDetails.isEmpty()) {
            return 0;
        }
        return reservationDetails.stream()
                .mapToDouble(ReservationDetails::calculateLineTotal)
                .sum();
    }
}