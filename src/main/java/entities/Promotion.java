//package entities;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//
//import java.util.Date;
//
//@Data
//@Entity
//@EqualsAndHashCode(onlyExplicitlyIncluded = true)
//@Table(name = "promotions")
//public class Promotion {
//    @Id
//    @Column(name = "promotion_id")
//    private String promotionId;
//    private String name;
//    private String description;
//
//    @Column(name = "start_date")
//    private Date startDate;
//    @Column(name = "end_date")
//    private Date endDate;
//
//    @Column(name = "discount_percentage")
//    private double discountPercentage;
//
//    @OneToOne(mappedBy = "promotion")
//    private Room room;
//
//    @OneToOne(mappedBy = "promotion")
//    private Service service;
//}
