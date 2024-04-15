package HotelSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection() {
        try {
            // Load the JDBC driver for SQLite
            Class.forName("org.sqlite.JDBC");

            // Specify the path to your database file
            String url = "jdbc:sqlite:src/main/resources/Databases/hotelsystem.db"; // Adjust the path as necessary

            // Establish the connection to the database
            return DriverManager.getConnection(url);
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Failed to establish a connection: " + e.getMessage());
        }
        return null;
    }
}
