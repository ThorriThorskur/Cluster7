package Controllers;

import EngineStuff.Cart;
import FlightSystem.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.ArrayList;

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
    private ComboBox<Seat> fxSeatChoice;
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
    @FXML
    private ImageView seatLayoutImageView;

    private Flight selectedFlight;

    private BookingFlightDB bookingFlightDB;

    private final int KR_PER_BAG = 5000;

    @FXML
    public void handleCancel(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void initData(Flight flight) throws ClassNotFoundException {
        this.selectedFlight = flight;
        fxDeparture.setText(flight.getLocationProperty());
        fxArrival.setText(flight.getDestinationProperty());
        populateSeatChoiceBox(flight.getSeats());
        bookingFlightDB = new BookingFlightDB();
        fxTotalPrice.setText(String.valueOf(selectedFlight.getStartingPrice() ));
        createPriceBinding();
        Image seatLayoutImage = new Image(getClass().getResourceAsStream("/Images/img.png"));
        seatLayoutImageView.setImage(seatLayoutImage);
    }

    private void createPriceBinding() {
        fxBagsSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            int current = selectedFlight.getStartingPrice();
            fxTotalPrice.setText(String.valueOf(current + (KR_PER_BAG*newValue)));
        });
    }

    private void populateSeatChoiceBox(ArrayList<Seat> seats) {
        ArrayList<Seat> availableSeats = new ArrayList<>();
        for (Seat seat : seats) {
            if (!seat.getBooked()){
                availableSeats.add(seat);
            }
        }
        fxSeatChoice.setItems(FXCollections.observableArrayList(availableSeats));
    }

    @FXML
    public void handleConfirmBooking() throws ClassNotFoundException {
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
        Seat seat = fxSeatChoice.getValue();
        int bags = fxBagsSpinner.getValue();

        if (validateInput(name, passportNumber, address, email, phoneNumber)) {
            Passenger passenger = new Passenger(name, passportNumber, address, email, phoneNumber);
            BookingFlight booking = new BookingFlight(selectedFlight, seat, passenger, false, bags);
            //selectedFlight.getSeat(seat.getSeatName()).setIsonHold(true);
            //bookingFlightDB.insert(booking);

            Cart.getInstance().addBooking(booking);

            initData(selectedFlight);
            clearFields();

            System.out.println("Booking created with ID: " + booking.getId());
            Stage stage = (Stage) fxName.getScene().getWindow();
            stage.close();

        } else {
            setErrorMessage("Please fill all fields correctly.");
        }
    }

    private void clearFields(){
        fxName.clear();
        fxPassportNumber.clear();
        fxAddress.clear();
        fxEmail.clear();
        fxPhoneNumber.clear();
        fxBagsSpinner.getValueFactory().setValue(0);
        fxSeatChoice.getSelectionModel().clearSelection();
    }


    private boolean validateInput(String name, int passportNumber, String address, String email, String phoneNumber) {
        return !(name.isBlank() || address.isBlank() || email.isBlank() || !email.contains("@") || phoneNumber.isBlank());
    }

    private void setErrorMessage(String message) {
        fxErrorMessage.setText(message);
    }
}
