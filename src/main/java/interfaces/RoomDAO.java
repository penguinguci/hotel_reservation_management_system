package interfaces;

import entities.Room;
import entities.RoomType;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface RoomDAO extends GenericDAO<Room, String> {
    public List<String> addAmenity(String roomId, String amenity);
    public List<String> updateAmenity(String roomId, int amenityIndex, String newAmenity);
    List<Room> findByCriteria(Map<String, Object> criteria);
    List<Room> findAvailableRooms(Date checkInDate, Date checkOutDate, Integer capacity, String roomType, Double minPrice, Double maxPrice);
    boolean isRoomAvailable(String roomId, Date checkIn, Date checkOut);
    double calculateRoomPrice(String roomId, Date checkInDate, Date checkOutDate);
    List<Room> getAllRoomTypes();
    List<Room> getRoomsByStatus(int status);
    public List<Room> getAllRooms();
    public List<Integer> getAllFloors();
}
