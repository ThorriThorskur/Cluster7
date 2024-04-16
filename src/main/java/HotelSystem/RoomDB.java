package HotelSystem;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RoomDB {
    public List<Room> searchRoomsByHotelAndDates(String hotelName, LocalDate checkInDate, LocalDate checkOutDate) {
        List<Room> availableRooms = new ArrayList<>();
        String sql = "SELECT r.* FROM Rooms r " +
                "JOIN Hotels h ON r.hotelId = h.hotelId " +
                "WHERE h.name = ? AND r.available = 1 " +
                "AND r.roomId NOT IN (SELECT roomId FROM Reservations " +
                "WHERE checkOutDate >= ? AND checkInDate <= ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, hotelName);
            statement.setDate(2, java.sql.Date.valueOf(checkInDate));
            statement.setDate(3, java.sql.Date.valueOf(checkOutDate));
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Room room = new Room();
                room.setRoomNumber(resultSet.getInt("roomNumber"));
                room.setType(Room.RoomType.valueOf(resultSet.getString("type")));
                room.setPricePerNight(resultSet.getDouble("pricePerNight"));
                room.setIsAvailable(resultSet.getBoolean("available"));
                room.setRoomId(resultSet.getInt("roomId"));
                availableRooms.add(room);
            }
        } catch (SQLException e) {
            System.err.println("SQLException occurred while retrieving rooms: " + e.getMessage());
        }
        return availableRooms;
    }

}