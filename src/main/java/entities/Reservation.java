package entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    private Integer numberOfNights = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_type")
    private BookingType bookingType = BookingType.NIGHT;

    @Column(name = "check_in_time")
    private Date checkInTime;

    @Column(name = "check_out_time")
    private Date checkOutTime;

    @Column(name = "duration_hours")
    private Integer durationHours = 0;

    @Column(name = "hourly_rate")
    private double hourlyRate;

    public enum BookingType {
        NIGHT, HOUR
    }

    public static final int STATUS_PENDING = 0;
    public static final int STATUS_CHECKED_IN = 1;
    public static final int STATUS_CHECKED_OUT = 2;
    public static final int STATUS_CANCELLED = 3;

    @Column(name = "reservation_status")
    private int reservationStatus = STATUS_PENDING;

    @Column(name = "actual_checkout_time")
    private Date actualCheckOutTime;

    @Column(name = "overstay_fee")
    private double overstayFee;

    @Column(name = "overstay_units")
    private int overstayUnits;

    public boolean isCheckedOut() {
        return reservationStatus == STATUS_CHECKED_OUT;
    }

    public boolean canCheckIn() {
        return reservationStatus == STATUS_PENDING
                && room != null
                && room.getStatus() == Room.STATUS_RESERVED;
    }

    public boolean canCheckOut() {
        return reservationStatus == STATUS_CHECKED_IN
                && room != null
                && room.getStatus() == Room.STATUS_OCCUPIED;
    }

    public boolean canCancel() {
        return reservationStatus == STATUS_PENDING || reservationStatus == STATUS_CHECKED_IN;
    }

    // Phương thức tính phụ phí check-out muộn
    public void calculateOverstayDetails(Date actualCheckOut) {
        this.actualCheckOutTime = actualCheckOut;

        if (bookingType == BookingType.HOUR) {
            calculateHourlyOverstay(actualCheckOut);
        } else {
            calculateNightlyOverstay(actualCheckOut);
        }
    }

    private void calculateHourlyOverstay(Date actualCheckOut) {
        if (checkOutTime == null || actualCheckOut == null) {
            overstayUnits = 0;
            overstayFee = 0;
            return;
        }

        long diffMs = actualCheckOut.getTime() - checkOutTime.getTime();
        if (diffMs <= 0) {
            overstayUnits = 0;
            overstayFee = 0;
            return;
        }

        // Làm tròn lên theo giờ
        overstayUnits = (int) Math.ceil(diffMs / (60.0 * 60 * 1000));
        overstayFee = overstayUnits * hourlyRate * 1.2; // Phụ phí 20%
    }

    private void calculateNightlyOverstay(Date actualCheckOut) {
        if (checkOutDate == null || actualCheckOut == null) {
            overstayUnits = 0;
            overstayFee = 0;
            return;
        }

        Calendar expected = Calendar.getInstance();
        expected.setTime(checkOutDate);
        expected.set(Calendar.HOUR_OF_DAY, room.getStandardCheckoutHour());
        expected.set(Calendar.MINUTE, 0);
        expected.set(Calendar.SECOND, 0);

        long diffMs = actualCheckOut.getTime() - expected.getTimeInMillis();
        if (diffMs <= 0) {
            overstayUnits = 0;
            overstayFee = 0;
            return;
        }

        // Tính số giờ phụ trội
        int extraHours = (int) Math.ceil(diffMs / (60.0 * 60 * 1000));

        // Nếu quá 3 giờ tính thêm 1 ngày
        if (extraHours > 3) {
            overstayUnits = 1; // 1 ngày
            overstayFee = room.getPrice();
        } else {
            overstayUnits = 0; // Miễn phí 3 giờ đầu
            overstayFee = 0;
        }
    }


    public void updateStatus(int newStatus) {
        this.reservationStatus = newStatus;
    }

    public void calculateDurationHours() {
        if (checkInTime != null && checkOutTime != null) {
            long diffInMillis = checkOutTime.getTime() - checkInTime.getTime();
            durationHours = (int) Math.ceil(diffInMillis / (60.0 * 60 * 1000));
            durationHours = Math.max(durationHours, 1); // Đảm bảo tối thiểu 1 giờ
        } else {
            durationHours = 1;
        }
    }

    public boolean isValidHourlyBooking() {
        if (bookingType != BookingType.HOUR) return true; // Chỉ kiểm tra cho đặt theo giờ

        if (room == null || durationHours == null || checkInTime == null || checkOutTime == null) {
            return false;
        }

        if (durationHours < room.getMinHours() || durationHours > room.getMaxHours()) {
            return false;
        }

        return checkInTime.before(checkOutTime) && durationHours > 0;
    }

    public double calculateTotalPrice() {
        double roomPrice = 0;

        if (bookingType == BookingType.NIGHT) {
            roomPrice = room.getPrice() * numberOfNights;
        } else if (bookingType == BookingType.HOUR) {
            roomPrice = hourlyRate * durationHours;
        }

        double servicePrice = (reservationDetails == null || reservationDetails.isEmpty()) ? 0 :
                reservationDetails.stream().mapToDouble(ReservationDetails::calculateLineTotal).sum();

        this.totalPrice = roomPrice + servicePrice;
        return this.totalPrice;
    }

    public double calculateDepositAmount() {
        if (bookingMethod == BookingMethod.AT_THE_COUNTER) {
            this.depositAmount = Math.min(1000000, totalPrice * 0.3);
        } else {
            this.depositAmount = totalPrice * 0.5;
        }
        return depositAmount;
    }

    public double calculateRemainingAmount() {
        this.remainingAmount = totalPrice - depositAmount;
        return remainingAmount;
    }

    public double calculateTotalServicePrice() {
        if (reservationDetails == null || reservationDetails.isEmpty()) {
            return 0;
        }
        return reservationDetails.stream().mapToDouble(ReservationDetails::calculateLineTotal).sum();
    }

}