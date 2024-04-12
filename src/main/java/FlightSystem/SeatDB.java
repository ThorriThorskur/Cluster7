package FlightSystem;

import java.sql.*;
import java.util.ArrayList;

public class SeatDB {
    static Connection c;

    public SeatDB() throws ClassNotFoundException {
        initialize();
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

    // This method finds the seats for a given flight in order to create the seat array for the flight object.
    public ArrayList<Seat> findSeats(String flightID, String depDate) throws SQLException {
        // The flights objects need location, destination, date, id, ArrayList<Seat> seats, status
        String sqlSeats = "Select Seat, Taken from Flights where FlightId = (?) AND DepDate = (?)";
        PreparedStatement prepSeats = c.prepareStatement(sqlSeats);
        prepSeats.setString(1,flightID);
        prepSeats.setString(2,depDate);
        ResultSet resultSeats = prepSeats.executeQuery();

        ArrayList<Seat> seats = new ArrayList<>();

        while(resultSeats.next()){
            seats.add(new Seat(resultSeats.getString(1), resultSeats.getBoolean(2), false));
        }
        return seats;

    }

    public void closeConnection() throws SQLException {
        c.close();
    }
}
