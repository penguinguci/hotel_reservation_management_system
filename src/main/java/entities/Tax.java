//package entities;
//
//
//import jakarta.persistence.*;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//
//import java.util.Set;
//
//@Data
//@Entity
//@EqualsAndHashCode(onlyExplicitlyIncluded = true)
//@Table(name = "taxes")
//public class Tax {
//    @Id
//    @EqualsAndHashCode.Include
//    @Column(name = "tax_id")
//    private String taxId;
//
//    @Column(name = "tax_name", columnDefinition = "nvarchar(30)", nullable = false)
//    private String taxName;
//
//    @Column(name = "tax_rate", nullable = false)
//    private double taxRate;
//
//    @Column(columnDefinition = "nvarchar(1024)")
//    private String description;
//
//    @Column(name = "is_applicable", columnDefinition = "bit", nullable = false)
//    private boolean isApplicable;
//
//    private String desription;
//
//    @OneToMany(mappedBy = "tax")
//    private Set<Orders> orders;
//}
