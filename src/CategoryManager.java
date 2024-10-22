import java.util.List;

public class CategoryManager {
    private CategoryDAO categoryDAO;

    public CategoryManager() {
        this.categoryDAO = new CategoryDAO();
    }

    // Add a new category
    public void addCategory(Category category) {
        categoryDAO.addCategory(category);
    }

    // Update an existing category
    public void updateCategory(Category category) {
        categoryDAO.updateCategory(category);
    }

    // Get all categories
    public List<Category> getCategories() {
        return categoryDAO.getAllCategories();
    }

    // Delete a category
    public void deleteCategory(int categoryId) {
        categoryDAO.deleteCategory(categoryId);
    }
}
