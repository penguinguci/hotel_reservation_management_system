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
//@Entity
//@EqualsAndHashCode(callSuper = true)
//@Table(name = "suite_rooms")
//public class SuiteRoom extends Room {
//    @Column(name = "has_private_kitchen")
//    private boolean hasPrivateKitchen;
//
//    @Column(name = "has_living_area")
//    private boolean hasLivingArea;
//
//    @Column(name = "has_jacuzzi")
//    private boolean hasJacuzzi;
//
//    @Column(name = "number_of_bedrooms")
//    private int numberOfBedrooms;
//
//    @Column(name = "room_view")
//    private String roomView;
//
//    @ToString.Exclude
//    @ElementCollection
//    @CollectionTable(name = "exclusive_services", joinColumns = @JoinColumn(name = "room_id"))
//    @Column(name = "exclusive_services")
//    private List<String> exclusiveServices;
//}
