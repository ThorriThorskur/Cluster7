package Controllers;

import DayTours.DayTourBooking;
import DayTours.Tour;
import DayTours.User;
import FlightSystem.Flight;
import Interface.InterfaceService;
import Interface.InterfaceServiceController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


import java.sql.*;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;

public class DayTourController implements InterfaceServiceController {

    @FXML
    TextField fxTourName;

    @FXML
    DatePicker fxDate;

    @FXML
    ChoiceBox fxCategory;

    @FXML
    private TableView<Tour> tableTours;

    @FXML
    private TableColumn<Tour, String> fxcolumnTourName;
    @FXML
    private TableColumn<Tour, String> fxcolumnAddress;
    @FXML
    private TableColumn<Tour, LocalDateTime> fxcolumnDate;
    @FXML
    private TableColumn<Tour, String> fxcolumnTime; // Assuming this should actually be category
    @FXML
    private TableColumn<Tour, Float> fxcolumnRating;
    @FXML
    private TableColumn<Tour, Integer> fxcolumnPrice;

    private List<DayTourBooking> bookings;

    private static final String DB_URL = "jdbc:sqlite:src/main/resources/Databases/tours.db";

    public DayTourController() {

        this.bookings = new ArrayList<>();
    }

    public void initialize() {
        // Bind the table columns to the Tour properties
        fxcolumnTourName.setCellValueFactory(new PropertyValueFactory<>("name"));
        fxcolumnAddress.setCellValueFactory(new PropertyValueFactory<>("description"));
        fxcolumnDate.setCellValueFactory(new PropertyValueFactory<>("timeDateTour")); // Make sure the format is suitable
        fxcolumnTime.setCellValueFactory(new PropertyValueFactory<>("category")); // Corrected to Category
        fxcolumnRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        fxcolumnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    public Collection<Tour> search(String query) {
        ArrayList<Tour> foundTours = new ArrayList<>();
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

    public void searchClick(ActionEvent actionEvent) throws SQLException, ParseException {
        System.out.println("Whattup!");
        ArrayList<Tour> tourArrayList = new ArrayList<>();
        String tourName = fxTourName.getText();
        String category = String.valueOf(fxCategory.getValue());
        String date = String.valueOf(fxDate.getValue());

        ObservableList<Tour> tourObs = FXCollections.observableArrayList(search(tourName));
        System.out.println(Arrays.toString(search(tourName).toArray()));
        tableTours.setItems(tourObs);
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

