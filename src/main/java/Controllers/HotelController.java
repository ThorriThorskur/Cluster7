package Controllers;

import EngineStuff.Cart;
import EngineStuff.Location;
import HotelSystem.*;
import Interface.InterfaceService;
import Interface.InterfaceServiceController;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class HotelController implements InterfaceServiceController {

    @FXML
    private ChoiceBox<Hotel> cmbHotels;
    @FXML
    private ChoiceBox<String> cmbLocation;
    @FXML
    private TableView<Room> tableRooms;
    @FXML
    private TableColumn<Room, String> columnRoomNumber;
    @FXML
    private TableColumn<Room, String> columnRoomType;
    @FXML
    private TableColumn<Room, String> columnAvailability;
    @FXML
    private TableColumn<Room, Double> columnPricePerNight;
    @FXML
    private DatePicker dpCheckIn;
    @FXML
    private DatePicker dpCheckOut;

    @FXML
    private TextField txtMaxPrice;


    private HotelDB hotelDB;
    private RoomDB roomDB;

    public HotelController() {
        this.hotelDB = new HotelDB();
        this.roomDB = new RoomDB();
    }

    public void initialize() {
        populateLocations();
        setupTableColumns();
        hotelInitialPopUp();
        cmbLocation.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            populateHotels(newValue);
        });
    }

    public void populateLocations() {
        List<String> locations = getHotelLocations();
        ObservableList<String> hotelLocations = FXCollections.observableArrayList(locations);
        cmbLocation.setItems(hotelLocations);
    }

    public void populateHotels(String locationName) {
        List<Hotel> hotels = hotelDB.getHotelsByLocation(locationName);
        ObservableList<Hotel> hotelList = FXCollections.observableArrayList(hotels);
        cmbHotels.setItems(hotelList);
    }

    public void hotelInitialPopUp() {
        List<Hotel> allHotels = hotelDB.selectAll();
        ObservableList<Hotel> hotelList = FXCollections.observableArrayList(allHotels);
        cmbHotels.setItems(hotelList);
    }

    public List<String> getHotelLocations() {
        return hotelDB.getUniqueHotelLocations();
    }

    private void setupTableColumns() {
        columnRoomNumber.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getRoomNumber())));

        columnRoomType.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getType().toString()));

        columnAvailability.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().isAvailable() ? "Available" : "Unavailable"));

        columnPricePerNight.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getPricePerNight()).asObject());
    }

    public List<Hotel> search(String query) {
        return hotelDB.searchHotels(query);
    }


    @FXML
    private void onHotelSearchButtonClicked() {
        Hotel selectedHotel = cmbHotels.getValue();
        LocalDate checkInDate = dpCheckIn.getValue();
        LocalDate checkOutDate = dpCheckOut.getValue();

        final double[] maxPricePerNight = {Double.POSITIVE_INFINITY};

        String maxPriceText = txtMaxPrice.getText();
        if (!maxPriceText.isEmpty()) {
            try {
                maxPricePerNight[0] = Double.parseDouble(maxPriceText);
            } catch (NumberFormatException e) {
                return;
            }
        }

        if (selectedHotel != null && checkInDate != null && checkOutDate != null && checkInDate.isBefore(checkOutDate)) {
            String hotelName = selectedHotel.getName();
            Location hotelLocation = selectedHotel.getLocation();

            List<Room> rooms = roomDB.searchRoomsByHotelAndDates(hotelName, checkInDate, checkOutDate);

            rooms = rooms.stream()
                    .filter(room -> room.getPricePerNight() <= maxPricePerNight[0])
                    .filter(room -> isRoomAvailableForDates(room, checkInDate, checkOutDate)) // Filter out unavailable rooms
                    .collect(Collectors.toList());

            System.out.println(rooms);
            tableRooms.setItems(FXCollections.observableArrayList(rooms));
        }
    }


    @FXML
    private void onBookHotelButtonClicked() {
        Room selectedRoom = tableRooms.getSelectionModel().getSelectedItem();
        LocalDate checkInDate = dpCheckIn.getValue();
        LocalDate checkOutDate = dpCheckOut.getValue();

        if (selectedRoom != null && checkInDate != null && checkOutDate != null && checkInDate.isBefore(checkOutDate)) {
            Reservation reservation = ReservationDB.createReservation(selectedRoom, checkInDate, checkOutDate);
            Cart.getInstance().addBooking(reservation);

            onHotelSearchButtonClicked();//prevents the user from adding the same room to the cart twice
        } else {
        }
    }

    private boolean isRoomAvailableForDates(Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        List<Reservation> existingReservations = ReservationDB.getReservationsByRoom(room);
        for (Reservation reservation : existingReservations) {
            LocalDate reservationCheckInDate = reservation.getCheckInDate();
            LocalDate reservationCheckOutDate = reservation.getCheckOutDate();
            if (!checkOutDate.isBefore(reservationCheckInDate) && !checkInDate.isAfter(reservationCheckOutDate)) {
                return false;
            }
        }
        return true;
    }


    public void addService(InterfaceService service) {
        if (service instanceof Hotel) {
            Hotel hotel = (Hotel) service;
        }
    }

    public void removeService(InterfaceService service) {
        if (service instanceof Hotel) {
            Hotel hotel = (Hotel) service;
        }
    }
}