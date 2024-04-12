package FlightSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class BookingFlightDB {
    /*Where all queries about bookings and updating and creating them will reside.*/

    static Connection c;

    FlightDB flightDB;

    public BookingFlightDB() throws ClassNotFoundException {
        initialize();
        flightDB = new FlightDB();
    }

    // initializes the connection from the controller
    public static void initialize() throws ClassNotFoundException {
        c = null;
        Class.forName("org.sqlite.JDBC");

        try {
            c = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Databases/flightsystem.db");
        } catch (Exception e){
            System.out.println("no connection was possible");
        }

    }

    public void closeConnection() throws SQLException {
        c.close();
    }

    public ArrayList<BookingFlight> select(BookingFlight b) {
        return null;
    }

    public void update(BookingFlight b) {
        return;
    }

    public void delete(BookingFlight b) {
        return;
    }

    public void insert(BookingFlight b) {
        flightDB.updateSeatToTaken(b.getBookedFlight(), b.getSeat());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String depDate = dateFormat.format(b.getBookedFlight().getDepartureDate());

        try {
            String insert = "INSERT INTO Booking VALUES ((?), (?),(?),(?),(?),(?),(?))";
            PreparedStatement prep = c.prepareStatement(insert);
            prep.setString(1, String.valueOf(b.getPassenger().getPassportNumber()));
            prep.setString(2, b.getBookedFlight().getId());
            prep.setString(3, b.getSeat().getSeatName());
            prep.setString(4, depDate);
            prep.setString(5, depDate);
            prep.setBoolean(6, false);
            prep.setString(7, b.getId());

            prep.executeUpdate();

        } catch (Exception e){
            System.out.println("Issues updating the Booking table)");
        }

        return;
    }
}
