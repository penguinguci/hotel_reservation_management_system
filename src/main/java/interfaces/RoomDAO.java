package interfaces;

import entities.Room;

import java.util.Date;
import java.util.List;

public interface RoomDAO extends GenericDAO<Room, String> {
    public List<Room> findAvailableRooms(Date checkInDate, Date checkOutDate, Integer capacity, String roomType, Double minPrice, Double maxPrice);
    public boolean isRoomAvailable(String roomId, Date checkIn, Date checkOut);
    public double calculateRoomPrice(String roomId, Date checkInDate, Date checkOutDate);
    public List<Room> getAllRoomTypes();
    public List<Room> getRoomsByStatus(int status);
}
