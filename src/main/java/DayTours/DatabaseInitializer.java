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
        LocalDateTime dateTime = LocalDateTime.now(); // Starting point for tour dates

        // Insert details for 20 tours
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Icelandic Volcano Adventure', 'Explore active volcanoes', 300, 'Adventure', 5, '" + dateTime.plusDays(1).toString() + "', 'Vikurbraut 75, Vik', TRUE, FALSE, TRUE, 15)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Glacier Hike and Ice Cave Tour', 'Trek across Vatnajokull', 250, 'Adventure', 5, '" + dateTime.plusDays(2).toString() + "', 'Skaftafell 64, Höfn', TRUE, TRUE, TRUE, 12)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Golden Circle Classic', 'Visit Geysir and Gullfoss', 150, 'Cultural', 4, '" + dateTime.plusDays(3).toString() + "', 'Laugavegur 11, Reykjavik', TRUE, FALSE, TRUE, 20)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Northern Lights Night Tour', 'Aurora Borealis viewing', 180, 'Nature', 5, '" + dateTime.plusDays(4).toString() + "', 'Akureyri 5, Akureyri', TRUE, FALSE, TRUE, 10)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Whale Watching Excursion', 'See whales in their natural habitat', 220, 'Nature', 5, '" + dateTime.plusDays(5).toString() + "', 'Hafnarstræti 102, Husavik', TRUE, FALSE, TRUE, 8)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Reykjavik City Walk', 'Discover the capitals landmarks', 100, 'Cultural', 4, '" + dateTime.plusDays(6).toString() + "', 'Tjarnargata 11, Reykjavik', TRUE, TRUE, TRUE, 25)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Blue Lagoon Spa Day', 'Relax in geothermal waters', 280, 'Wellness', 5, '" + dateTime.plusDays(7).toString() + "', 'Nordurljosavegur 9, Grindavik', TRUE, TRUE, TRUE, 30)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Westfjords Wilderness', 'Untouched landscapes tour', 350, 'Adventure', 5, '" + dateTime.plusDays(8).toString() + "', 'Isafjordur 23, Isafjordur', TRUE, FALSE, TRUE, 8)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Snæfellsnes Peninsula Journey', 'Explore coastal villages and lava fields', 200, 'Adventure', 5, '" + dateTime.plusDays(9).toString() + "', 'Hellissandur 2, Snæfellsnes', TRUE, TRUE, TRUE, 15)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Lava Tunnel Exploration', 'Tour of Raufarhólshellir', 180, 'Adventure', 5, '" + dateTime.plusDays(10).toString() + "', 'Leitahraun 12, Hafnarfjordur', TRUE, TRUE, TRUE, 10)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Puffin Watching on Vestmannaeyjar', 'Boat tour around islands', 165, 'Nature', 4, '" + dateTime.plusDays(11).toString() + "', 'Heimaey 7, Vestmannaeyjar', TRUE, FALSE, TRUE, 20)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Viking History Tour', 'Explore historical sites in Thingvellir', 130, 'Cultural', 4, '" + dateTime.plusDays(12).toString() + "', 'Thingvellir 1, Thingvellir', TRUE, TRUE, TRUE, 25)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Ice Fishing in the North', 'Fish on frozen lakes', 150, 'Nature', 4, '" + dateTime.plusDays(13).toString() + "', 'Myvatn 6, Myvatn', TRUE, FALSE, TRUE, 12)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Reindeer Safari in East Iceland', 'Wildlife tour in remote areas', 270, 'Nature', 5, '" + dateTime.plusDays(14).toString() + "', 'Egilsstadir 4, Egilsstadir', TRUE, TRUE, TRUE, 6)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'South Coast Waterfalls Tour', 'Visit Seljalandsfoss and Skógafoss', 200, 'Adventure', 5, '" + dateTime.plusDays(15).toString() + "', 'Skogar 9, Skogar', TRUE, TRUE, TRUE, 18)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Horseback Riding Through Lava Fields', 'Ride Icelandic horses', 190, 'Adventure', 5, '" + dateTime.plusDays(16).toString() + "', 'Mosfellsbaer 5, Mosfellsbaer', TRUE, FALSE, TRUE, 15)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Geothermal Cooking Class', 'Learn to cook with geothermal heat', 110, 'Cultural', 4, '" + dateTime.plusDays(17).toString() + "', 'Geysir 8, Haukadalur', TRUE, TRUE, TRUE, 20)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Black Sand Beach Tour', 'Visit Reynisfjara and surroundings', 150, 'Nature', 4, '" + dateTime.plusDays(18).toString() + "', 'Vik 3, Vik', TRUE, FALSE, TRUE, 25)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Midnight Sun Photography Tour', 'Capture unique arctic summer scenes', 210, 'Photography', 5, '" + dateTime.plusDays(19).toString() + "', 'Akranes 2, Akranes', TRUE, TRUE, TRUE, 10)");
        inserts.add("INSERT INTO Tours (tourID, name, description, price, category, rating, timedateTour, tourAddress, familyFriendly, wheelchairAccesible, availability, capacity) VALUES ('" + UUID.randomUUID().toString() + "', 'Fjord Kayaking Experience', 'Kayak in stunning fjords', 220, 'Adventure', 5, '" + dateTime.plusDays(20).toString() + "', 'Fjordur 15, Westfjords', TRUE, TRUE, TRUE, 8)");

        // Insert additional 17 tours similarly
        // ... Add more here using a loop or manual entries

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
