public class Fruit {
    private String name;
    private boolean available;
    private String origin;
    
    public Fruit(String fruitName, boolean isAvailable, String countryOfOrigin) {
        this.name = fruitName;
        this.available = isAvailable;
        this.origin = countryOfOrigin;
    }

    public static Fruit createFruit(String fruitName, boolean isAvailable, String countryOfOrigin) {
        return new Fruit(fruitName, isAvailable, countryOfOrigin);
    }

    public String displayFruit() {
        return String.format("%-20s %-20s %-20s", this.name, this.available ? "Yes" : "No", this.origin);
    }

    public String getName() {
        return name;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getOrigin() {
        return origin;
    }
}
