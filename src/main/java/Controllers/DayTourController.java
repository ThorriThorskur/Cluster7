package Controllers;

import DayTours.DayTourBooking;
import DayTours.Tour;
import DayTours.User;
import Interface.InterfaceService;
import Interface.InterfaceServiceController;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class DayTourController implements InterfaceServiceController {

    private List<DayTourBooking> bookings;

    private static final String DB_URL = "jdbc:sqlite:src/main/resources/Databases/tours.db";

    public DayTourController() {

        this.bookings = new ArrayList<>();
    }

    public Collection<Tour> search(String query) {
        List<Tour> foundTours = new ArrayList<>();
        String sql = "SELECT * FROM Tours WHERE name LIKE ? OR description LIKE ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + query + "%");
            pstmt.setString(2, "%" + query + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                UUID id = UUID.fromString(rs.getString("tourID"));
                String name = rs.getString("name");
                String description = rs.getString("description");
                int price = rs.getInt("price");
                String category = rs.getString("category");
                int capacity = rs.getInt("capacity");
                LocalDateTime timeDateTour = LocalDateTime.parse(rs.getString("timedateTour"));
                String location = rs.getString("tourAddress");
                boolean familyFriendly = rs.getBoolean("familyFriendly");
                boolean wheelchairAccessible = rs.getBoolean("wheelchairAccesible");
                boolean availability = rs.getBoolean("availability");
                float rating = rs.getFloat("rating");

                Tour tour = new Tour(id, name, description, category, price, capacity, timeDateTour, location, rating);
                tour.setDescription(description);
                tour.setChildSafe(familyFriendly);
                tour.setWheelchairAccessible(wheelchairAccessible);
                tour.setAvailability(availability);
                tour.setRating(rating);

                foundTours.add(tour);
            }
        } catch (SQLException e) {
            System.out.println("Error occurred during search: " + e.getMessage());
        }
        return foundTours;
    }

    public void addService(InterfaceService service) {
        if (service instanceof Tour) {
            Tour t = (Tour) service;
            String sql = "INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, t.getTourId().toString());
                pstmt.setString(2, t.getName());
                pstmt.setString(3, t.getDescription());
                pstmt.setInt(4, Math.round(t.getPrice()));
                pstmt.setString(5, t.getCategory());
                pstmt.setInt(6, (int) t.getRating());
                pstmt.setString(7, t.getTimeDateTour().toString());
                pstmt.setString(8, t.getLocation().getName());
                pstmt.setBoolean(9, t.isChildSafe());
                pstmt.setBoolean(10, t.isWheelchairAccessible());
                pstmt.setBoolean(11, t.isAvailability());
                pstmt.setInt(12, t.getCapacity());

                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Tour successfully added to the database.");
                } else {
                    System.out.println("The tour could not be added.");
                }
            } catch (SQLException e) {
                System.out.println("Error occurred while adding the tour: " + e.getMessage());
            }
        }
    }


    public void removeService(InterfaceService service) {
        Tour t = (Tour) service;
        String sql = "DELETE FROM Tours WHERE tourID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, t.getTourId().toString());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Booking successfully cancelled.");
            } else {
                System.out.println("No booking found with the specified ID, or it could not be cancelled.");
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while cancelling the booking: " + e.getMessage());
        }
    }

    public void bookTour(DayTourBooking booking) {
        String sql = "INSERT INTO booking (bookingId, tourId, userId, pickUpLocation) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, UUID.randomUUID().toString());
            pstmt.setString(2, booking.getTour().getId().toString());
            pstmt.setString(3, booking.getUser().getId().toString());
            pstmt.setString(4, booking.getPickUpLocation());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Booking successfully added to the database.");
            } else {
                System.out.println("The booking could not be added.");
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while booking the tour: " + e.getMessage());
        }
    }

    // Getters and setters
    public List<DayTourBooking> getBookings() {
        return bookings;
    }

    public void setBookings(List<DayTourBooking> bookings) {
        this.bookings = bookings;
    }

    public String viewBookingDetails(DayTourBooking booking) {
        return booking.getPickUpLocation();
    }

}

