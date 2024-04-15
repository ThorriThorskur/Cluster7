package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import FlightSystem.Flight;

public class BookFlightController {
    private Flight flight;

    @FXML
    private Label fxDeparture;
    @FXML
    private Label fxArrival;
    @FXML
    private Label fxPriceField;

    public BookFlightController() {
        this.flight = null;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
        initialize();
    }

    @FXML
    private void initialize() {
        if (flight != null) {
            fxDeparture.setText(flight.getLocationProperty());
            fxArrival.setText(flight.getDestinationProperty());
            fxPriceField.setText(String.valueOf(flight.getPriceProperty()) + " kr");
        }
    }

    @FXML
    public void handleCancel(ActionEvent actionEvent) {
       Node source = (Node) actionEvent.getSource();
       Stage stage = (Stage) source.getScene().getWindow();
       stage.close();
    }
}