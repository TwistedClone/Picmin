import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private static final String DB_URL = "jdbc:sqlite:your_database.db";  // Path to your database

    // Constructor to create the products table if it doesn't exist
    public ProductDAO() {
        createProductsTable();
    }

    // Method to create the products table
    private void createProductsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS products (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "description TEXT);";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.executeUpdate();  // Execute the SQL to create the table
            System.out.println("Products table created or already exists.");

        } catch (SQLException e) {
            System.out.println("Error creating products table: " + e.getMessage());
        }
    }

    // Fetch all products from the database
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products";  // SQL query to get all products

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description")
                );
                products.add(product);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching products: " + e.getMessage());
        }

        return products;
    }

    // Add a new product to the database
    public void addProduct(Product product) {
        String query = "INSERT INTO products (name, description) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.executeUpdate();

            System.out.println("Product added successfully!");

        } catch (SQLException e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
    }

    // Update an existing product
    public void updateProduct(Product product) {
        String query = "UPDATE products SET name = ?, description = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setInt(3, product.getId());
            pstmt.executeUpdate();

            System.out.println("Product updated successfully!");

        } catch (SQLException e) {
            System.out.println("Error updating product: " + e.getMessage());
        }
    }

    // Delete a product by ID
    public void deleteProduct(int productId) {
        String query = "DELETE FROM products WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, productId);
            pstmt.executeUpdate();

            System.out.println("Product deleted successfully!");

        } catch (SQLException e) {
            System.out.println("Error deleting product: " + e.getMessage());
        }
    }
}
