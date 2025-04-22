package interfaces;

import entities.RoomType;

import java.util.List;

public interface RoomTypesDAO extends GenericDAO<RoomType, String> {
    List<RoomType> getAllRoomTypes();
}
