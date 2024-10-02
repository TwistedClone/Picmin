import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private static final String DB_URL = "jdbc:sqlite:your_database.db";

    public UserDAO() {
        createUsersTable();
    }

    // Create the users table if it doesn't exist
    private void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL, " +
                "password TEXT NOT NULL, " +
                "role TEXT NOT NULL);";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.executeUpdate();
            System.out.println("Users table created or already exists.");
        } catch (SQLException e) {
            System.out.println("Error creating users table: " + e.getMessage());
        }
    }

    // Save a user in the database
    public void saveUser(User user) {
        String sql = "INSERT INTO users(username, password, role) VALUES(?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole().name());  // Store the enum name (e.g., "ADMIN", "USER")

            pstmt.executeUpdate();
            System.out.println("User saved successfully!");
        } catch (SQLException e) {
            System.out.println("Error saving user: " + e.getMessage());
        }
    }

    public User authenticate(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");  // Retrieve the id from the result set
                String role = rs.getString("role").toUpperCase();
                return new User(id, username, password, User.Role.valueOf(role));  // Pass id to the User object
            }

        } catch (SQLException e) {
            System.out.println("Error during authentication: " + e.getMessage());
        }
        return null;
    }

    // Fetch all users from the database
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");  // Retrieve the id from the result set
                String username = rs.getString("username");
                String password = rs.getString("password");
                String role = rs.getString("role").toUpperCase();
                users.add(new User(id, username, password, User.Role.valueOf(role)));  // Pass id to the User object
            }
        } catch (SQLException e) {
            System.out.println("Error fetching users: " + e.getMessage());
        }
        return users;
    }



    // Update the role of a user
    public void updateUserRole(String username, User.Role newRole) {
        String sql = "UPDATE users SET role = ? WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newRole.name());  // Use the enum's name for storing
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating user role: " + e.getMessage());
        }
    }

    // Find a user by their username
    public User findUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");  // Retrieve the id from the result set
                String password = rs.getString("password");
                String role = rs.getString("role").toUpperCase();
                return new User(id, username, password, User.Role.valueOf(role));  // Pass id to the User object
            }

        } catch (SQLException e) {
            System.out.println("Error finding user: " + e.getMessage());
        }
        return null;
    }

}
