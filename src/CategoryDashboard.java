import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class CategoryDashboard {

    private CategoryDAO categoryDAO = new CategoryDAO();  // Assuming you have a DAO for Category

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Category Dashboard");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        // Define columns for the category table
        List<ColumnDefinition<Category>> columns = List.of(
                new ColumnDefinition<>("Name", Category::getName),
                new ColumnDefinition<>("Description", Category::getDescription)
        );

        // Load categories into the table using the generic Table class
        List<Category> categories = categoryDAO.getAllCategories();  // Retrieve categories from the database
        Table<Category> categoryTable = new Table<>(columns, categories);
        categoryTable.setupTable(columns, categories);  // Set up the table with categories

        // Edit Category functionality
        Button editCategoryButton = new Button("Edit Category");
        editCategoryButton.setOnAction(e -> {
            Category selectedCategory = categoryTable.getSelectedItem();  // Get the selected category
            if (selectedCategory != null) {
                showEditCategoryDialog(selectedCategory);
                categoryTable.setupTable(columns, categoryDAO.getAllCategories());  // Refresh table
            } else {
                showAlert(Alert.AlertType.ERROR, "No Selection", "Please select a category to edit.");
            }
        });

        Button deleteCategoryButton = new Button("Delete Category");
        deleteCategoryButton.setOnAction(e -> {
            Category selectedCategory = categoryTable.getSelectedItem();
            if (selectedCategory != null) {
                categoryDAO.deleteCategory(selectedCategory.getId());
                categoryTable.setupTable(columns, categoryDAO.getAllCategories());  // Refresh table
            } else {
                showAlert(Alert.AlertType.ERROR, "No Selection", "Please select a category to edit.");
            }
        });

        // Add TableView to the root and buttons
        root.getChildren().addAll(categoryTable.tableView, editCategoryButton, deleteCategoryButton);

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showEditCategoryDialog(Category category) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Edit Category: " + category.getName());

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        TextField nameField = new TextField(category.getName());
        TextArea descriptionField = new TextArea(category.getDescription());

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            String newName = nameField.getText();
            String newDescription = descriptionField.getText();

            category.setName(newName);
            category.setDescription(newDescription);

            categoryDAO.updateCategory(category);  // Update category in the database
            dialogStage.close();
        });

        vbox.getChildren().addAll(new Label("Category Name:"), nameField,
                new Label("Description:"), descriptionField, saveButton);

        Scene scene = new Scene(vbox, 300, 200);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
