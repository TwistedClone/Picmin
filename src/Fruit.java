public class Fruit {
    private int id;  // Unique ID for each fruit (used for database operations)
    private String name;
    private boolean available;
    private String origin;
    private int current_stock;

    // Constructor without id (used when creating a new fruit)
    public Fruit(String fruitName, boolean isAvailable, String countryOfOrigin, int currentStock) {
        this.name = fruitName;
        this.available = isAvailable;
        this.origin = countryOfOrigin;
        this.current_stock = currentStock;
    }

    // Constructor with id (used when retrieving from the database)
    public Fruit(int id, String fruitName, boolean isAvailable, String countryOfOrigin, int currentStock) {
        this.id = id;
        this.name = fruitName;
        this.available = isAvailable;
        this.origin = countryOfOrigin;
        this.current_stock = currentStock;
    }

    // Getters and setters for all fields
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

    // Display fruit details (for UI)
    public String displayFruit() {
        return String.format("%-15s %-15s %-15s %-20s", this.name, this.available ? "Yes" : "No", this.origin, this.current_stock);
    }
}
