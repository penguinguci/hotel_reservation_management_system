package entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "room_type")
public class RoomType {
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "type_id")
    private String typeID;
    @Column(name = "type_name",columnDefinition = "nvarchar(100)")
    private String typeName;
    @Column(name ="description",columnDefinition = "nvarchar(1024)")
    private String description;
}
