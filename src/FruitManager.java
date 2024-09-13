import java.util.List;

public class FruitManager {
    private FruitDAO fruitDAO;

    public FruitManager() {
        this.fruitDAO = new FruitDAO();
    }

    // Add a new fruit
    public void addFruit(Fruit fruit) {
        fruitDAO.saveFruit(fruit);
    }

    // Update an existing fruit
    public void updateFruit(Fruit fruit, String name, boolean isAvailable, String origin, int currentStock) {
        fruit.setName(name);
        fruit.setAvailable(isAvailable);
        fruit.setOrigin(origin);
        fruit.setCurrentStock(currentStock);
        fruitDAO.updateFruit(fruit);
    }

    // Get all fruits
    public List<Fruit> getFruits() {
        return fruitDAO.getFruits();
    }

    // Delete a fruit
    public void deleteFruit(Fruit fruit) {
        fruitDAO.deleteFruit(fruit.getId());
    }
}
