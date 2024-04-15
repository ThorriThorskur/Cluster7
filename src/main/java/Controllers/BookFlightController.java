package Controllers;

import FlightSystem.BookingFlight;
import FlightSystem.Flight;
import FlightSystem.Passenger;
import FlightSystem.Seat;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class BookFlightController {
    @FXML
    private TextField fxName;
    @FXML
    private TextField fxPassportNumber;
    @FXML
    private TextField fxAddress;
    @FXML
    private TextField fxEmail;
    @FXML
    private TextField fxPhoneNumber;
    @FXML
    private ComboBox<String> fxSeatChoice;
    @FXML
    private Spinner<Integer> fxBagsSpinner;
    @FXML
    private Label fxDeparture;
    @FXML
    private Label fxArrival;
    @FXML
    private Label fxTotalPrice;
    @FXML
    private Label fxErrorMessage;

    private Flight selectedFlight;

   @FXML
    public void handleCancel(ActionEvent actionEvent) {
       Node source = (Node) actionEvent.getSource();
       Stage stage = (Stage) source.getScene().getWindow();
       stage.close();
    }

    public void initData(Flight flight) {
        this.selectedFlight = flight;
        fxDeparture.setText(flight.getLocationProperty());
        fxArrival.setText(flight.getDestinationProperty());
        populateSeatChoiceBox(flight.getSeats());
    }

    private void populateSeatChoiceBox(ArrayList<Seat> seats) {
        fxSeatChoice.setItems(FXCollections.observableArrayList(seats.stream().map(Seat::toString).collect(Collectors.toList())));
    }

    @FXML
    public void handleConfirmBooking() {
        String name = fxName.getText();
        int passportNumber;
        try {
            passportNumber = Integer.parseInt(fxPassportNumber.getText());
        } catch (NumberFormatException e) {
            setErrorMessage("Passport number must be a valid number.");
            return;
        }
        String address = fxAddress.getText();
        String email = fxEmail.getText();
        String phoneNumber = fxPhoneNumber.getText();
        String seatId = fxSeatChoice.getValue();
        int bags = fxBagsSpinner.getValue();

        if (validateInput(name, passportNumber, address, email, phoneNumber)) {
            Passenger passenger = new Passenger(name, passportNumber, address, email, phoneNumber);
            Seat seat = new Seat(seatId, false, false);
            BookingFlight booking = new BookingFlight(selectedFlight, seat, passenger, false, bags);

            System.out.println("Booking created with ID: " + booking.getId());
            setErrorMessage("Booking successful: " + booking.getId());
        } else {
            setErrorMessage("Please fill all fields correctly.");
        }
    }

    private boolean validateInput(String name, int passportNumber, String address, String email, String phoneNumber) {
        return !(name.isBlank() || address.isBlank() || email.isBlank() || !email.contains("@") || phoneNumber.isBlank());
    }

    private void setErrorMessage(String message) {
        fxErrorMessage.setText(message);
    }
}
