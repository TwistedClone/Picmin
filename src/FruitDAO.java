import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FruitDAO {
    private static final String DB_URL = "jdbc:sqlite:your_database.db";

    // Constructor to create the fruits table if it doesn't exist
    public FruitDAO() {
        createFruitsTable();  // Call the method to create the fruits table
    }

    // Method to create the fruits table with category_id and location_id
    private void createFruitsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS fruits (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "available INTEGER NOT NULL, " +
                "origin TEXT NOT NULL, " +
                "current_stock INTEGER NOT NULL, " +
                "price REAL NOT NULL, " +  // Add price field
                "category_id INTEGER, " +
                "location_id INTEGER, " +
                "FOREIGN KEY (category_id) REFERENCES categories(id), " +
                "FOREIGN KEY (location_id) REFERENCES locations(id)" +
                ");";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
            System.out.println("Fruits table created or already exists.");
        } catch (SQLException e) {
            System.out.println("Error creating fruits table: " + e.getMessage());
        }
    }

    // Method to save a new fruit into the database
    public void saveFruit(Fruit fruit) {
        String sql = "INSERT INTO fruits(name, available, origin, current_stock, price, category_id, location_id) VALUES(?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fruit.getName());
            pstmt.setInt(2, fruit.isAvailable() ? 1 : 0);
            pstmt.setString(3, fruit.getOrigin().getCountryName());  // Use Country's getCountryName() method
            pstmt.setInt(4, fruit.getCurrentStock());
            pstmt.setDouble(5, fruit.getPrice());  // Save the price
            pstmt.setInt(6, fruit.getCategory().getId());
            pstmt.setInt(7, fruit.getLocation().getId());

            pstmt.executeUpdate();
            System.out.println("Fruit saved successfully!");

        } catch (SQLException e) {
            System.out.println("Error saving fruit: " + e.getMessage());
        }
    }

    // Method to update an existing fruit in the database
    public void updateFruit(Fruit fruit) {
        String sql = "UPDATE fruits SET name = ?, available = ?, origin = ?, current_stock = ?, price = ?, category_id = ?, location_id = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fruit.getName());
            pstmt.setInt(2, fruit.isAvailable() ? 1 : 0);
            pstmt.setString(3, fruit.getOrigin().getCountryName());  // Update origin as a string
            pstmt.setInt(4, fruit.getCurrentStock());
            pstmt.setDouble(5, fruit.getPrice());  // Update the price
            pstmt.setInt(6, fruit.getCategory().getId());  // Update the category's ID
            pstmt.setInt(7, fruit.getLocation().getId());  // Update the location's ID
            pstmt.setInt(8, fruit.getId());

            pstmt.executeUpdate();
            System.out.println("Fruit updated successfully!");

        } catch (SQLException e) {
            System.out.println("Error updating fruit: " + e.getMessage());
        }
    }
    // Method to retrieve all fruit data from the database
    public List<Fruit> getFruits() {
        List<Fruit> fruits = new ArrayList<>();
        String sql = "SELECT f.*, p.name AS category_name, p.description AS category_description, " +
                "l.name AS location_name, l.city AS location_city FROM fruits f " +
                "LEFT JOIN categories p ON f.category_id = p.id " +
                "LEFT JOIN locations l ON f.location_id = l.id";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                boolean available = rs.getInt("available") == 1;
                String originName = rs.getString("origin");
                Country origin = new Country(originName);  // Convert to Country object
                int currentStock = rs.getInt("current_stock");
                double price = rs.getDouble("price");  // Get the price

                // Get category details
                int categoryId = rs.getInt("category_id");
                String categoryName = rs.getString("category_name");
                String categoryDescription = rs.getString("category_description");
                Category category = new Category(categoryId, categoryName, categoryDescription);

                // Get location details
                int locationId = rs.getInt("location_id");
                String locationName = rs.getString("location_name");
                String locationCity = rs.getString("location_city");
                Location location = new Location(locationId, locationName, locationCity);

                fruits.add(new Fruit(id, name, available, origin, currentStock, category, location, price));  // Add price to constructor
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving fruits: " + e.getMessage());
        }

        return fruits;
    }


    // Method to delete a fruit from the database
    public void deleteFruit(int id) {
        String sql = "DELETE FROM fruits WHERE id = ?";  // SQL command to delete a fruit entry based on ID

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);  // Set the fruit ID in the SQL query
            pstmt.executeUpdate();  // Execute the SQL update to delete the fruit
            System.out.println("Fruit deleted successfully!");

        } catch (SQLException e) {
            System.out.println("Error deleting fruit: " + e.getMessage());
        }
    }
}
