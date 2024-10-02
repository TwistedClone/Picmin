import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ProductEditScreen extends Application {
    private ProductManager productManager;
    private TextField nameField;
    private FruitTable fruitTable;
    private TextField descriptionField;
    private Button saveButton;
    private Product selectedProduct;

    public ProductEditScreen(ProductManager productManager) {
        this.productManager = productManager;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Product Edit/Create Screen");

        VBox inputPanel = new VBox(10);
        inputPanel.setPadding(new Insets(10));
        inputPanel.setStyle("-fx-background-color: #f4f4f4;");

        Label nameLabel = new Label("Product Name:");
        nameField = new TextField();

        Label descriptionLabel = new Label("Product Description:");
        descriptionField = new TextField();

        saveButton = new Button("Save Product");
        saveButton.setOnAction(e -> saveProduct());

        inputPanel.getChildren().addAll(nameLabel, nameField, descriptionLabel, descriptionField, saveButton);

        Scene scene = new Scene(inputPanel, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public ProductEditScreen(ProductManager productManager, FruitTable fruitTable) {
        this.productManager = productManager;
        this.fruitTable = fruitTable;
    }


    private void saveProduct() {
        String name = nameField.getText();
        String description = descriptionField.getText();

        if (selectedProduct == null) {
            // Creating a new product
            Product newProduct = new Product(0, name, description);  // ID 0 means it's new and will be auto-generated
            productManager.addProduct(newProduct);
        } else {
            // Updating an existing product
            selectedProduct.setName(name);
            selectedProduct.setDescription(description);
            productManager.updateProduct(selectedProduct);
        }

        // Clear the fields after saving
        nameField.clear();
        descriptionField.clear();

    }

    public void editProduct(Product product) {
        selectedProduct = product;
        nameField.setText(product.getName());
        descriptionField.setText(product.getDescription());
        saveButton.setText("Update Product");

    }
}
