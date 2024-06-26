package Controllers;
import DayTours.DayTourBooking;
import DayTours.Tour;
import Interface.InterfaceService;
import Interface.InterfaceServiceController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
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
    private TableColumn<Tour, String> fxcolumnDescription;
    @FXML
    private TableColumn<Tour, LocalDateTime> fxcolumnDate;
    @FXML
    private TableColumn<Tour, String> fxcolumnTime;
    @FXML
    private TableColumn<Tour, Float> fxcolumnRating;
    @FXML
    private TableColumn<Tour, Integer> fxcolumnPrice;
    @FXML
    private TableColumn<Tour, String> fxcolumnLocation;
    @FXML
    private TableColumn<Tour, Boolean> fxcolumnFamily;
    @FXML
    private TableColumn<Tour, Boolean> fxcolumnWheelchair;
    @FXML
    private TableColumn<Tour, Boolean> fxcolumnAvailability;
    @FXML
    private TableColumn<Tour, Integer> fxcolumnCapacity;
    @FXML
    private TableColumn<Tour, UUID> fxcolumnID;

    private List<DayTourBooking> bookings;

    private static final String DB_URL = "jdbc:sqlite:src/main/resources/Databases/tours.db";

    @FXML
    private void handleBookTour() throws IOException, ClassNotFoundException, SQLException {
        try { Tour selectedTour = tableTours.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Controllers/DayTourBooking.fxml"));
            Parent root = loader.load();
            BookDayTourController controller = loader.getController();
            controller.initData(selectedTour, this);

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Book Day Tour");
            stage.setScene(new Scene(root));
            stage.showAndWait();
       }
       catch (Exception e)
       {
           System.out.println("No tour was chosen.");
       }
    }

    public DayTourController() {
        this.bookings = new ArrayList<>();
    }

    public void initialize() {
        fxcolumnTourName.setCellValueFactory(new PropertyValueFactory<>("name"));
        fxcolumnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        fxcolumnDate.setCellValueFactory(new PropertyValueFactory<>("dateOnly"));
        fxcolumnTime.setCellValueFactory(new PropertyValueFactory<>("category"));
        fxcolumnRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        fxcolumnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        fxcolumnLocation.setCellValueFactory(new PropertyValueFactory<>("locationName"));
        fxcolumnFamily.setCellValueFactory(new PropertyValueFactory<>("childSafe"));
        fxcolumnWheelchair.setCellValueFactory(new PropertyValueFactory<>("wheelchairAccessible"));
        fxcolumnAvailability.setCellValueFactory(new PropertyValueFactory<>("availability"));
        fxcolumnCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        fxcolumnID.setCellValueFactory(new PropertyValueFactory<>("id"));


        ObservableList<String> categoryObs = FXCollections.observableArrayList(getCategories());
        fxCategory.setItems(categoryObs);

        Set<LocalDate> availableDates = getAvailableTourDates();

        fxDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date != null && !empty) {
                    if (availableDates.contains(date)) {
                        setStyle("-fx-background-color: #90EE90;");
                        setTooltip(new Tooltip("Available Tour!"));
                    }
                }
            }
        });
    }



    private Set<LocalDate> getAvailableTourDates() {
        Set<LocalDate> availableDates = new HashSet<>();
        String sql = "SELECT DISTINCT date(timedateTour) FROM Tours WHERE availability = 1";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                LocalDate date = LocalDate.parse(rs.getString(1));
                availableDates.add(date);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching available tour dates: " + e.getMessage());
        }
        return availableDates;
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
                pstmt.setBoolean(9, t.getChildSafe());
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


    private Collection<Tour> searchTours(String tourName, String category, LocalDate date) {
        List<Tour> foundTours = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Tours WHERE ");
        List<String> conditions = new ArrayList<>();
        List<Object> parameters = new ArrayList<>();

        if (tourName != null && !tourName.isBlank()) {
            conditions.add("name LIKE ?");
            parameters.add("%" + tourName + "%");
        }
        if (category != null && !category.isBlank()) {
            conditions.add("category = ?");
            parameters.add(category);
        }
        if (date != null) {
            conditions.add("date(timedateTour) = ?");
            parameters.add(date.toString());
        }

        if (conditions.isEmpty()) {
            sql.append("1=1");
        } else {
            sql.append(String.join(" AND ", conditions));
        }

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Tour tour = extractTourFromResultSet(rs);
                    foundTours.add(tour);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error occurred during search: " + e.getMessage());
        }
        return foundTours;
    }
    private Tour extractTourFromResultSet(ResultSet rs) throws SQLException {
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

        return tour;
    }
    public void searchClick(ActionEvent actionEvent) throws SQLException {
        String tourName = fxTourName.getText();
        Object categoryObj = fxCategory.getValue();
        String category = categoryObj != null ? categoryObj.toString() : null;
        LocalDate date = fxDate.getValue();

        Collection<Tour> foundTours = searchTours(tourName, category, date);
        ObservableList<Tour> tourObs = FXCollections.observableArrayList(foundTours);
        tableTours.setItems(tourObs);
    }


    private static ArrayList<String> getCategories() {
        ArrayList<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM tours";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            System.err.println("SQL Error occurred while fetching categories: " + e.getMessage());
            e.printStackTrace();
        }

        return categories;
    }

    // Getters and setters
    private List<DayTourBooking> getBookings() {
        return bookings;
    }

    private void setBookings(List<DayTourBooking> bookings) {
        this.bookings = bookings;
    }

    private String viewBookingDetails(DayTourBooking booking) {
        return booking.getPickUpLocation();
    }

}

