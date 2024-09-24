import java.util.List;

public class ProductManager {
    private ProductDAO productDAO;

    public ProductManager() {
        this.productDAO = new ProductDAO();
    }

    // Add a new product
    public void addProduct(Product product) {
        productDAO.addProduct(product);
    }

    // Update an existing product
    public void updateProduct(Product product) {
        productDAO.updateProduct(product);
    }

    // Get all products
    public List<Product> getProducts() {
        return productDAO.getAllProducts();
    }

    // Delete a product
    public void deleteProduct(int productId) {
        productDAO.deleteProduct(productId);
    }
}
