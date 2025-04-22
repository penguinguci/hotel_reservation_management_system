package entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "room_type")
public class RoomType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "type_id")
    private String typeID;
    @Column(name = "type_name",columnDefinition = "nvarchar(100)")
    private String typeName;
    @Column(name ="description",columnDefinition = "nvarchar(1024)")
    private String description;

    @OneToMany(mappedBy = "roomType")
    private List<Room> room;
}
