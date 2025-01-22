package entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "bonus_points")
public class BonusPoint {
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "bonus_point_id")
    private String bonusPointId;

    @OneToOne
    @JoinColumn(name = "customer_id", unique = true, nullable = false)
    private Customer customer;

    @Column(name = "total_point", columnDefinition = "float")
    private double totalPoint;

    @Column(name = "last_update")
    private Date lastUpdate;

    @Column(name = "expiration_date")
    private Date expirationDate; // ngày hết hạn điểm thưởng nếu có

    @Enumerated(EnumType.STRING)
    private Rank rank;
}
