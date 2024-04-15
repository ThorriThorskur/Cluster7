package HotelSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReservationDB {
    private Connection connection;
    public Reservation selectById(int reservationId) {
        String sql = "SELECT * FROM Reservations WHERE reservationId = ?";
        Reservation reservation = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, reservationId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int guestId = resultSet.getInt("guestId");
                    int roomId = resultSet.getInt("roomId");
                    Date checkInDate = new Date(resultSet.getDate("checkInDate").getTime()); // Convert java.sql.Date to java.util.Date
                    Date checkOutDate = new Date(resultSet.getDate("checkOutDate").getTime()); // Convert java.sql.Date to java.util.Date
                    double totalCost = resultSet.getDouble("totalCost");

                    // Now initialize the Reservation object using the constructor
                    reservation = new Reservation(reservationId, guestId, roomId, checkInDate, checkOutDate);
                    reservation.setTotalCost(totalCost); // Set the total cost if it needs to be stored separately
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving reservation: " + e.getMessage());
            // Handle the exception as appropriate (e.g., rethrow, log, etc.)
        }
        return reservation; // This will return null if the reservation was not found or if an error occurred
    }


    public List<Reservation> selectAllByGuest(int guestId) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM Reservations WHERE guestId = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, guestId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int reservationId = resultSet.getInt("reservationId");
                    int roomId = resultSet.getInt("roomId");
                    Date checkInDate = new Date(resultSet.getDate("checkInDate").getTime()); // Convert java.sql.Date to java.util.Date
                    Date checkOutDate = new Date(resultSet.getDate("checkOutDate").getTime()); // Convert java.sql.Date to java.util.Date
                    double totalCost = resultSet.getDouble("totalCost");

                    Reservation reservation = new Reservation(reservationId, guestId, roomId, checkInDate, checkOutDate);
                    reservation.setTotalCost(totalCost);
                    reservations.add(reservation);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving reservations: " + e.getMessage());
        }
        return reservations;
    }
    public void update(Reservation reservation) {
        String sql = "UPDATE Reservations SET guestId = ?, roomId = ?, checkInDate = ?, checkOutDate = ?, totalCost = ? WHERE reservationId = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, reservation.getGuestId());
            statement.setInt(2, reservation.getRoomId());
            statement.setDate(3, new java.sql.Date(reservation.getCheckInDate().getTime())); // Convert java.util.Date to java.sql.Date
            statement.setDate(4, new java.sql.Date(reservation.getCheckOutDate().getTime())); // Convert java.util.Date to java.sql.Date
            statement.setDouble(5, reservation.getTotalCost());
            statement.setInt(6, reservation.getReservationID());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                System.err.println("Update failed: No reservation found with ID " + reservation.getReservationID());
            }
        } catch (SQLException e) {
            System.err.println("Error updating reservation: " + e.getMessage());
        }
    }
    public void insert(Reservation reservation) {
        String sql = "INSERT INTO Reservations (guestId, roomId, checkInDate, checkOutDate, totalCost) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, reservation.getGuestId());
            statement.setInt(2, reservation.getRoomId());
            statement.setDate(3, new java.sql.Date(reservation.getCheckInDate().getTime())); // Convert java.util.Date to java.sql.Date
            statement.setDate(4, new java.sql.Date(reservation.getCheckOutDate().getTime())); // Convert java.util.Date to java.sql.Date
            statement.setDouble(5, reservation.getTotalCost());

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Reservation inserted successfully.");
            } else {
                System.err.println("Failed to insert reservation.");
            }
        } catch (SQLException e) {
            System.err.println("Error inserting reservation: " + e.getMessage());
        }
    }
    public void delete(int reservationId) {
        String sql = "DELETE FROM Reservations WHERE reservationId = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, reservationId);
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Reservation deleted successfully.");
            } else {
                System.out.println("No reservation found with ID " + reservationId + ", or it could not be deleted.");
            }
        } catch (SQLException e) {
            System.err.println("Error deleting reservation: " + e.getMessage());
        }
    }
}