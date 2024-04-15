package HotelSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomDB {
    public Room selectById(int roomNumber) {
        String sql = "SELECT * FROM Rooms WHERE roomNumber = ?";
        Room room = null;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, roomNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    room = new Room();
                    room.setRoomNumber(resultSet.getInt("roomNumber"));
                    room.setType(Room.RoomType.valueOf(resultSet.getString("type")));
                    room.setPricePerNight(resultSet.getDouble("pricePerNight"));
                    room.setIsAvailable(resultSet.getBoolean("available"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving room: " + e.getMessage());
        }
        return room;
    }

    public List<Room> searchRoomsByHotelName(String hotelName) {
        List<Room> rooms = new ArrayList<>();
        // SQL query to select all rooms from the Rooms table where the hotelId matches a hotel name
        String sql = "SELECT r.* FROM Rooms r " +
                "JOIN Hotels h ON r.hotelId = h.hotelId " +
                "WHERE h.name = ? AND r.available = 1"; // Only get available rooms

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, hotelName);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Room room = new Room();
                room.setRoomNumber(resultSet.getInt("roomNumber"));
                room.setType(Room.RoomType.valueOf(resultSet.getString("type")));
                room.setPricePerNight(resultSet.getDouble("pricePerNight"));
                room.setIsAvailable(resultSet.getBoolean("available"));
                rooms.add(room);
            }
        } catch (SQLException e) {
            System.err.println("SQLException occurred while retrieving rooms: " + e.getMessage());
        }
        return rooms;
    }

    public List<Room> searchRoomsByHotelAndType(int hotelId, String checkInDate, String checkOutDate, Room.RoomType type) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Rooms WHERE hotelId = ? AND type = ? AND roomId NOT IN (" +
                "SELECT roomId FROM Reservations WHERE checkInDate <= ? AND checkOutDate >= ?)";

        try (Connection connection = DatabaseConnection.getConnection(); // Obtain connection from centralized class
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, hotelId);
            statement.setString(2, type.name());
            statement.setString(3, checkOutDate);
            statement.setString(4, checkInDate);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Room room = new Room();
                room.setRoomNumber(resultSet.getInt("roomNumber"));
                room.setType(Room.RoomType.valueOf(resultSet.getString("type")));
                room.setPricePerNight(resultSet.getDouble("pricePerNight"));
                room.setIsAvailable(resultSet.getBoolean("available"));
                rooms.add(room);
            }
        } catch (SQLException e) {
            System.err.println("SQLException occurred while retrieving rooms: " + e.getMessage());
        }
        return rooms;
    }

    public void update(Room room) {
        String sql = "UPDATE Rooms SET type = ?, pricePerNight = ?, available = ?, capacity = ?, clean = ? WHERE roomNumber = ?";

        try (Connection connection = DatabaseConnection.getConnection(); // Get connection from centralized class
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, room.getType().toString());
            statement.setDouble(2, room.getPricePerNight());
            statement.setBoolean(3, room.isAvailable());
            statement.setInt(6, room.getRoomNumber());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating room: " + e.getMessage());
        }
    }

    public void insert(Room room) {
        String sql = "INSERT INTO Rooms (roomNumber, type, pricePerNight, available, capacity, clean) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, room.getRoomNumber());
            statement.setString(2, room.getType().toString());
            statement.setDouble(3, room.getPricePerNight());
            statement.setBoolean(4, room.isAvailable());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting room: " + e.getMessage());
        }
    }
    public void delete(int roomNumber) {
        String sql = "DELETE FROM Rooms WHERE roomNumber = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, roomNumber);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting room: " + e.getMessage());
        }
    }
}