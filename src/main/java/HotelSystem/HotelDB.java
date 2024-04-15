package HotelSystem;

import EngineStuff.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HotelDB {

    public Hotel select(String hotelId) {
        String sql = "SELECT * FROM Hotels WHERE hotelId = ?";
        Hotel hotel = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, hotelId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    List<Room> rooms = new ArrayList<>();
                    List<Reservation> reservations = new ArrayList<>();

                    String fetchedHotelId = resultSet.getString("hotelId");
                    String name = resultSet.getString("name");
                    String locationId = resultSet.getString("locationId");
                    double rating = resultSet.getDouble("rating");

                    // TODO: Add a method to get the Location by ID or Name
                    Location location = getLocationById(locationId);

                    hotel = new Hotel(name, location, rating, rooms, reservations, UUID.fromString(fetchedHotelId));
                }
            }
        } catch (SQLException e) {
            System.err.println("SQLException occurred while retrieving hotel details: " + e.getMessage());
        }
        return hotel;
    }
    private Location getLocationById(String locationId) {
        String sql = "SELECT * FROM Locations WHERE locationId = ?";
        Location location = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, locationId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    float latitude = resultSet.getFloat("latitude");
                    float longitude = resultSet.getFloat("longitude");
                    String name = resultSet.getString("name");
                    location = new Location(latitude, longitude, name);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving location by ID: " + e.getMessage());
        }
        return location;
    }


    public List<Hotel> searchHotels(String query) {
        List<Hotel> hotels = new ArrayList<>();
        String sql = "SELECT * FROM Hotels WHERE LOWER(name) LIKE ? OR LOWER(location) LIKE ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + query.toLowerCase() + "%");
            statement.setString(2, "%" + query.toLowerCase() + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                UUID hotelId = UUID.fromString(resultSet.getString("hotelId"));  // Correctly retrieving UUID from the database
                String name = resultSet.getString("name");
                String locationStr = resultSet.getString("location");
                double rating = resultSet.getDouble("rating");
                List<Room> rooms = new ArrayList<>();
                List<Reservation> reservations = new ArrayList<>();

                // Assuming you have a method to convert a location string to a Location object
                Location location = new Location(0,0,locationStr); // Update this part as necessary if location needs more details

                hotels.add(new Hotel(name, location, rating, rooms, reservations, hotelId));
            }
        } catch (SQLException e) {
            System.err.println("SQLException occurred while searching for hotels: " + e.getMessage());
        }
        return hotels;
    }
    public List<Hotel> selectAll() {
        List<Hotel> hotels = new ArrayList<>();
        String sql = "SELECT * FROM Hotels";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                List<Room> rooms = new ArrayList<>();
                List<Reservation> reservations = new ArrayList<>();

                UUID hotelId = UUID.fromString(resultSet.getString("hotelId")); // Correctly retrieving UUID from the database
                String name = resultSet.getString("name");
                String locationId = resultSet.getString("locationId"); // Correct column name
                double rating = resultSet.getDouble("rating");

                // Fetch location details from the Locations table based on locationId
                Location location = getLocationById(locationId);

                hotels.add(new Hotel(name, location, rating, rooms, reservations, hotelId));
            }
        } catch (SQLException e) {
            System.err.println("SQLException occurred while retrieving hotels: " + e.getMessage());
        }
        return hotels;
    }
    public List<String> getUniqueHotelLocations() {
        List<String> locations = new ArrayList<>();
        String sql = "SELECT DISTINCT name FROM Locations";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                locations.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving unique hotel locations: " + e.getMessage());
            // Optionally, handle the exception more gracefully or throw it to be handled elsewhere
        }
        return locations;
    }

    public List<Hotel> getHotelsByLocation(String locationName) {
        List<Hotel> hotels = new ArrayList<>();
        String sql = "SELECT * FROM Hotels WHERE locationId IN (SELECT locationId FROM Locations WHERE name = ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, locationName);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    UUID hotelId = UUID.fromString(resultSet.getString("hotelId"));
                    String name = resultSet.getString("name");
                    double rating = resultSet.getDouble("rating");
                    List<Room> rooms = new ArrayList<>();
                    List<Reservation> reservations = new ArrayList<>();

                    // Assuming you have a method to convert a location string to a Location object
                    Location location = new Location(0, 0, locationName); // Update this part as necessary if location needs more details

                    hotels.add(new Hotel(name, location, rating, rooms, reservations, hotelId));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching hotels by location: " + e.getMessage());
        }
        return hotels;
    }


    private String getLocationIdByName(String locationName) {
        String sql = "SELECT locationId FROM Locations WHERE name = ?";
        String locationId = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, locationName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    locationId = resultSet.getString("locationId");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving location ID by name: " + e.getMessage());
        }
        return locationId;
    }

}
