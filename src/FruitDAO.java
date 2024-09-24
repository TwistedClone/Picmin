import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FruitDAO {
    private static final String DB_URL = "jdbc:sqlite:your_database.db";

    // Constructor om de fruitentabel te maken als deze nog niet bestaat
    public FruitDAO() {
        createFruitsTable();  // Roep de methode aan om de fruits tabel te maken
    }

    // Methode om de fruits tabel te maken
    private void createFruitsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS fruits (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +  // Uniek ID voor elke fruitinvoer
                "name TEXT NOT NULL, " +  // Naam van het fruit
                "available INTEGER NOT NULL, " +  // Beschikbaarheid
                "origin TEXT NOT NULL, " +  // Land van herkomst
                "current_stock INTEGER NOT NULL, " +  // Huidige voorraad
                "product_id INTEGER, " +  // Added foreign key for product
                "FOREIGN KEY (product_id) REFERENCES products(id));";  // Foreign key constraint to the products table

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
            System.out.println("Fruits table created or already exists.");
        } catch (SQLException e) {
            System.out.println("Error creating fruits table: " + e.getMessage());
        }
    }

    // Methode om een nieuw fruit op te slaan in de database
    public void saveFruit(Fruit fruit) {
        String sql = "INSERT INTO fruits(name, available, origin, current_stock, product_id) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fruit.getName());
            pstmt.setInt(2, fruit.isAvailable() ? 1 : 0);
            pstmt.setString(3, fruit.getOrigin());
            pstmt.setInt(4, fruit.getCurrentStock());
            pstmt.setInt(5, fruit.getProduct().getId());  // Save the product's ID

            pstmt.executeUpdate();
            System.out.println("Fruit saved successfully!");

        } catch (SQLException e) {
            System.out.println("Error saving fruit: " + e.getMessage());
        }
    }


    // Methode om een bestaand fruit in de database bij te werken
    public void updateFruit(Fruit fruit) {
        String sql = "UPDATE fruits SET name = ?, available = ?, origin = ?, current_stock = ?, product_id = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fruit.getName());
            pstmt.setInt(2, fruit.isAvailable() ? 1 : 0);
            pstmt.setString(3, fruit.getOrigin());
            pstmt.setInt(4, fruit.getCurrentStock());
            pstmt.setInt(5, fruit.getProduct().getId());  // Update the product's ID
            pstmt.setInt(6, fruit.getId());

            pstmt.executeUpdate();
            System.out.println("Fruit updated successfully!");

        } catch (SQLException e) {
            System.out.println("Error updating fruit: " + e.getMessage());
        }
    }


    // Methode om alle fruitgegevens uit de database op te halen
    public List<Fruit> getFruits() {
        List<Fruit> fruits = new ArrayList<>();
        String sql = "SELECT f.*, p.name AS product_name, p.description AS product_description FROM fruits f " +
                "LEFT JOIN products p ON f.product_id = p.id";  // Join fruits with products

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                boolean available = rs.getInt("available") == 1;
                String origin = rs.getString("origin");
                int currentStock = rs.getInt("current_stock");

                // Get product details
                int productId = rs.getInt("product_id");
                String productName = rs.getString("product_name");
                String productDescription = rs.getString("product_description");
                Product product = new Product(productId, productName, productDescription);

                fruits.add(new Fruit(id, name, available, origin, currentStock, product));
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving fruits: " + e.getMessage());
        }

        return fruits;
    }


    // Methode om een fruit uit de database te verwijderen
    public void deleteFruit(int id) {
        String sql = "DELETE FROM fruits WHERE id = ?";  // SQL-commando om een fruitinvoer te verwijderen op basis van het ID

        // Maak verbinding met de database en verwijder het fruit met het opgegeven ID
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);  // Zet het fruit-ID in de SQL-query
            pstmt.executeUpdate();  // Voer de SQL-update uit om het fruit te verwijderen
            System.out.println("Fruit deleted successfully!");  // Bevestigingsbericht dat het fruit is verwijderd

        } catch (SQLException e) {
            System.out.println("Error deleting fruit: " + e.getMessage());  // Foutmelding bij het verwijderen van fruit
        }
    }
}
