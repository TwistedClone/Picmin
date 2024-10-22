import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    private static final String DB_URL = "jdbc:sqlite:your_database.db";  // Path to your database

    // Constructor to create the categories table if it doesn't exist
    public CategoryDAO() {
        createCategoriesTable();
    }

    // Method to create the categories table
    private void createCategoriesTable() {
        String sql = "CREATE TABLE IF NOT EXISTS categories (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "description TEXT);";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.executeUpdate();  // Execute the SQL to create the table
            System.out.println("Categories table created or already exists.");

        } catch (SQLException e) {
            System.out.println("Error creating categories table: " + e.getMessage());
        }
    }

    // Fetch all categories from the database
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM categories";  // SQL query to get all categories

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Category category = new Category(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description")
                );
                categories.add(category);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching categories: " + e.getMessage());
        }

        return categories;
    }

    // Add a new category to the database
    public void addCategory(Category category) {
        String query = "INSERT INTO categories (name, description) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, category.getName());
            pstmt.setString(2, category.getDescription());
            pstmt.executeUpdate();

            System.out.println("Category added successfully!");

        } catch (SQLException e) {
            System.out.println("Error adding category: " + e.getMessage());
        }
    }

    // Update an existing category
    public void updateCategory(Category category) {
        String query = "UPDATE categories SET name = ?, description = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, category.getName());
            pstmt.setString(2, category.getDescription());
            pstmt.setInt(3, category.getId());
            pstmt.executeUpdate();

            System.out.println("Category updated successfully!");

        } catch (SQLException e) {
            System.out.println("Error updating category: " + e.getMessage());
        }
    }

    // Delete a category by ID
    public void deleteCategory(int categoryId) {
        String query = "DELETE FROM categories WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, categoryId);
            pstmt.executeUpdate();

            System.out.println("Category deleted successfully!");

        } catch (SQLException e) {
            System.out.println("Error deleting category: " + e.getMessage());
        }
    }
}
