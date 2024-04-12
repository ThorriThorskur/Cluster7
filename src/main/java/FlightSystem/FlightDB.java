package FlightSystem;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FlightDB {
    /* Where all queries about finding flights, updating flights and adding flights will reside.*/


    static Connection c;

    private SeatDB seatDB;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public FlightDB() throws ClassNotFoundException {
        initialize();
        seatDB = new SeatDB();
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

    /*
    This method will search based on given location, destination and date of take off and return an array list of Flight objects.
     */
    public ArrayList<Flight> select(String location, String destination, String date) throws SQLException, ParseException {
        try{

            String sql = "Select DISTINCT Flights.FlightID, DepTime, ArrTime from Flights, Airport a1, Airport a2 where Flights.FlightID = a1.FlightID " +
                    "AND Flights.FlightID = a2.FlightID AND a1.Location = (?) AND a2.location = (?) AND DepDate = (?) " +
                    "AND Taken=FALSE AND a1.FlightType='Departure' AND a2.FlightType='Arrival'";
            PreparedStatement preparedStatement = c.prepareStatement(sql);
            preparedStatement.setString(1, location);
            preparedStatement.setString(2,destination);
            preparedStatement.setString(3,date);
            ResultSet result = preparedStatement.executeQuery();

            ArrayList<Flight> flights = new ArrayList<>();

            while(result.next()){
                String flightID = result.getString(1);
                //Concatenating the departure and arrival date and time.
                String departureDateTime = date + " " + result.getString(2);
                String arrivalDateTime = date + " " + result.getString(3);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                java.util.Date depDateTime = dateFormat.parse(departureDateTime);
                java.util.Date arrDateTime = dateFormat.parse(arrivalDateTime);

                flights.add(new Flight(location,destination,depDateTime, arrDateTime,flightID, seatDB.findSeats(flightID,date), "On Time"));
            }
            return flights;

        } catch (Exception e){
            System.out.println("Error fetching the chosen flight date and location.");
            throw e;
        }
    }


    public void update(Flight f, String depDate, String depTime, String arrDate, String arrTime, String status) {
        try {
            String updateQuery = "UPDATE Flights " +
                    "SET DepDate = (?), DepTime = (?), ArrDate = (?), ArrTime = (?), Status = (?) " +
                    "WHERE FlightID = (?) AND DepDate = (?) AND FlightID IN " +
                    "(SELECT FlightID FROM Airport WHERE Location = (?) AND FlightType = 'Departure')";
            PreparedStatement updateStatement = c.prepareStatement(updateQuery);
            updateStatement.setString(1, depDate);
            updateStatement.setString(2, depTime);
            updateStatement.setString(3, arrDate);
            updateStatement.setString(4, arrTime);
            updateStatement.setString(5, status);
            updateStatement.setString(6, f.getId());
            updateStatement.setString(7, depDate);
            updateStatement.setString(8, f.getId());
            updateStatement.executeUpdate();

            System.out.println("Flight updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error updating the flight.");
            e.printStackTrace();
        }
    }

    public void delete(Flight f) {
        try {
            // Delete the flight from the Flights table
            String deleteFlightQuery = "DELETE FROM Flights " +
                    "WHERE FlightID = (?) AND DepDate = (?) AND FlightID IN " +
                    "(SELECT FlightID FROM Airport WHERE Location = (?) AND FlightType = 'Departure')";
            PreparedStatement deleteFlightStatement = c.prepareStatement(deleteFlightQuery);
            deleteFlightStatement.setString(1, f.getId());
            deleteFlightStatement.setString(2, dateFormat.format(f.getDepartureDate()));
            deleteFlightStatement.setString(3, f.getLocation());
            int rowsAffected = deleteFlightStatement.executeUpdate();

            if (rowsAffected > 0) {
                String locationID = fetchID(f.getLocation());

                // Delete the flight from the Airport table
                String deleteAirportQuery = "DELETE FROM Airport " +
                        "WHERE FlightID = (?) and LocationID = (?)";
                PreparedStatement deleteAirportStatement = c.prepareStatement(deleteAirportQuery);
                deleteAirportStatement.setString(1, f.getId());
                deleteAirportStatement.setString(2, locationID);
                deleteAirportStatement.executeUpdate();

                System.out.println("Flight deleted successfully.");
            } else {
                System.out.println("No flight found with the provided details.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting the flight.");
            e.printStackTrace();
        }
    }

    // Returns the airport id for a given airport location
    public String fetchID(String location) throws SQLException {
        String query = "Select AirportID from AirportSolo WHERE Location=(?)";
        PreparedStatement prep = c.prepareStatement(query);
        prep.setString(1,location);
        ResultSet result = prep.executeQuery();
        return result.getString(1);
    }

    // not really used and may be deleted later.

    public String fetchTime (String flightID, String depDate) throws SQLException {
        String query = "Select DepTime from Flights WHERE FlightID = (?) AND DepDate = (?)";
        PreparedStatement prep = c.prepareStatement(query);
        prep.setString(1,flightID);
        prep.setString(2,depDate);
        ResultSet result = prep.executeQuery();
        return result.getString(1);
    }

    public void closeConnection() throws SQLException {
        c.close();
    }

    // to populate any lists that store all the possible locations and destinations within Iceland
    public ArrayList<String> getAirportNames(){
        try{
            String query = "Select DISTINCT Location from AirportSolo";
            PreparedStatement prep = c.prepareStatement(query);
            ResultSet result = prep.executeQuery();

            ArrayList<String> airportLocations = new ArrayList<>();
            while (result.next()){
                airportLocations.add(result.getString(1));
            }
            return airportLocations;

        } catch (Exception e){
            System.out.println("Error in finding all existing airports.");
        }
        return null;
    }

    public void updateSeatToTaken(Flight flight, Seat seat){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String depDate = dateFormat.format(flight.getDepartureDate());

            String query = "Update Flights set Taken=TRUE where FlightID = (?)" +
                    " AND DepDate = (?) AND Seat = (?)";
            PreparedStatement prep = c.prepareStatement(query);
            prep.setString(1,flight.getId());
            prep.setString(2, depDate);
            prep.setString(3, seat.getSeatName());

            prep.executeUpdate();

        } catch (Exception e){
            System.out.println("Error in updating seat status");
        }
    }
}
