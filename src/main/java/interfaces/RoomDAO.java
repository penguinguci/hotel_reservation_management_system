package interfaces;

import entities.Room;

import java.util.Date;
import java.util.List;

public interface RoomDAO extends GenericDAO<Room, String> {
    public List<Room> searchAvailableRooms(Date checkInDate, Date checkOutDate, int capacity, String roomType, double minPrice, double maxPrice);
    public boolean isRoomAvailable(String roomId, Date checkIn, Date checkOut);
    public double calculateRoomPrice(String roomId, Date checkInDate, Date checkOutDate);
    public List<Room> getAllRoomTypes();
    public List<Room> getRoomsByStatus(int status);
}
