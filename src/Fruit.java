public class Fruit {

    private int id;  // Unieke ID voor elke vrucht (gebruikt voor database)
    private String name;
    private boolean available;
    private String origin;
    private int current_stock;
    private Product product;  // Added field for associated Product

    // Constructor zonder ID (Word gebruikt voor creatie van de vrucht)
    public Fruit(String fruitName, boolean isAvailable, String countryOfOrigin, int currentStock, Product product) {
        this.name = fruitName;
        this.available = isAvailable;
        this.origin = countryOfOrigin;
        this.current_stock = currentStock;
        this.product = product;  // Set the associated Product
    }

    // Constructor met ID (Opzoeken in database)
    public Fruit(int id, String name, boolean available, String origin, int currentStock, Product product) {
        this.id = id;
        this.name = name;
        this.available = available;
        this.origin = origin;
        this.current_stock = currentStock;
        this.product = product;
    }

    // Getters en setters voor alle velden
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public int getCurrentStock() {
        return current_stock;
    }

    public void setCurrentStock(int current_stock) {
        this.current_stock = current_stock;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    // Display fruit details (voor de UI)
    public String displayFruit() {
        return String.format("%-15s %-15s %-15s %-20s %-20s", this.name, this.available ? "Yes" : "No", this.origin, this.current_stock, this.product.getName());
    }
}
