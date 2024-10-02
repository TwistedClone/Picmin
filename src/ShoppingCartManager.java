import java.util.List;

public class ShoppingCartManager {
    private ShoppingCartDAO shoppingCartDAO;
    private User user;

    public ShoppingCartManager(User user) {
        this.shoppingCartDAO = new ShoppingCartDAO();
        this.user = user;
    }

    // Method to add an item to the cart for the current user
    public void addItemToCart(Fruit fruit, int quantity) {
        double totalPrice = fruit.getPrice() * quantity;
        ShoppingCart cartItem = new ShoppingCart(fruit, quantity, totalPrice, user);  // Pass the current user
        shoppingCartDAO.addToCart(cartItem);
    }

    // Method to retrieve all items from the cart for the current user
    public List<ShoppingCart> getCartItems() {
        return shoppingCartDAO.getAllCartItems(user);  // Retrieve items for the current user
    }

    // Method to remove an item from the cart for the current user
    public void removeFromCart(ShoppingCart cartItem) {
        shoppingCartDAO.removeFromCart(cartItem);  // Remove the item from the cart in the database
    }



    public void clearCart() {
        shoppingCartDAO.clearCart(user);  // Clear the cart for the current user
    }
}


