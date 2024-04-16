package Controllers;

import DayTours.DayTourBooking;
import DayTours.Tour;
import DayTours.User;
import EngineStuff.Cart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class BookDayTourController {

    @FXML
    private TextField fxTourName;
    @FXML
    private Label fxPrice;

    @FXML
    private TextField fxTourEmail;
    @FXML
    private TextField fxPhoneNumber;

    private Tour selectedTour;
    private static final String DB_URL = "jdbc:sqlite:src/main/resources/Databases/tours.db";

    @FXML
    private Label fxtitle;

    @FXML
    private Label fxErrorMessage;
    private DayTourController dayTourController;



    @FXML
    private void handleConfirmBooking() throws ClassNotFoundException, SQLException {
        User currentUser = new User(UUID.randomUUID(), fxTourName.getText(), fxTourEmail.getText());
        DayTourBooking bookingTour = new DayTourBooking(UUID.randomUUID(), selectedTour, currentUser, LocalDate.now(), LocalTime.now(), selectedTour.getLocationName());
        if (selectedTour.getCapacity() > 0) {
            insertUser(currentUser);
            String sqlBooking = "INSERT INTO Booking (bookingID, tourID, userID, pickUpLocation) VALUES (?, ?, ?, ?)";
            String sqlTour = "UPDATE Tours SET capacity = ?, availability = ? WHERE tourID = ?";

            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmtBooking = conn.prepareStatement(sqlBooking);
                 PreparedStatement pstmtTour = conn.prepareStatement(sqlTour)) {

                // Insert booking
                pstmtBooking.setString(1, bookingTour.getId().toString());
                pstmtBooking.setString(2, selectedTour.getTourId().toString());
                pstmtBooking.setString(3, currentUser.getId().toString());
                pstmtBooking.setString(4, selectedTour.getLocationName());
                pstmtBooking.executeUpdate();

                // Update tour capacity and availability
                int newCapacity = selectedTour.getCapacity() - 1;
                boolean newAvailability = newCapacity > 0;
                pstmtTour.setInt(1, newCapacity);
                pstmtTour.setBoolean(2, newAvailability);
                pstmtTour.setString(3, selectedTour.getTourId().toString());
                pstmtTour.executeUpdate();

                selectedTour.setCapacity(newCapacity);

                System.out.println("Booking created with ID: " + bookingTour.getId());

            } catch (SQLException e) {
                System.out.println("Error occurred while adding the booking: " + e.getMessage());
            }

            Cart.getInstance().addBooking(bookingTour);
            initData(selectedTour, dayTourController);
            clearFields();
        }
        else {
            fxErrorMessage.setText("Sorry, this tour is fully booked.");
        }
    }

    private void clearFields(){
        fxTourEmail.clear();
        fxTourName.clear();
        fxPhoneNumber.clear();
        fxErrorMessage.setText("");
    }
    public void initData(Tour tour, DayTourController dayTourController) throws ClassNotFoundException, SQLException {
        this.selectedTour = tour;
        fxtitle.setText(selectedTour.getName() + " " + selectedTour.getDateOnly());
        fxPrice.setText(String.valueOf(selectedTour.getPrice()));
        this.dayTourController =dayTourController;
        ActionEvent event = new ActionEvent();
        dayTourController.searchClick(event);

    }

    public void insertUser(User user) throws SQLException {
        String sql = "INSERT INTO Users (userID, name, email, userType) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getId().toString());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, "Regular");

            pstmt.executeUpdate();
            System.out.println("User inserted: " + user.getName());
        } catch (SQLException e) {
            System.out.println("Error occurred while inserting the user: " + e.getMessage());
            throw e;
        }
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
}
