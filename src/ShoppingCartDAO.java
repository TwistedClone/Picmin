import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartDAO {

    private static final String DB_URL = "jdbc:sqlite:your_database.db";

    public ShoppingCartDAO() {
        createShoppingCartTable();
    }

    private void createShoppingCartTable() {
        String sql = "CREATE TABLE IF NOT EXISTS shopping_cart (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "fruit_id INTEGER NOT NULL, " +
                "quantity INTEGER NOT NULL, " +
                "total_price REAL NOT NULL, " +
                "user_id INTEGER NOT NULL, " +  // Add user_id to associate with User
                "FOREIGN KEY(fruit_id) REFERENCES fruits(id), " +
                "FOREIGN KEY(user_id) REFERENCES users(id)" +
                ");";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
            System.out.println("Shopping Cart table created.");
        } catch (SQLException e) {
            System.out.println("Error creating shopping cart table: " + e.getMessage());
        }
    }

    // Method to add an item to the shopping cart for a specific user
    public void addToCart(ShoppingCart cartItem) {
        String sql = "INSERT INTO shopping_cart(fruit_id, quantity, total_price, user_id) VALUES(?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, cartItem.getFruit().getId());  // Use the ID of the existing fruit
            pstmt.setInt(2, cartItem.getQuantity());
            pstmt.setDouble(3, cartItem.getTotalPrice());
            pstmt.setInt(4, cartItem.getUser().getId());  // Use the user's ID

            pstmt.executeUpdate();
            System.out.println("Item added to shopping cart.");
        } catch (SQLException e) {
            System.out.println("Error adding item to cart: " + e.getMessage());
        }
    }

    // Method to retrieve all shopping cart items for the current user
    public List<ShoppingCart> getAllCartItems(User user) {
        List<ShoppingCart> cartItems = new ArrayList<>();
        String sql = "SELECT sc.id, sc.quantity, sc.total_price, " +
                "f.id AS fruit_id, f.name AS fruit_name, f.price AS fruit_price, f.available, " +
                "f.origin, f.current_stock, p.id AS product_id, p.name AS product_name, " +
                "l.id AS location_id, l.name AS location_name " +
                "FROM shopping_cart sc " +
                "JOIN fruits f ON sc.fruit_id = f.id " +
                "JOIN products p ON f.product_id = p.id " +
                "JOIN locations l ON f.location_id = l.id " +
                "WHERE sc.user_id = ?";  // Query only for the current user's cart

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, user.getId());  // Get cart items for the current user
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int quantity = rs.getInt("quantity");
                double totalPrice = rs.getDouble("total_price");

                // Get Fruit details from the result set
                int fruitId = rs.getInt("fruit_id");
                String fruitName = rs.getString("fruit_name");
                double fruitPrice = rs.getDouble("fruit_price");
                boolean available = rs.getInt("available") == 1;
                String originName = rs.getString("origin");
                int currentStock = rs.getInt("current_stock");

                // Get Product and Location details
                int productId = rs.getInt("product_id");
                String productName = rs.getString("product_name");
                Product product = new Product(productId, productName, ""); // Assuming you have a Product constructor

                int locationId = rs.getInt("location_id");
                String locationName = rs.getString("location_name");
                Location location = new Location(locationId, locationName, "");

                // Create a Fruit object
                Country origin = new Country(originName);
                Fruit fruit = new Fruit(fruitId, fruitName, available, origin, currentStock, product, location, fruitPrice);

                // Create a ShoppingCart object
                ShoppingCart cartItem = new ShoppingCart(fruit, quantity, totalPrice, user);
                cartItems.add(cartItem);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving cart items: " + e.getMessage());
        }

        return cartItems;
    }

    public void removeFromCart(ShoppingCart cartItem) {
        String sql = "DELETE FROM shopping_cart WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);  // Assuming you have a method to get a DB connection
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, cartItem.getId());  // Assuming the ShoppingCart has an id field
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Optionally, add a method to clear the cart for the current user
    public void clearCart(User user) {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, user.getId());  // Clear only the cart of the current user
            pstmt.executeUpdate();
            System.out.println("Cart cleared for user!");

        } catch (SQLException e) {
            System.out.println("Error clearing cart: " + e.getMessage());
        }
    }
}
