import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:fruits.db";

    // Initialize the database and create the table
    public static void initializeDatabase() {
        try {
            // Manually load the SQLite driver
            Class.forName("org.sqlite.JDBC");

            try (Connection conn = connect();
                 Statement stmt = conn.createStatement()) {
                if (conn != null) {
                    String createTableSQL = "CREATE TABLE IF NOT EXISTS Fruits ("
                            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"  // Auto-incrementing id field
                            + "name TEXT NOT NULL,"
                            + "available INTEGER NOT NULL,"
                            + "origin TEXT NOT NULL,"
                            + "stock INTEGER NOT NULL"
                            + ");";
                    stmt.execute(createTableSQL);  // Create the Fruits table
                    System.out.println("Database has been initialized.");
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Method to connect to the database
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
