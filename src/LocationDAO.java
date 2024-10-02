import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationDAO {
    private static final String DB_URL = "jdbc:sqlite:your_database.db";  // Path to your database

    // Constructor to create the locations table if it doesn't exist
    public LocationDAO() {
        createLocationsTable();
    }

    private Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;"); // Enable foreign keys
        }
        return conn;
    }

    // Method to create the locations table
    private void createLocationsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS locations (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "city TEXT);";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.executeUpdate();  // Execute the SQL to create the table
            System.out.println("Locations table created or already exists.");

        } catch (SQLException e) {
            System.out.println("Error creating locations table: " + e.getMessage());
        }
    }

    // Fetch all locations from the database
    public List<Location> getAllLocations() {
        List<Location> locations = new ArrayList<>();
        String query = "SELECT * FROM locations";  // SQL query to get all locations

        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String city = rs.getString("city");

                Location location = new Location(id, name, city);
                locations.add(location);

                System.out.println("Fetched location: " + id + " " + name + " " + city);  // Debug output
            }

        } catch (SQLException e) {
            System.out.println("Error fetching locations: " + e.getMessage());
        }

        return locations;
    }


    // Add a new location to the database
    public void addLocation(Location location) {
        String query = "INSERT INTO locations (name, city) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, location.getName());
            pstmt.setString(2, location.getCity());
            pstmt.executeUpdate();

            System.out.println("Location added successfully!");

        } catch (SQLException e) {
            System.out.println("Error adding location: " + e.getMessage());
        }
    }

    // Update an existing location
    public void updateLocation(Location location) {
        String query = "UPDATE locations SET name = ?, city = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, location.getName());
            pstmt.setString(2, location.getCity());
            pstmt.setInt(3, location.getId());
            pstmt.executeUpdate();

            System.out.println("Location updated successfully!");

        } catch (SQLException e) {
            System.out.println("Error updating location: " + e.getMessage());
        }
    }

    // Delete a location by ID
    public void deleteLocation(int locationId) {
        String query = "DELETE FROM locations WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, locationId);
            pstmt.executeUpdate();

            System.out.println("Location deleted successfully!");

        } catch (SQLException e) {
            System.out.println("Error deleting location: " + e.getMessage());
        }
    }
}
