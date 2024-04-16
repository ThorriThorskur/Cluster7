package Controllers;

import EngineStuff.Location;
import Interface.InterfaceService;
import Interface.InterfaceServiceController;
import HotelSystem.Hotel;
import HotelSystem.HotelDB;
import HotelSystem.Room;
import HotelSystem.RoomDB;
import HotelSystem.ReservationDB;

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

    public HotelController(HotelDB hotelDB, RoomDB roomDB, ReservationDB reservationDB) {
        this.hotelDB = hotelDB;
        this.roomDB = roomDB;
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
    public List<String> getHotelLocations(){
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
        System.out.println("Please select your dates");
        Hotel selectedHotel = cmbHotels.getValue();
        LocalDate checkInDate = dpCheckIn.getValue();
        LocalDate checkOutDate = dpCheckOut.getValue();

        final double[] maxPricePerNight = {Double.POSITIVE_INFINITY};

        String maxPriceText = txtMaxPrice.getText();
        if (!maxPriceText.isEmpty()) {
            try {
                maxPricePerNight[0] = Double.parseDouble(maxPriceText);
            } catch (NumberFormatException e) {
                System.err.println("Invalid input for max price per night");
                return;
            }
        }

        if (selectedHotel != null && checkInDate != null && checkOutDate != null) {
            String hotelName = selectedHotel.getName();
            Location hotelLocation = selectedHotel.getLocation();

            List<Room> rooms = roomDB.searchRoomsByHotelAndDates(hotelName, checkInDate, checkOutDate);

            rooms = rooms.stream()
                    .filter(room -> room.getPricePerNight() <= maxPricePerNight[0])
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

        if (selectedRoom != null && checkInDate != null && checkOutDate != null) {
            ReservationDB.createReservation(selectedRoom, checkInDate, checkOutDate);
            System.out.println("Booked hotel");
        } else {
            // Handle case when room or dates are not selected
            System.out.println("Please select a room and enter check-in and check-out dates.");
        }
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

    // Additional methods specific to hotels

    public Hotel getHotelDetails(String hotelId) {
        return hotelDB.select(hotelId);
    }

    public List<Hotel> listHotels() {
        return hotelDB.selectAll();
    }

    /**
     public void bookRoom(int guestId, int roomId, Date checkInDate, Date checkOutDate) {
     Reservation newReservation = new Reservation(guestId, roomId, checkInDate, checkOutDate);
     reservationDB.insert(newReservation);
     }
     */

    /**
    public List<Room> listRoomsByHotel() {
        return roomDB.listRoomsByHotel();
    }*/

    public void updateRoom(Room room) {
        roomDB.update(room);
    }

    public void deleteRoom(int roomId) {
        roomDB.delete(roomId);
    }

    /**
     public void createReservation(int guestId, int roomId, Date checkInDate, Date checkOutDate) {
     Reservation reservation = new Reservation(guestId, roomId, checkInDate, checkOutDate);
     double totalCost = calculateTotalCost(checkInDate, checkOutDate);
     reservation.setTotalCost(totalCost);
     reservationDB.insert(reservation);
     }*/

    private double calculateTotalCost(Date checkInDate, Date checkOutDate) {
        long diff = checkOutDate.getTime() - checkInDate.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000);
        return diffDays * 100.0; // Simplified cost calculation
    }
}
