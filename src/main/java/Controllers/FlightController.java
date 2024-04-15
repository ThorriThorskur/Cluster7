package Controllers;

import Interface.InterfaceService;
import Interface.InterfaceServiceController;
import FlightSystem.BookingFlightDB;
import FlightSystem.Flight;
import FlightSystem.FlightDB;
import FlightSystem.Seat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FlightController implements InterfaceServiceController {
    private FlightDB db;
    private BookingFlightDB bookingDb;

    @FXML
    private ChoiceBox<String> fxDeparture;

    @FXML
    private ChoiceBox<String> fxDestination;

    @FXML
    private DatePicker dpDate;

    @FXML
    private TextField txtMaxPrice;

    @FXML
    private TableView<Flight> tableFlights;

    @FXML
    private TableColumn<Flight, String> columnFlightName;

    @FXML
    private TableColumn<Flight, String> columnDeparture;

    @FXML
    private TableColumn<Flight, String> columnArrival;

    @FXML
    private TableColumn<Flight, String> columnDepartureTime;

    @FXML
    private TableColumn<Flight, String> columnArrivalTime;

    @FXML
    private TableColumn<Flight, String> columnPrice;

    @FXML
    private void handleBookFlight() throws IOException {
        Flight selectedFlight = tableFlights.getSelectionModel().getSelectedItem();
        if (selectedFlight != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Controllers/FlightBooking.fxml"));
            Parent root = loader.load();
            BookFlightController controller = loader.getController();
            controller.initData(selectedFlight);

            Stage stage = new Stage();

            BookFlightController controller = loader.getController();
            controller.setFlight(tableFlights.getSelectionModel().getSelectedItem());

            stage.setTitle("Book Flight");
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.show();
        } else {
            //show error message to the user maybe?
            System.out.println("No flight selected.");
        }
    }

    public void initialize() throws ClassNotFoundException {
        this.db = new FlightDB();
        this.bookingDb = new BookingFlightDB();
        dpDate.setValue(LocalDate.now());
        populateDropDown();
        createTableViewBindings();
    }

    public void createTableViewBindings() {
        columnFlightName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        columnDeparture.setCellValueFactory(cellData -> cellData.getValue().locationProperty());
        columnArrival.setCellValueFactory(cellData -> cellData.getValue().destinationProperty());
        columnDepartureTime.setCellValueFactory(cellData -> cellData.getValue().depTimeProperty());
        columnArrivalTime.setCellValueFactory(cellData -> cellData.getValue().arrTimeProperty());
        columnPrice.setCellValueFactory(cellData -> cellData.getValue().priceProperty());

    }

    public void populateDropDown() throws ClassNotFoundException {
        ArrayList<String> airportNames = getAirportNames();
        ObservableList<String> airportObs = FXCollections.observableArrayList(airportNames);
        fxDeparture.setItems(airportObs);
        fxDestination.setItems(airportObs);
    }


    /*
    public FlightController(FlightDB flightDB, BookingFlightDB bookingDb){
        this.db = flightDB;
        this.bookingDb = bookingDb;
    }

     */

    @Override
    public Collection search(String query) {
        return List.of();
    }

    @Override
    public void addService(InterfaceService service) {
        Flight f = (Flight) service;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        try {
            db.create(f.getName(), f.getLocation().getName(), f.getDestination().getName(), dateFormat.format(f.getDepartureDate()), timeFormat.format(f.getDepartureDate()), dateFormat.format(f.getArrivalDate()), timeFormat.format(f.getArrivalDate()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeService(InterfaceService service) {
        Flight f = (Flight) service;
        db.delete(f);
    }


    public ArrayList<Flight> searchFlights(String departureLocation, String arrivalLocation, String date) throws SQLException, ParseException {
        return db.select(departureLocation, arrivalLocation, date);
    }

    public ArrayList<Flight> searchFlightsWithPrice(String departureLocation, String arrivalLocation, String date, int maxPrice) throws SQLException, ParseException {
        ArrayList<Flight> unfilteredFlights = searchFlights(departureLocation, arrivalLocation, date);
        ArrayList<Flight> filteredFlights = new ArrayList<>();
        for (Flight flight : unfilteredFlights) {
            if (flight.getStartingPrice() <= maxPrice) {
                filteredFlights.add(flight);
            }
        }
        return filteredFlights;
    }

    public void updateFlight(Flight f, String depDate, String depTime, String arrDate, String arrTime, String status) {
        db.update(f, depDate, depTime, arrDate, arrTime, status);
    }

    public ArrayList<String> getAirportNames(){
        return db.getAirportNames();
    }

    // TODO: Code how the available seat display is coded, both to show what is available and what happens when a seat is chosen.
    public Seat[][] getSeatAvailability(Flight flight){
        Seat [][] seats = new Seat[20][4];
        int columns = 4;
        int rows = 20;
        int seatCount = 0;
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++){
                seats[i][j] = flight.getSeats().get(seatCount++);
            }
        }

        return seats;
    }

    public void onSearchClick(ActionEvent actionEvent) throws SQLException, ParseException {
        ArrayList<Flight> flightArrayList = new ArrayList<>();
        String location = fxDeparture.getValue();
        String destination = fxDestination.getValue();
        String date = String.valueOf(dpDate.getValue());

        if (location != null && txtMaxPrice.getText().isEmpty()){
            flightArrayList = searchFlights(location, destination, date);
        } else {
            flightArrayList = searchFlightsWithPrice(location, destination, date, Integer.parseInt(txtMaxPrice.getText()));
        }
        ObservableList<Flight> flightObs = FXCollections.observableArrayList(flightArrayList);
        tableFlights.setItems(flightObs);

    }
}
