package Controllers;

import DayTours.Tour;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;

public class BookDayTourController {

    @FXML
    private Label fxTourName;
    @FXML
    private Label fxTotalPrice;
    @FXML
    private Spinner<Integer> fxTicketsSpinner;

    public void initData(Tour selectedTour) {
        //logic to set label tour name and such can be implemented here :)
    }

    @FXML
    private void handleConfirmBooking() {
        // Booking confirmation logic can be implemented here :)
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
}
