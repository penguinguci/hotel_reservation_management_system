package entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.collection.spi.PersistentCollection;

import java.io.Serializable;
import java.util.*;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "rooms")
@Inheritance(strategy = InheritanceType.JOINED)
public class Room implements Serializable {
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "room_id")
    protected String roomId;

    @Column(name = "price", columnDefinition = "float")
    protected double price;

    @Column(name = "capacity")
    protected int capacity;

    protected int status;

    @Column(name = "floor")
    protected int floor;

    @ToString.Exclude
    @ElementCollection
    @CollectionTable(name = "amentities", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "amentities", nullable = false)
    @Fetch(FetchMode.JOIN)
    protected List<String> amenities;

    @Column(name = "room_size")
    protected double roomSize;

    @Column(name = "room_image", columnDefinition = "varchar(1024)")
    protected String roomImage;

    @ManyToOne
    @JoinColumn(name = "type_id")
    protected RoomType roomType;

    @OneToMany(mappedBy = "room")
    protected List<Orders> orders;

    @OneToMany(mappedBy = "room")
    protected List<Reservation> reservations;

    @Column(name = "hourly_base_rate", columnDefinition = "float")
    protected double hourlyBaseRate = 0.0; // Giá cơ bản theo giờ, mặc định là 0.0

    @Column(name = "min_hours")
    protected int minHours = 1; // Số giờ tối thiểu có thể đặt

    @Column(name = "max_hours")
    protected int maxHours = 12; // Số giờ tối đa có thể đặt trong một lần

    @ElementCollection
    @CollectionTable(name = "hourly_price_rules", joinColumns = @JoinColumn(name = "room_id"))
    @MapKeyColumn(name = "hour_range")
    @Column(name = "multiplier")
    protected Map<String, Double> hourlyPriceRules = new HashMap<>(); // Khởi tạo để tránh NPE

    // Status constants
    public static final int STATUS_AVAILABLE = 0;
    public static final int STATUS_OCCUPIED = 1;
    public static final int STATUS_MAINTENANCE = 2;
    public static final int STATUS_RESERVED = 3;

    public boolean isAvailable() {
        return status == STATUS_AVAILABLE;
    }

    @Column(name = "standard_checkout_hour")
    private int standardCheckoutHour = 12; // Giờ check-out tiêu chuẩn (mặc định 12h)

    public boolean isHourlyBooking() {
        return hourlyBaseRate > 0;
    }

    public void checkIn(Reservation reservation) {
        if (reservation.getBookingType() == Reservation.BookingType.NIGHT) {
            status = STATUS_OCCUPIED;
        } else {
            status = STATUS_OCCUPIED;
        }
    }

    public void checkOut(Reservation reservation) {
        status = STATUS_AVAILABLE;
    }

    public int getStandardCheckoutHour() {
        return standardCheckoutHour;
    }

    public void cancelReservation() {
        if (status == STATUS_RESERVED) {
            status = STATUS_AVAILABLE;
        }
    }

    // Phương thức tính giá mặc định theo giờ nếu chưa có giá cụ thể
    public double getDefaultHourlyRate() {
        // Nếu hourlyBaseRate chưa được thiết lập, tính dựa trên giá theo đêm
        if (hourlyBaseRate <= 0.0) {
            // Ví dụ: Giá theo giờ = 20% giá theo đêm
            return price * 0.2;
        }
        return hourlyBaseRate;
    }

    // Phương thức mới để tính giá theo giờ dựa vào khung giờ cụ thể
    public double calculateHourlyRate(Date checkInTime) {
        double baseRate = getSafeHourlyBaseRate();

        // Kiểm tra và khởi tạo nếu cần
        if (hourlyPriceRules == null) {
            hourlyPriceRules = new HashMap<>();
        } else if (hourlyPriceRules instanceof PersistentCollection &&
                !((PersistentCollection) hourlyPriceRules).wasInitialized()) {
            return baseRate; // Hoặc xử lý khác nếu cần
        }

        if (checkInTime == null || hourlyPriceRules.isEmpty()) {
            return baseRate;
        }


        Calendar cal = Calendar.getInstance();
        cal.setTime(checkInTime);
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        for (Map.Entry<String, Double> rule : hourlyPriceRules.entrySet()) {
            if (rule.getKey() == null || rule.getValue() == null) continue;

            String[] range = rule.getKey().split("-");
            if (range.length == 2) {
                try {
                    int startHour = Integer.parseInt(range[0]);
                    int endHour = Integer.parseInt(range[1]);

                    if (startHour <= hour && hour < endHour) {
                        return baseRate * rule.getValue();
                    }
                    // Xử lý trường hợp qua đêm (ví dụ: 22-6)
                    if (endHour < startHour && (hour >= startHour || hour < endHour)) {
                        return baseRate * rule.getValue();
                    }
                } catch (NumberFormatException e) {
                    continue;
                }
            }
        }

        return baseRate;
    }


    // Phương thức kiểm tra phòng có khả dụng trong khoảng thời gian cụ thể không (theo giờ)
    public boolean isAvailableForHourlyBooking(Date startTime, Date endTime) {
        if (startTime == null || endTime == null || startTime.after(endTime)) {
            return false;
        }

        // Kiểm tra trạng thái phòng
        if (status != STATUS_AVAILABLE) {
            return false;
        }

        // Kiểm tra trùng với các đặt phòng theo giờ
        if (reservations != null) {
            for (Reservation reservation : reservations) {
                if (reservation == null || !reservation.isStatus() ||
                        reservation.getBookingType() != Reservation.BookingType.HOUR) {
                    continue;
                }

                Date resStart = reservation.getCheckInTime();
                Date resEnd = reservation.getCheckOutTime();

                if (resStart == null || resEnd == null) continue;

                // Kiểm tra xem có trùng thời gian không
                if (!(endTime.before(resStart) || startTime.after(resEnd))) {
                    return false;
                }
            }
        }

        // Kiểm tra hợp lệ với các đặt phòng theo đêm
        return isHourlyBookingValidWithNightReservations(startTime, endTime);
    }

    // Phương thức để lấy giá theo giờ một cách an toàn
    public double getSafeHourlyBaseRate() {
        return hourlyBaseRate > 0 ? hourlyBaseRate : getDefaultHourlyRate();
    }

    // Lấy thời gian check-in sớm nhất của các đặt phòng theo đêm trong tương lai
    public Date getEarliestNightReservationCheckIn(Date currentTime) {
        if (reservations == null || reservations.isEmpty()) {
            return null;
        }

        Date earliestCheckIn = null;
        for (Reservation reservation : reservations) {
            if (reservation.isStatus() && reservation.getBookingType() == Reservation.BookingType.NIGHT) {
                Date checkIn = reservation.getCheckInDate();
                if (checkIn != null && checkIn.after(currentTime)) {
                    if (earliestCheckIn == null || checkIn.before(earliestCheckIn)) {
                        earliestCheckIn = checkIn;
                    }
                }
            }
        }
        return earliestCheckIn;
    }

    // Kiểm tra thời gian đặt phòng theo giờ có hợp lệ so với đặt phòng theo đêm
    public boolean isHourlyBookingValidWithNightReservations(Date startTime, Date endTime) {
        if (startTime == null || endTime == null || startTime.after(endTime)) {
            return false;
        }

        Date earliestCheckIn = getEarliestNightReservationCheckIn(new Date());
        if (earliestCheckIn == null) {
            return true; // Không có đặt phòng theo đêm trong tương lai
        }

        // Thời gian check-out của đặt phòng theo giờ phải trước thời gian check-in của đặt phòng theo đêm
        return endTime.before(earliestCheckIn);
    }
}