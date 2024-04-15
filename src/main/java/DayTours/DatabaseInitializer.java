package DayTours;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class DatabaseInitializer {

    private static final String DB_URL = "jdbc:sqlite:src/main/resources/Databases/tours.db";
    private static final String DB_PATH = "src/main/resources/Databases/tours.db";
    private static final String SCHEMA_FILE_PATH = "src/main/java/DayTours/SCHEMA.sql";
    private Connection conn;



    public static void createNewDatabase() {
        File dbFile = new File(DB_PATH);
        if (dbFile.exists()) {
            if (dbFile.delete()) {
                System.out.println("Existing database deleted.");
            } else {
                System.out.println("Failed to delete the existing database.");
                return;
            }
        }

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



    public static void initializeDatabase() {
        try {
            // Explicitly load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            List<String> statements = readSqlStatements(SCHEMA_FILE_PATH);

            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement()) {

                for (String sql : statements) {
                    stmt.execute(sql);
                }

                // Prepare and execute data for inserting tours
                List<String> tourInserts = generateTourInserts();
                for (String insertSql : tourInserts) {
                    stmt.execute(insertSql);
                }

            } catch (SQLException e) {
                System.out.println("Database initialization error: " + e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC driver not found: " + e.getMessage());
        }
    }

    private static List<String> generateTourInserts() {
        List<String> inserts = new ArrayList<>();
        LocalDateTime dateTime = LocalDateTime.now();

        // Insert details for 20 tours
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Icelandic Volcano Adventure', 'Explore active volcanoes', 24995, 'Adventure', 5, '" + dateTime.plusDays(1).toString() + "', 'Skólavörðustígur 8, Reykjavík', TRUE, FALSE, TRUE, 15)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Reykjavík City Sightseeing Tour', 'Explore the cultural and architectural highlights of Iceland’s capital.', 24995, 'Adventure', 5, '" + dateTime.plusDays(2).toString() + "', 'Skaftafell 64, Höfn', TRUE, TRUE, TRUE, 12)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Golden Circle Classic', 'Visit Geysir and Gullfoss', 14999, 'Cultural', 4, '" + dateTime.plusDays(3).toString() + "', 'Laugavegur 11, Reykjavik', TRUE, FALSE, TRUE, 20)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Northern Lights Night Tour', 'Aurora Borealis viewing', 9995, 'Nature', 5, '" + dateTime.plusDays(4).toString() + "', 'Lágmúli 9, Reykjavík', TRUE, FALSE, TRUE, 10)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Whale Watching Excursion', 'See whales in their natural habitat', 11995, 'Nature', 5, '" + dateTime.plusDays(5).toString() + "', 'Háskólabíó, Reykjavík', TRUE, FALSE, TRUE, 8)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Reykjavik City Walk', 'Discover the capitals landmarks', 5995, 'Cultural', 4, '" + dateTime.plusDays(6).toString() + "', 'Tjarnargata 11, Reykjavik', TRUE, TRUE, TRUE, 25)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Blue Lagoon Spa Day', 'Relax in geothermal waters', 9990, 'Wellness', 5, '" + dateTime.plusDays(7).toString() + "', 'BSÍ, Reykjavík', TRUE, TRUE, TRUE, 30)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Sky Lagoon Spa Day', 'Where the sea meets the sky', 8990, 'Wellness', 4, '" + dateTime.plusDays(7).toString() + "', 'Vesturvör 44, Kópavogur', TRUE, TRUE, TRUE, 30)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Helicopter Tour Over Reykjavík', 'Explore geothermal fields, lava landscapes, and historical sites near Reykjavík', 45995, 'Adventure', 5, '" + dateTime.plusDays(8).toString() + "', 'Sigtún 28, Reykjavík', TRUE, FALSE, TRUE, 8)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Sea Angling Adventure', 'Enjoy a fun sea angling trip right off the coast of Reykjavík and cook your catch on board', 7995, 'Adventure', 5, '" + dateTime.plusDays(9).toString() + "', 'Borgartún 32, Reykjavík', TRUE, TRUE, TRUE, 15)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Lava Tunnel Exploration', 'Tour of Raufarhólshellir', 18995, 'Adventure', 5, '" + dateTime.plusDays(10).toString() + "', 'Leitahraun 12, Hafnarfjordur', TRUE, TRUE, TRUE, 10)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Hiking Tour of Mount Esja', 'Hike up Reykjavík’s iconic mountain for panoramic views of the capital area', 4595, 'Nature', 4, '" + dateTime.plusDays(11).toString() + "', 'Hamrahlíð 10, Reykjavík', TRUE, FALSE, TRUE, 20)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Viking History Tour', 'Explore historical sites in Thingvellir', 13995, 'Cultural', 4, '" + dateTime.plusDays(12).toString() + "', 'Hverfisgata 8, Reykjavík', TRUE, TRUE, TRUE, 25)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Reykjavík Art Walk', 'Discover the vibrant art scene in Reykjavík with visits to galleries and stunning street art locations', 7990, 'Photography', 4, '" + dateTime.plusDays(13).toString() + "', 'Katrínartún 2, Reykjavík', TRUE, FALSE, TRUE, 12)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Kayaking Tour Near Reykjavík', 'Paddle in the calm waters around the beautiful coastal areas just outside the city', 11995, 'Nature', 5, '" + dateTime.plusDays(14).toString() + "', 'Skeifan 8, Reykjavík', TRUE, TRUE, TRUE, 6)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'South Coast Waterfalls Tour', 'Visit Seljalandsfoss and Skógafoss', 19995, 'Adventure', 5, '" + dateTime.plusDays(15).toString() + "', 'Sundlaugarvegur 105, Reykjavík', TRUE, TRUE, TRUE, 18)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Horseback Riding Through Lava Fields', 'Ride Icelandic horses', 19995, 'Adventure', 5, '" + dateTime.plusDays(16).toString() + "', 'Mosfellsbaer 5, Mosfellsbaer', TRUE, FALSE, TRUE, 15)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Geothermal Cooking Class', 'Learn to cook with geothermal heat', 15990, 'Nature', 4, '" + dateTime.plusDays(17).toString() + "', 'Skólavörðustígur 56, Reykjavík', TRUE, TRUE, TRUE, 20)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Icelandic Sagas and Folklore Tour', 'Learn about Iceland’s rich history and folklore through stories and visits to significant historical sites', 10995, 'Cultural', 4, '" + dateTime.plusDays(18).toString() + "', 'Hallgrímstog 1, Reykjavík', TRUE, FALSE, TRUE, 25)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Midnight Sun Photography Tour', 'Capture unique arctic summer scenes', 8990, 'Photography', 5, '" + dateTime.plusDays(19).toString() + "', 'Eiríksgata 4, Reykjavík', TRUE, TRUE, TRUE, 10)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Hvalfjörður Fjord Day Hike', 'Discover the serene beauty of Hvalfjörður Fjord with a scenic hike to the Glymur waterfall.', 14995, 'Adventure', 5, '" + dateTime.plusDays(20).toString() + "', 'Tryggjavagata 17, Reykjavík', TRUE, TRUE, TRUE, 8)");


        return inserts;
    }


    private static List<String> readSqlStatements(String filePath) {
        List<String> statements = new ArrayList<>();
        StringBuilder statement = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("--")) {
                    continue;
                }
                statement.append(line);
                if (line.endsWith(";")) {
                    statements.add(statement.toString());
                    statement.setLength(0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return statements;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        createNewDatabase();
        initializeDatabase();
    }

}
