import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    private static final String DB_URL = "jdbc:sqlite:your_database.db";  // Path to your database

    // Constructor to create the categorys table if it doesn't exist
    public CategoryDAO() {
        createCategoriesTable();
    }

    // Method to create the categorys table
    private void createCategoriesTable() {
        String sql = "CREATE TABLE IF NOT EXISTS categorys (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "description TEXT);";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.executeUpdate();  // Execute the SQL to create the table
            System.out.println("Categories table created or already exists.");

        } catch (SQLException e) {
            System.out.println("Error creating categorys table: " + e.getMessage());
        }
    }

    // Fetch all categorys from the database
    public List<Category> getAllCategories() {
        List<Category> categorys = new ArrayList<>();
        String query = "SELECT * FROM categorys";  // SQL query to get all categorys

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Category category = new Category(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description")
                );
                categorys.add(category);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching categorys: " + e.getMessage());
        }

        return categorys;
    }

    // Add a new category to the database
    public void addCategory(Category category) {
        String query = "INSERT INTO categorys (name, description) VALUES (?, ?)";

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
        String query = "UPDATE categorys SET name = ?, description = ? WHERE id = ?";

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
        String query = "DELETE FROM categorys WHERE id = ?";

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
