package Controllers;

import DayTours.DayTourBooking;
import DayTours.Tour;
import DayTours.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class BookDayTourController {

    @FXML
    private Label fxTourName;
    @FXML
    private Label fxTotalPrice;
    @FXML
    private Spinner<Integer> fxTicketsSpinner;

    private Tour selectedTour;


    @FXML
    private void handleConfirmBooking() {
        User currentUser = new User(UUID.randomUUID(), fxName.getText(), fxEmail.getText());
        DayTourBooking bookingTour = new DayTourBooking(UUID.randomUUID(), selectedTour, currentUser, LocalDate.now(), LocalTime.now(), selectedTour.getLocationName());
        if (selectedTour.getCapacity() != 0) {
            // Example SQL to insert into the database (you'd use a database handler in your application)
            String sql = "INSERT INTO Booking (bookingID, tourID, userID, pickUpLocation) VALUES ('"
                    + UUID.randomUUID() + "', '" + bookingTour.getTour() + "', '" + currentUser.getId() + "', '" + selectedTour.getLocation() + "')";
            System.out.println("Booking created with ID: " + bookingID);

            selectedTour.setCapacity(selectedTour.getCapacity()-1);
        }
    }
    public void initData(Tour tour) throws ClassNotFoundException {
        this.selectedTour = tour;

    }


    @FXML
    public void handleCancel(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
}
