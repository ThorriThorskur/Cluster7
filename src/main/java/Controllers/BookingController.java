package Controllers;

import DayTours.DayTourBooking;
import DayTours.Tour;
import HotelSystem.Reservation;
import Interface.InterfaceBooking;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import FlightSystem.*;

public class BookingController {
    @FXML
    private TableView<InterfaceBooking> myTableView;
    @FXML
    private TableColumn<InterfaceBooking, String> typeColumn;
    @FXML
    private TableColumn<InterfaceBooking, String> detailsColumn;
    @FXML
    private TableColumn<InterfaceBooking, Double> priceColumn;

    private BookingFlightDB bookingFlightDB;

    private Stage stage;
    private Scene scene;

    @FXML
    public void initialize() throws ClassNotFoundException {
        // Setup columns
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        detailsColumn.setCellValueFactory(new PropertyValueFactory<>("details"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        bookingFlightDB = new BookingFlightDB();
    }

    public void handleBack(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("TravelApp");
        stage.setScene(scene);
        stage.show();

        //debug
        System.out.println("handleBack");
    }

    public void setBookingItems(ObservableList<InterfaceBooking> items) {
        myTableView.setItems(items);
    }

    @FXML
    public void handlePurchase(ActionEvent actionEvent) {
        ObservableList<InterfaceBooking> items = myTableView.getItems();
        List<InterfaceBooking> toRemove = new ArrayList<>();

        for (InterfaceBooking item : items) {
            try {
                if (item instanceof BookingFlight) {
                    BookingFlight flightBooking = (BookingFlight) item;
                    handleFlightBooking(flightBooking);
                    toRemove.add(flightBooking);
                } else if (item instanceof DayTourBooking) {
                    DayTourBooking tourBooking = (DayTourBooking) item;
                    handleDayTourBooking(tourBooking);
                    toRemove.add(tourBooking);
                } else if (item instanceof Reservation) {
                    Reservation roomBooking = (Reservation) item;

                    toRemove.add(roomBooking);
                }
            } catch (Exception e) {
                System.out.println("Error processing booking: " + e.getMessage());
            }
        }

        items.removeAll(toRemove);
    }

    private void handleFlightBooking(BookingFlight booking) {
        try {
            if (!booking.isPaidFor()) {
                booking.setPaidFor(true);
                booking.getBookedFlight().getSeat(booking.getSeat().getSeatName()).setBooked(true);
                bookingFlightDB.insert(booking);
                System.out.println("Booking processed: " + booking.getId());
            }
        } catch (Exception e) {
            System.out.println("Error processing booking: " + e.getMessage());
        }
    }

    private void handleDayTourBooking(DayTourBooking booking) {
        String sql = "INSERT INTO Booking (bookingID, tourID, userID, pickUpLocation) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Databases/tours.db");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, booking.getId().toString());
            pstmt.setString(2, booking.getTour().getTourId().toString());
            pstmt.setString(3, booking.getUser().getId().toString());
            pstmt.setString(4, booking.getPickUpLocation());
            pstmt.executeUpdate();

            System.out.println("Booking processed: " + booking.getId());
        } catch (SQLException e) {
            System.out.println("Error processing booking: " + e.getMessage());
        }
    }
}