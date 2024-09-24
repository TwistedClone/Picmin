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
    public void updateFruit(Fruit fruit, String name, boolean isAvailable, String origin, int currentStock, Product product) {
        fruit.setName(name);
        fruit.setAvailable(isAvailable);
        fruit.setOrigin(origin);
        fruit.setCurrentStock(currentStock);
        fruit.setProduct(product);  // Update the product
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
