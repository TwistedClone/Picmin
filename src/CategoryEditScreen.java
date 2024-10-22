import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class CategoryEditScreen extends Application {
    private CategoryManager categoryManager;
    private TextField nameField;
    private FruitTable fruitTable;
    private TextField descriptionField;
    private Button saveButton;
    private Category selectedCategory;

    public CategoryEditScreen(CategoryManager categoryManager) {
        this.categoryManager = categoryManager;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Category Edit/Create Screen");

        VBox inputPanel = new VBox(10);
        inputPanel.setPadding(new Insets(10));
        inputPanel.setStyle("-fx-background-color: #f4f4f4;");

        Label nameLabel = new Label("Category Name:");
        nameField = new TextField();

        Label descriptionLabel = new Label("Category Description:");
        descriptionField = new TextField();

        saveButton = new Button("Save Category");
        saveButton.setOnAction(e -> saveCategory());

        inputPanel.getChildren().addAll(nameLabel, nameField, descriptionLabel, descriptionField, saveButton);

        Scene scene = new Scene(inputPanel, 400, 200);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public CategoryEditScreen(CategoryManager categoryManager, FruitTable fruitTable) {
        this.categoryManager = categoryManager;
        this.fruitTable = fruitTable;
    }


    private void saveCategory() {
        String name = nameField.getText();
        String description = descriptionField.getText();

        if (selectedCategory == null) {
            // Creating a new category
            Category newCategory = new Category(0, name, description);  // ID 0 means it's new and will be auto-generated
            categoryManager.addCategory(newCategory);
        } else {
            // Updating an existing category
            selectedCategory.setName(name);
            selectedCategory.setDescription(description);
            categoryManager.updateCategory(selectedCategory);
        }

        // Clear the fields after saving
        nameField.clear();
        descriptionField.clear();

    }

    public void editCategory(Category category) {
        selectedCategory = category;
        nameField.setText(category.getName());
        descriptionField.setText(category.getDescription());
        saveButton.setText("Update Category");

    }
}
