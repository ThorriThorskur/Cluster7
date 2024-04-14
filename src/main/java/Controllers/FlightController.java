package Controllers;

import Interface.InterfaceService;
import Interface.InterfaceServiceController;
import FlightSystem.BookingFlightDB;
import FlightSystem.Flight;
import FlightSystem.FlightDB;
import FlightSystem.Seat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

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
    private ChoiceBox<String> fxDepDate;

    @FXML
    private ChoiceBox<String> fxArrDate;

    @FXML
    private DatePicker dpDate;

    @FXML
    private TextField txtMaxPrice;



    public void initialize() throws ClassNotFoundException {
        this.db = new FlightDB();
        this.bookingDb = new BookingFlightDB();
        dpDate.setValue(LocalDate.now());
        populateDropDown();
    }

    public void populateDropDown() throws ClassNotFoundException {
        ArrayList<String> airportNames = getAirportNames();
        ObservableList<String> airportObs = FXCollections.observableArrayList(airportNames);
        fxDepDate.setItems(airportObs);
        fxArrDate.setItems(airportObs);
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
}
