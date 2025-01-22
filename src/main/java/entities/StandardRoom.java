package entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "standard_rooms")
public class StandardRoom extends Room{
    @Column(name = "has_basic_tv")
    private boolean hasBasicTV;       // Có TV cơ bản

    @Column(name = "has_shared_bathroom")
    private boolean hasSharedBathroom; // Sử dụng nhà tắm chung

    @Column(name = "bed_type")
    private String bedType;           // Loại giường (Single, Twin, Double)

    @Column(name = "has_minibar")
    private boolean hasMiniBar;       // Có minibar không
}
