public class ShoppingCart {

    private int id;
    private Fruit fruit;
    private int quantity;
    private double totalPrice;
    private User user;  // Associate the shopping cart with a User

    // Constructor
    public ShoppingCart(Fruit fruit, int quantity, double totalPrice, User user) {
        this.fruit = fruit;
        this.quantity = quantity;
        this.totalPrice = fruit.getPrice() * quantity;
        this.user = user;  // Set the user
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public Fruit getFruit() {
        return fruit;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public User getUser() {
        return user;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalPrice = this.fruit.getPrice() * quantity;
    }
}
