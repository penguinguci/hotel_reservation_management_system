//package entities;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import lombok.ToString;
//
//import java.util.List;
//
//@Data
//@EqualsAndHashCode(callSuper = true)
//@Entity
//@Table(name = "deluxe_rooms")
//public class DeluxeRoom extends Room {
//    @Column(name = "has_balcony")
//    private boolean hasBalcony;       // Có ban công không
//
//    @Column(name = "luxury_decor")
//    private String luxuryDecor;       // Mô tả nội thất sang trọng
//
//    @Column(name = "soundproofing_level")
//    private String soundproofingLevel; // Mức cách âm (High, Medium, Low)
//
//    @ToString.Exclude
//    @ElementCollection
//    @CollectionTable(name = "upgraded_amenities", joinColumns = @JoinColumn(name = "room_id"))
//    @Column(name = "upgraded_amenities")
//    private List<String> upgradedAmenities; // Tiện nghi nâng cấp (Smart TV, High-Speed WiFi)
//
//    @Column(name = "includes_breakfast")
//    private boolean includesBreakfast; // Bao gồm bữa sáng
//}
