import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:fruits.db";

    // Initialize the database and create the tables
    public static void initializeDatabase() {
        try {
            // Load the SQLite driver manually
            Class.forName("org.sqlite.JDBC");

            try (Connection conn = connect();  // Connect to the database
                 Statement stmt = conn.createStatement()) {
                if (conn != null) {
                    // Create the Fruits table with foreign key references to categorys and locations
                    String createFruitsTableSQL = "CREATE TABLE IF NOT EXISTS Fruits ("
                            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"  // Auto-incrementing id field
                            + "name TEXT NOT NULL,"
                            + "available INTEGER NOT NULL,"
                            + "origin TEXT NOT NULL,"
                            + "stock INTEGER NOT NULL,"
                            + "category_id INTEGER,"  // Foreign key for categorys
                            + "location_id INTEGER,"  // Foreign key for locations
                            + "FOREIGN KEY (category_id) REFERENCES categorys(id),"
                            + "FOREIGN KEY (location_id) REFERENCES locations(id)"
                            + ");";
                    stmt.execute(createFruitsTableSQL);  // Create the Fruits Table
                    System.out.println("Fruits table has been initialized.");

                    // Optionally, create the Categories table if it doesn't already exist
                    String createCategoriesTableSQL = "CREATE TABLE IF NOT EXISTS categorys ("
                            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "name TEXT NOT NULL, "
                            + "description TEXT"
                            + ");";
                    stmt.execute(createCategoriesTableSQL);  // Create the Categories Table
                    System.out.println("Categories table has been initialized.");

                    // Optionally, create the Locations table if it doesn't already exist
                    String createLocationsTableSQL = "CREATE TABLE IF NOT EXISTS locations ("
                            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "name TEXT NOT NULL, "
                            + "city TEXT"
                            + ");";
                    stmt.execute(createLocationsTableSQL);  // Create the Locations Table
                    System.out.println("Locations table has been initialized.");
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Establish a connection with the database
    public static Connection connect() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        try (Statement stmt = conn.createStatement()) {
            // Enable foreign key enforcement
            stmt.execute("PRAGMA foreign_keys = ON;");
        } catch (SQLException e) {
            System.out.println("Error enabling foreign keys: " + e.getMessage());
        }
        return conn;
    }
}
