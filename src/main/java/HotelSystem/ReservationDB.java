package HotelSystem;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDB {
    public static void insert(Reservation reservation) {
        String sql = "INSERT INTO Reservations (roomId, checkInDate, checkOutDate, totalCost) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            java.sql.Date sqlCheckInDate = java.sql.Date.valueOf(reservation.getCheckInDate());
            java.sql.Date sqlCheckOutDate = java.sql.Date.valueOf(reservation.getCheckOutDate());

            statement.setInt(1, reservation.getRoomId());
            statement.setDate(2, sqlCheckInDate);
            statement.setDate(3, sqlCheckOutDate);
            statement.setDouble(4, reservation.getTotalCost());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating reservation failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int reservationId = generatedKeys.getInt(1);
                    reservation.setReservationID(reservationId);
                } else {
                    throw new SQLException("Creating reservation failed, no ID obtained.");
                }
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

    public static Reservation createReservation(Room selectedRoom, LocalDate checkInDate, LocalDate checkOutDate) {
        double pricePerNight = selectedRoom.getPricePerNight();
        long numOfDays = checkOutDate.toEpochDay() - checkInDate.toEpochDay();
        double totalCost = numOfDays * pricePerNight;

        Reservation newReservation = new Reservation(0, selectedRoom.getRoomId(), checkInDate, checkOutDate, totalCost);
        ReservationDB.insert(newReservation);

        if (newReservation != null) {
            System.out.println("Reservation created successfully: " + newReservation);
            return newReservation;
        } else {
            System.err.println("Failed to create reservation.");
            return null;
        }
    }

    public static List<Reservation> getReservationsByRoom(Room room) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM Reservations WHERE roomId = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, room.getRoomId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int reservationId = resultSet.getInt("reservationId");
                    LocalDate checkInDate = resultSet.getDate("checkInDate").toLocalDate();
                    LocalDate checkOutDate = resultSet.getDate("checkOutDate").toLocalDate();
                    double totalCost = resultSet.getDouble("totalCost");

                    Reservation reservation = new Reservation(reservationId, room.getRoomId(), checkInDate, checkOutDate, totalCost);
                    reservations.add(reservation);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving reservations " + e.getMessage());
        }
        return reservations;
    }

}