public class Fruit {

    private int id;
    private String name;
    private boolean available;
    private Country origin;  // Use Country object instead of String for origin
    private int currentStock;
    private Product product;
    private Location location;
    private double price;


    // Constructor without ID
    public Fruit(String fruitName, boolean isAvailable, Country origin, int currentStock, Product product, Location location, double price) {
        this.name = fruitName;
        this.available = isAvailable;
        this.origin = origin;
        this.currentStock = currentStock;
        this.product = product;
        this.location = location;
        this.price = price;
    }

    // Constructor with ID
    public Fruit(int id, String name, boolean available, Country origin, int currentStock, Product product, Location location, double price) {
        this.id = id;
        this.name = name;
        this.available = available;
        this.origin = origin;
        this.currentStock = currentStock;
        this.product = product;
        this.location = location;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Getters and setters
    public Country getOrigin() {
        return origin;
    }

    public void setOrigin(Country origin) {
        this.origin = origin;
    }

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

    public int getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
