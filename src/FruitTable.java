import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class FruitTable extends BaseView {

    private final List<Fruit> fruits;
    private final User currentUser;
    private ShoppingCartManager shoppingCartManager; // Manager for shopping cart functionality

    public Table<Fruit> fruitTable;  // Store the Table instance so we can update its data later

    private TextField nameField;
    private TextField stockField;
    private TextField quantityField;  // Field to input quantity
    private TextField priceField;
    private Button addButton;
    private Button deleteButton;
    private Button addToCartButton;  // Add to cart button
    public ComboBox<Category> categoryComboBox;
    public ComboBox<Location> locationComboBox;
    private Fruit selectedFruit = null;  // Track the selected fruit for updating
    private ComboBox<Country> originComboBox;

    public FruitTable(Stage stage, List<Fruit> fruits, User currentUser) {
        super(stage);
        this.fruits = fruits;
        this.currentUser = currentUser;
        this.shoppingCartManager = new ShoppingCartManager(currentUser);  // Initialize the shopping cart manager
    }
    @Override
    public void show() {
        // Define columns for the fruit table
        List<ColumnDefinition<Fruit>> columns = new ArrayList<>(List.of(
                new ColumnDefinition<>("Name", Fruit::getName),
                new ColumnDefinition<>("Available", fruit -> fruit.getCurrentStock() > 0 ? "Yes" : "No"),
                new ColumnDefinition<>("Origin", fruit -> fruit.getOrigin().getCountryName()),
                new ColumnDefinition<>("Stock", fruit -> String.valueOf(fruit.getCurrentStock())),
                new ColumnDefinition<>("Category", fruit -> fruit.getCategory().getName()),
                new ColumnDefinition<>("Location", fruit -> fruit.getLocation().getName()),
                new ColumnDefinition<>("Price/Qty", fruit -> String.valueOf(fruit.getPrice()))
        ));

        root.setPadding(new Insets(10));

        ImageView logoView = new ImageView();
        Image logo = new Image(getClass().getResourceAsStream("/picit.png"));
        logoView.setImage(logo);
        logoView.setFitHeight(50);  // Adjust size as necessary
        logoView.setFitWidth(50);   // Adjust size as necessary

        // Shopping cart and add to cart buttons
        Button shoppingCartButton = new Button("Shopping Cart");
        shoppingCartButton.setOnAction(e -> openShoppingCart());

        quantityField = new TextField();
        quantityField.setPromptText("Quantity");

        addToCartButton = new Button("Add to Cart");
        addToCartButton.setOnAction(e -> addToCart());

        // Layout the shopping cart button in a horizontal box
        HBox topPanel = new HBox(10, logoView ,shoppingCartButton, quantityField, addToCartButton);
        root.getChildren().add(topPanel);

        // Create the fruit table using the generic Table class
        fruitTable = new Table<>(columns, fruits);
        fruitTable.setupTable(columns, fruits);  // Apply initial filter

        // Add listeners to update the table on search input
        TextField searchField = new TextField();
        searchField.setPromptText("Search");
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<Fruit> filteredFruits = filterFruits(newValue);
            fruitTable.setupTable(columns, filteredFruits);
        });

        root.getChildren().add(searchField);
        root.getChildren().add(fruitTable.tableView);  // Add the TableView to the root layout

        // Only show add/edit/delete buttons to Admin and Medewerker
        if (currentUser.getRole() != User.Role.USER) {
            createFruitForm();  // Create the form for adding/updating fruits

            // Add buttons for editing categories and locations
            Button categoryEditButton = new Button("Add Category");
            categoryEditButton.setOnAction(e -> openCategoryEditScreen());


            Button logoutButton = new Button("Logout");
            logoutButton.setOnAction(e -> handleLogout());

            if (currentUser.getRole() == User.Role.ADMIN) {

                Button adminDashboardButton = new Button("Admin Dashboard");
                adminDashboardButton.setOnAction(e -> openAdminDashboard());

                Button categoryDashboardButton = new Button("Category Dashboard");
                categoryDashboardButton.setOnAction(e -> openCategoryDashboard());

                Button locationDashboardButton = new Button("Location Dashboard");
                locationDashboardButton.setOnAction(e -> openLocationDashboard());

                HBox modelEditPanel = new HBox(10, categoryDashboardButton, adminDashboardButton, locationDashboardButton);
                modelEditPanel.setStyle("-fx-padding: 20px 0 0 0; -fx-alignment: bottom-center;");
                root.getChildren().add(modelEditPanel);

            }

            HBox buttonPanel = new HBox(10, categoryEditButton, logoutButton);
            buttonPanel.setStyle("-fx-padding: 20px 0 0 0; -fx-alignment: bottom-center;");
            root.getChildren().add(buttonPanel);

        } else {
            // For users, only add the logout button
            Button logoutButton = new Button("Logout");
            logoutButton.setOnAction(e -> handleLogout());

            HBox buttonPanel = new HBox(10, logoutButton);
            root.getChildren().add(buttonPanel);
        }

        // Add a listener to capture the selected fruit in the table and prepopulate the form
        fruitTable.tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedFruit = newValue;  // Update selectedFruit when a new item is selected
                populateFruitForm(selectedFruit);  // Prepopulate the form with selected fruit's data
                incrementInterest(selectedFruit);  // Increment interest
                deleteButton.setDisable(false);
            }
        });

        Scene scene = new Scene(root, 1200, 700);
        setScene(scene);
        stage.show();
    }

    private Map<Fruit, Integer> interestMap = new HashMap<>();

    private void incrementInterest(Fruit fruit) {
        int currentInterest = interestMap.getOrDefault(fruit, 0);
        interestMap.put(fruit, currentInterest + 1);
    }

    private void openAdminDashboard() {
        AdminDashboard adminDashboard = new AdminDashboard(currentUser);
        Stage adminStage = new Stage();
        adminDashboard.start(adminStage);  // Show the admin dashboard in a new stage
    }

    private void openCategoryDashboard() {
        CategoryDashboard categoryDashboard = new CategoryDashboard();
        Stage adminStage = new Stage();
        categoryDashboard.start(adminStage);  // Show the admin dashboard in a new stage
    }

    private void openLocationDashboard() {
        LocationDashboard locationDashboard = new LocationDashboard();
        Stage adminStage = new Stage();
        locationDashboard.start(adminStage);  // Show the admin dashboard in a new stage
    }

    // Create input form for adding/updating fruits and for adding to the shopping cart
    private void createFruitForm() {
        nameField = new TextField();
        nameField.setPromptText("Name");

        originComboBox = new ComboBox<>();
        originComboBox.getItems().addAll(Country.getAllCountries());  // Populate ComboBox with Country objects
        originComboBox.setPromptText("Origin");

        stockField = new TextField();
        stockField.setPromptText("Stock");

        priceField = new TextField();  // Add a price field
        priceField.setPromptText("Price");  // Prompt for price

        categoryComboBox = new ComboBox<>();
        CategoryManager categoryManager = new CategoryManager();
        categoryComboBox.getItems().addAll(categoryManager.getCategories());

        locationComboBox = new ComboBox<>();
        LocationManager locationManager = new LocationManager();
        locationComboBox.getItems().addAll(locationManager.getLocations());

        addButton = new Button("Add Fruit");
        FruitDAO fruitDAO = new FruitDAO();  // Use FruitDAO
        addButton.setOnAction(e -> addOrUpdateFruit());

        deleteButton = new Button("Delete Fruit");
        deleteButton.setOnAction(e -> deleteSelectedFruit());
        deleteButton.setDisable(true);

        HBox formPanel = new HBox(10, nameField, originComboBox, stockField, priceField, categoryComboBox, locationComboBox, addButton, deleteButton);
        root.getChildren().add(formPanel);
    }

    public void refreshTable() {
        // Reload the fruits list from the database
        FruitDAO fruitDAO = new FruitDAO();
        List<Fruit> updatedFruits = fruitDAO.getFruits();  // Retrieve the updated list of fruits

        // Update the table data with the new fruit list
        fruitTable.setupTable(fruitTable.getColumns(), updatedFruits);
    }

    private void addToCart() {
        // Check if a fruit is selected in the table
        if (selectedFruit == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No fruit selected!");
            return;
        }

        // Parse the quantity from the input field
        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Quantity", "Please enter a valid quantity.");
            return;
        }

        // Ensure the quantity is valid
        if (quantity <= 0) {
            showAlert(Alert.AlertType.ERROR, "Invalid Quantity", "Quantity must be greater than zero.");
            return;
        }

        // Ensure there's enough stock available
        if (quantity > selectedFruit.getCurrentStock()) {
            showAlert(Alert.AlertType.ERROR, "Insufficient Stock", "Not enough stock available.");
            return;
        }

        // Subtract the selected quantity from the fruit's stock locally
        selectedFruit.setCurrentStock(selectedFruit.getCurrentStock() - quantity);

        // Update the stock in the database
        FruitDAO fruitDAO = new FruitDAO();  // Create an instance of the DAO
        fruitDAO.updateFruit(selectedFruit);  // Update the stock in the database

        // Add selected fruit to the shopping cart
        shoppingCartManager.addItemToCart(selectedFruit, quantity);

        // Display a success message
        showAlert(Alert.AlertType.INFORMATION, "Success", "Fruit added to cart!");

        // Refresh the table to reflect updated stock
        refreshTable();

        // Clear the form to prepare for the next action
        clearForm();
    }

    // Open the shopping cart screen
    private void openShoppingCart() {
        ShoppingCartScreen shoppingCartScreen = new ShoppingCartScreen(currentUser, this);
        shoppingCartScreen.show();
    }

    private void openCategoryEditScreen() {
        Stage categoryEditStage = new Stage();
        CategoryManager categoryManager = new CategoryManager();
        CategoryEditScreen categoryEditScreen = new CategoryEditScreen(categoryManager, this);
        categoryEditScreen.start(categoryEditStage);
    }


    // Method to update the selected fruit in the form for editing
    private void populateFruitForm(Fruit fruit) {
        nameField.setText(fruit.getName());
        originComboBox.setValue(fruit.getOrigin());
        stockField.setText(String.valueOf(fruit.getCurrentStock()));
        priceField.setText(String.valueOf(fruit.getPrice()));  // Set price
        quantityField.setText("");  // Clear the quantity as it's for the shopping cart
        categoryComboBox.setValue(fruit.getCategory());
        locationComboBox.setValue(fruit.getLocation());
        addButton.setText("Update Fruit");
    }

    // Add or update a fruit depending on the form state
    private void addOrUpdateFruit() {
        String name = nameField.getText();
        Country origin = originComboBox.getValue();
        int stock;
        double price;

        try {
            stock = Integer.parseInt(stockField.getText());
            price = Double.parseDouble(priceField.getText());  // Parse price from price field
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid numbers for stock and price.");
            return;
        }

        Category category = categoryComboBox.getValue();
        Location location = locationComboBox.getValue();

        FruitDAO fruitDAO = new FruitDAO();

        if (selectedFruit == null) {
            Fruit fruit = new Fruit(name, stock > 0, origin, stock, category, location, price);  // Use Country for origin and add price
            fruitDAO.saveFruit(fruit);  // Save new fruit to the database
            fruits.add(fruit);
        } else {
            selectedFruit.setName(name);
            selectedFruit.setAvailable(stock > 0);
            selectedFruit.setOrigin(origin);
            selectedFruit.setCurrentStock(stock);
            selectedFruit.setCategory(category);
            selectedFruit.setLocation(location);
            selectedFruit.setPrice(price);

            fruitDAO.updateFruit(selectedFruit);
        }

        fruitTable.setupTable(fruitTable.getColumns(), fruits);
        clearForm();
    }

    // Clear the form fields
    private void clearForm() {
        nameField.clear();
        originComboBox.setValue(null);
        stockField.clear();
        quantityField.clear();
        categoryComboBox.setValue(null);
        priceField.clear();
        selectedFruit = null;
        addButton.setText("Add Fruit");
    }

    // Delete the selected fruit
    private void deleteSelectedFruit() {
        if (selectedFruit != null) {
            fruits.remove(selectedFruit);
            fruitTable.setupTable(fruitTable.getColumns(), fruits);
            clearForm();
        }
    }


    // Filter fruits based on search term
    private List<Fruit> filterFruits(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return fruits;
        }
        String lowerCaseTerm = searchTerm.toLowerCase();
        return fruits.stream()
                .filter(fruit -> fruit.getName().toLowerCase().contains(lowerCaseTerm) ||
                        fruit.getOrigin().getCountryName().toLowerCase().contains(lowerCaseTerm) ||
                        fruit.getCategory().getName().toLowerCase().contains(lowerCaseTerm) ||
                        fruit.getLocation().getName().toLowerCase().contains(lowerCaseTerm))
                .toList();
    }

}
