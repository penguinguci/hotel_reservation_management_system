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
    private Integer numberOfNights = 1;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_type")
    private BookingType bookingType = BookingType.NIGHT; // Mặc định là đặt theo đêm

    @Column(name = "check_in_time")
    private Date checkInTime; // Thời gian check-in chi tiết đến giờ phút

    @Column(name = "check_out_time")
    private Date checkOutTime; // Thời gian check-out chi tiết đến giờ phút

    @Column(name = "duration_hours")
    private Integer durationHours = 1; // Số giờ đặt (chỉ dùng cho đặt theo giờ)

    @Column(name = "hourly_rate")
    private double hourlyRate; // Giá theo giờ

    // Enum cho loại đặt phòng
    public enum BookingType {
        NIGHT, // Đặt theo đêm
        HOUR   // Đặt theo giờ
    }

    // Hằng số trạng thái đặt phòng
    public static final int STATUS_PENDING = 0; // Chờ xử lý
    public static final int STATUS_CHECKED_IN = 1; // Đã check-in
    public static final int STATUS_CHECKED_OUT = 2; // Đã check-out
    public static final int STATUS_CANCELLED = 3; // Đã hủy

    @Column(name = "reservation_status")
    private int reservationStatus = STATUS_PENDING;

    // Phương thức kiểm tra xem có thể check-in không
    public boolean canCheckIn() {
        return reservationStatus == STATUS_PENDING && room.getStatus() == Room.STATUS_RESERVED;
    }

    // Phương thức kiểm tra xem có thể check-out không
    public boolean canCheckOut() {
        return reservationStatus == STATUS_CHECKED_IN && room.getStatus() == Room.STATUS_OCCUPIED;
    }

    // Phương thức kiểm tra xem có thể hủy không
    public boolean canCancel() {
        return reservationStatus == STATUS_PENDING || reservationStatus == STATUS_CHECKED_IN;
    }

    // Tính phí phụ trội khi check-out muộn
    public double calculateOverstayFee(Date actualCheckOutTime) {
        if (actualCheckOutTime == null || checkOutTime == null) return 0.0;

        if (bookingType == BookingType.HOUR) {
            long actualDurationMs = actualCheckOutTime.getTime() - checkInTime.getTime();
            int actualHours = (int) Math.ceil(actualDurationMs / (60.0 * 60 * 1000));
            if (actualHours > durationHours) {
                int extraHours = actualHours - durationHours;
                return extraHours * hourlyRate * 1.2; // Phí phụ trội 120% giá giờ
            }
        } else if (bookingType == BookingType.NIGHT) {
            long actualCheckOutMs = actualCheckOutTime.getTime();
            long expectedCheckOutMs = checkOutDate.getTime();
            if (actualCheckOutMs > expectedCheckOutMs) {
                long extraMs = actualCheckOutMs - expectedCheckOutMs;
                int extraHours = (int) Math.ceil(extraMs / (60.0 * 60 * 1000));
                return extraHours * (room.getPrice() / 24 * 1.2); // Phí phụ trội dựa trên giá đêm
            }
        }
        return 0.0;
    }

    // Cập nhật trạng thái đặt phòng
    public void updateStatus(int newStatus) {
        this.reservationStatus = newStatus;
    }


    // Phương thức mới để tính toán thời gian sử dụng theo giờ
    public void calculateDurationHours() {
        if (checkInTime != null && checkOutTime != null) {
            long diffInMillis = checkOutTime.getTime() - checkInTime.getTime();
            durationHours = (int) Math.ceil(diffInMillis / (60.0 * 60 * 1000));
        } else {
            durationHours = 1;
        }
    }

    // Phương thức mới để kiểm tra xem đặt phòng có hợp lệ không
    public boolean isValidHourlyBooking() {
        if (bookingType != BookingType.HOUR) return false;

        // Kiểm tra thời gian tối thiểu và tối đa
        if (room != null) {
            if (durationHours == null || durationHours < room.getMinHours() || durationHours > room.getMaxHours()) {
                return false;
            }
        }

        return checkInTime != null && checkOutTime != null &&
                checkInTime.before(checkOutTime) && durationHours > 0;
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