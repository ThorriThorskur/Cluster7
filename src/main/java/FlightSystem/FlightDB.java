package FlightSystem;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

public class FlightDB {
    final String [] seats = {"1A","1B","1C","1D",
            "2A","2B","2C","2D",
            "3A","3B","3C","3D",
            "4A","4B","4C","4D",
            "5A","5B","5C","5D",
            "6A","6B","6C","6D",
            "7A","7B","7C","7D",
            "8A","8B","8C","8D",
            "9A","9B","9C","9D",
            "10A","10B","10C","10D",
            "11A","11B","11C","11D",
            "12A","12B","12C","12D",
            "13A","13B","13C","13D",
            "14A","14B","14C","14D",
            "15A","15B","15C","15D",
            "16A","16B","16C","16D",
            "17A","17B","17C","17D",
            "18A","18B","18C","18D",
            "19A","19B","19C","19D",
            "20A","20B","20C","20D"};

    /* Where all queries about finding flights, updating flights and adding flights will reside.*/

    static Connection c;

    private SeatDB seatDB;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public static Map<String, float[]> coordinates;

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

    public void create(String flightID, String location, String destination, String depDate, String depTime, String arrDate, String arrTime) throws SQLException {
        System.out.println(flightID + " " + location + " " + destination + " " + depDate + " " +
                depTime + " " + arrDate + " " + arrTime);

        String locationID = fetchID(location);
        String destinationID = fetchID(destination);
        int count = seats.length;
        boolean value = false;
        try {
            for (int i = 0; i < count; i++) {
                String statement = "INSERT INTO Flights VALUES ((?),(?),(?),(?),(?),(?),(?))";
                PreparedStatement insert = c.prepareStatement(statement);
                insert.setString(1, flightID);
                insert.setString(2, seats[i]);
                insert.setBoolean(3, value);
                insert.setString(4, depDate);
                insert.setString(5, depTime);
                insert.setString(6, arrDate);
                insert.setString(7, arrTime);
                insert.executeUpdate();
            }

            String airportAssign = "INSERT INTO Airport VALUES ((?),(?),(?),(?))";

            //Add the flight as a departure from airport
            PreparedStatement locationInsert = c.prepareStatement(airportAssign);
            locationInsert.setString(1, locationID);
            locationInsert.setString(2, location);
            locationInsert.setString(3, flightID);
            locationInsert.setString(4, "Departure");
            locationInsert.executeUpdate();

            //Add the flight as an arrival to an airport
            PreparedStatement destinationInsert = c.prepareStatement(airportAssign);
            destinationInsert.setString(1, destinationID);
            destinationInsert.setString(2, destination);
            destinationInsert.setString(3, flightID);
            destinationInsert.setString(4, "Arrival");
            destinationInsert.executeUpdate();
        } catch (Exception e){
            System.out.println("Error adding the flight, check Primary Keys.");
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
            updateStatement.setString(6, f.getName());
            updateStatement.setString(7, depDate);
            updateStatement.setString(8, f.getName());
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
            deleteFlightStatement.setString(1, f.getName());
            deleteFlightStatement.setString(2, dateFormat.format(f.getDepartureDate()));
            deleteFlightStatement.setString(3, f.getLocation().getName());
            int rowsAffected = deleteFlightStatement.executeUpdate();

            if (rowsAffected > 0) {
                String locationID = fetchID(f.getLocation().getName());

                // Delete the flight from the Airport table
                String deleteAirportQuery = "DELETE FROM Airport " +
                        "WHERE FlightID = (?) and LocationID = (?)";
                PreparedStatement deleteAirportStatement = c.prepareStatement(deleteAirportQuery);
                deleteAirportStatement.setString(1, f.getName());
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
            prep.setString(1,flight.getName());
            prep.setString(2, depDate);
            prep.setString(3, seat.getSeatName());

            prep.executeUpdate();

        } catch (Exception e){
            System.out.println("Error in updating seat status");
        }
    }

    public void updateSeatToFree(Flight flight, Seat seat) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String depDate = dateFormat.format(flight.getDepartureDate());

            String query = "Update Flights set Taken=FALSE where FlightID = (?)" +
                    " AND DepDate = (?) AND Seat = (?)";
            PreparedStatement prep = c.prepareStatement(query);
            prep.setString(1,flight.getName());
            prep.setString(2, depDate);
            prep.setString(3, seat.getSeatName());

            prep.executeUpdate();

        } catch (Exception e){
            System.out.println("Error in updating seat status");
        }
    }
}
