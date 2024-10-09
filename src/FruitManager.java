import java.util.List;

public class FruitManager {
    private FruitDAO fruitDAO;

    public FruitManager() {
        this.fruitDAO = new FruitDAO();
    }

    // Voeg een nieuwe fruit toe
    public void addFruit(Fruit fruit) {
        fruitDAO.saveFruit(fruit);
    }

    // Bewerk een bestaande fruit
    public void updateFruit(Fruit fruit, String name, boolean isAvailable, String origin, int currentStock, Category category, Location location) {
        fruit.setName(name);
        fruit.setAvailable(isAvailable);
        Country country = new Country(origin);  // Assuming the String is the name of the country
        fruit.setOrigin(country);
        fruit.setCurrentStock(currentStock);
        fruit.setCategory(category);  // Update the category
        fruit.setLocation(location);
        fruitDAO.updateFruit(fruit);
    }

    // Haal alle fruit op (get)
    public List<Fruit> getFruits() {
        return fruitDAO.getFruits();
    }

    // Verwijder een fruit (delete)
    public void deleteFruit(Fruit fruit) {
        fruitDAO.deleteFruit(fruit.getId());
    }
}
