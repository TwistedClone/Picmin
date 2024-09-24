import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class Table extends Application {

    private static FruitManager fruitManager;
    private static ProductManager productManager;  // Use ProductManager instead of ProductDAO
    private static VBox fruitPanel;
    private static TextField nameField;
    private static ComboBox<String> countryComboBox;
    private static ComboBox<Product> productComboBox;  // ComboBox for Product selection
    private static TextField currentStockField;
    private static Button addButton;
    private static Button deleteButton;  // New delete button
    private static ScrollPane scrollPane;

    private static TextField searchField;
    private static Button logoutButton;
    private static ToggleButton languageToggle;

    private static Fruit selectedFruit = null;
    private static boolean isEnglish = true;
    private static String currentUserRole;  // Store the role of the logged-in user

    // Track the currently selected panel to un-highlight the previous one
    private static HBox selectedPanel = null;

    @Override
    public void start(Stage primaryStage) {
        // Example: starting with the User role. Modify this based on the LoginPage logic.
        showTable(primaryStage, "User");  // Default to User role for now
    }

    // Method to show the table based on the user's role
    public void showTable(Stage primaryStage, String role) {
        currentUserRole = role;  // Store the user's role
        setupStage(primaryStage);
    }

    private void setupStage(Stage primaryStage) {
        fruitManager = new FruitManager();
        productManager = new ProductManager();  // Initialize ProductManager

        primaryStage.setTitle(getTitleForRole());

        BorderPane root = new BorderPane();
        VBox inputPanel = new VBox(10);
        inputPanel.setPadding(new Insets(10));
        inputPanel.setSpacing(10);
        inputPanel.setStyle("-fx-background-color: #f4f4f4;");

        // Logout Button
        logoutButton = new Button(isEnglish ? "Logout" : "Uitloggen");
        logoutButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;");
        logoutButton.setOnAction(e -> {
            primaryStage.close();  // Close the table screen
            showLoginPage();       // Return to the login page
        });

        // Show admin/medewerker controls
        if (isAdminOrMedewerker()) {
            addAdminOrMedewerkerControls(inputPanel);
        }



        fruitPanel = new VBox(10);
        fruitPanel.setPadding(new Insets(10));

        scrollPane = new ScrollPane(fruitPanel);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #ffffff;");

        // Load fruit data and add to panel
        addTableHeader();
        loadFruits();

        searchField = new TextField();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isEmpty()) {
                loadFruits();  // Show all fruits if the search field is empty
            } else {
                loadFruitsWithSearch(newValue.trim().toLowerCase());
            }
        });

        languageToggle = new ToggleButton(isEnglish ? "Switch to Dutch" : "Wissel naar Engels");
        languageToggle.setOnAction(e -> {
            isEnglish = !isEnglish;
            primaryStage.close();
            showTable(new Stage(), currentUserRole);  // Reload based on the current role
        });

        HBox searchPanel = new HBox(10, languageToggle, searchField, logoutButton );  // Add logout button here
        searchPanel.setPadding(new Insets(10));

        if (isAdmin()) {
            Button adminDashboardButton = new Button(isEnglish ? "Admin Dashboard" : "Admin Dashboard");
            adminDashboardButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
            adminDashboardButton.setOnAction(e -> openAdminDashboard());
            searchPanel.getChildren().add(adminDashboardButton);  // Add button to the searchPanel
        }

        if (isAdminOrMedewerker()) {
            Button manageProductsButton = new Button(isEnglish ? "Create a Product" : "Maak een Product");
            manageProductsButton.setStyle("-fx-background-color: #5cb85c; -fx-text-fill: white;");
            manageProductsButton.setOnAction(e -> openProductEditScreen());  // Redirect to ProductEditScreen
            searchPanel.getChildren().add(manageProductsButton);
        }

        root.setTop(inputPanel);
        root.setCenter(scrollPane);
        root.setBottom(searchPanel);

        Scene scene = new Scene(root, 800, 1000);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Add a button for Admin Dashboard


    private void openAdminDashboard() {
        Stage adminStage = new Stage();
        // Pass the User object with the correct role type (User.Role)
        AdminDashboard adminDashboard = new AdminDashboard(new User("admin", "adminpass", User.Role.ADMIN));
        adminDashboard.start(adminStage);
    }

    // Show controls for Admin/Medewerker roles
    private void addAdminOrMedewerkerControls(VBox inputPanel) {
        Label nameLabel = new Label(isEnglish ? "Fruit Name:" : "Fruit Naam:");
        nameField = new TextField();

        // Country Dropdown
        Label originLabel = new Label(isEnglish ? "Country of Origin:" : "Land van herkomst:");
        countryComboBox = new ComboBox<>();
        countryComboBox.getItems().addAll(Country.getAllCountries());
        countryComboBox.setPromptText(isEnglish ? "Select a country" : "Selecteer een land");

        // Product Dropdown
        Label productLabel = new Label(isEnglish ? "Product:" : "Product:");
        productComboBox = new ComboBox<>();
        productComboBox.getItems().addAll(productManager.getProducts());  // Populate with products
        productComboBox.setPromptText(isEnglish ? "Select a product" : "Selecteer een product");

        Label currentStockLabel = new Label(isEnglish ? "Current Stock:" : "Huidige voorraad:");
        currentStockField = new TextField();

        addButton = new Button(isEnglish ? "Add Fruit" : "Fruit Toevoegen");
        addButton.setStyle("-fx-background-color: #5cb85c; -fx-text-fill: white;");
        addButton.setOnAction(e -> addOrUpdateFruit());

        // Create the delete button (initially hidden)
        deleteButton = new Button(isEnglish ? "Delete Fruit" : "Verwijder Fruit");
        deleteButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;");
        deleteButton.setVisible(false);  // Initially hidden
        deleteButton.setOnAction(e -> deleteSelectedFruit());

        inputPanel.getChildren().addAll(nameLabel, nameField, originLabel, countryComboBox, productLabel, productComboBox, currentStockLabel, currentStockField, addButton, deleteButton);
    }

    private String getTitleForRole() {
        if (isAdminOrMedewerker()) {
            return isEnglish ? "Admin/Medewerker - Fruit Table" : "Admin/Medewerker - Fruittafel";
        } else {
            return isEnglish ? "User - Fruit Table" : "Gebruiker - Fruittafel";
        }
    }

    private boolean isAdminOrMedewerker() {
        return "Admin".equalsIgnoreCase(currentUserRole) || "Medewerker".equalsIgnoreCase(currentUserRole);
    }

    private boolean isAdmin() {
        return "Admin".equalsIgnoreCase(currentUserRole);
    }

    // Load all fruits and display them
    private void loadFruits() {
        fruitPanel.getChildren().clear();  // Clear the previous fruit rows
        addTableHeader();  // Add the header again
        List<Fruit> fruits = fruitManager.getFruits();  // Fetch fruits from the manager

        for (Fruit fruit : fruits) {
            addFruitToPanel(fruit);  // Add each fruit to the panel
        }
    }

    // Load fruits that match the search term
    private void loadFruitsWithSearch(String searchTerm) {
        fruitPanel.getChildren().clear();  // Clear the previous fruit rows
        addTableHeader();  // Add the header again

        // Fetch fruits from the manager
        List<Fruit> fruits = fruitManager.getFruits();

        // Filter the fruits that match the search term (name or origin)
        List<Fruit> filteredFruits = fruits.stream()
                .filter(fruit -> fruit.getName().toLowerCase().contains(searchTerm) ||
                        fruit.getOrigin().toLowerCase().contains(searchTerm))
                .toList();

        // Add the filtered fruits to the panel
        for (Fruit fruit : filteredFruits) {
            addFruitToPanel(fruit);
        }

        if (filteredFruits.isEmpty()) {
            Label noResultsLabel = new Label(isEnglish ? "No fruits found" : "Geen vruchten gevonden");
            fruitPanel.getChildren().add(noResultsLabel);  // Show a message if no fruits are found
        }
    }

    private void addTableHeader() {
        HBox headerPanel = new HBox();
        headerPanel.setPadding(new Insets(10));
        headerPanel.setStyle("-fx-background-color: #e0e0e0;");

        Label nameHeader = new Label(isEnglish ? "Name" : "Naam");
        nameHeader.setPrefWidth(100);
        Label availableHeader = new Label(isEnglish ? "Available" : "Beschikbaar");
        availableHeader.setPrefWidth(100);
        Label originHeader = new Label(isEnglish ? "Origin" : "Herkomst");
        originHeader.setPrefWidth(100);
        Label stockHeader = new Label(isEnglish ? "Stock" : "Voorraad");
        stockHeader.setPrefWidth(100);
        Label productHeader = new Label(isEnglish ? "Product" : "Product");
        productHeader.setPrefWidth(100);  // Add a new header for Product

        headerPanel.getChildren().addAll(nameHeader, availableHeader, originHeader, stockHeader, productHeader);
        fruitPanel.getChildren().add(headerPanel);  // Add the header to the fruit panel
    }

    private void addFruitToPanel(Fruit fruit) {
        HBox fruitEntryPanel = new HBox(10);
        fruitEntryPanel.setPadding(new Insets(5));
        fruitEntryPanel.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #cccccc;");



        Label nameLabel = new Label(fruit.getName());
        nameLabel.setPrefWidth(100);
        Label availableLabel = new Label(fruit.isAvailable() ? (isEnglish ? "Yes" : "Ja") : (isEnglish ? "No" : "Nee"));
        availableLabel.setPrefWidth(100);
        if (fruit.isAvailable()) {
            availableLabel.setStyle("-fx-text-fill: green;");
        } else {
            availableLabel.setStyle("-fx-text-fill: red;");
        }

        Label originLabel = new Label(fruit.getOrigin());
        originLabel.setPrefWidth(100);
        Label stockLabel = new Label(String.valueOf(fruit.getCurrentStock()));
        stockLabel.setPrefWidth(100);
        Label productLabel = new Label(fruit.getProduct().getName());  // Display the product name
        productLabel.setPrefWidth(100);

        // Add action to select this fruit for editing and show delete button
        fruitEntryPanel.setOnMouseClicked(event -> {
            if (selectedPanel != null) {
                selectedPanel.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #cccccc;");
            }

            // Mark the clicked fruit entry as selected
            selectedPanel = fruitEntryPanel;
            selectedPanel.setStyle("-fx-background-color: #d3d3d3; -fx-border-color: #cccccc;");

            selectedFruit = fruit;
            nameField.setText(fruit.getName());
            countryComboBox.setValue(fruit.getOrigin());
            productComboBox.setValue(fruit.getProduct());  // Set the ComboBox value to the fruit's product
            currentStockField.setText(String.valueOf(fruit.getCurrentStock()));
            addButton.setText(isEnglish ? "Update Fruit" : "Fruit Bijwerken");

            deleteButton.setVisible(true);  // Show the delete button when a fruit is selected
        });

        fruitEntryPanel.getChildren().addAll(nameLabel, availableLabel, originLabel, stockLabel, productLabel);
        fruitPanel.getChildren().add(fruitEntryPanel);  // Add the fruit row to the fruit panel
    }

    private void addOrUpdateFruit() {
        // Add or update fruit depending on whether a fruit is selected for updating
        String name = nameField.getText();
        String origin = countryComboBox.getValue();  // Get the selected country from the ComboBox
        Product product = productComboBox.getValue();  // Get the selected product from the ComboBox
        int currentStock;

        try {
            currentStock = Integer.parseInt(currentStockField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, isEnglish ? "Invalid Input" : "Ongeldige invoer", isEnglish ? "Please enter a valid stock number." : "Voer een geldig voorraadnummer in.");
            return;
        }

        boolean isAvailable = currentStock > 0;

        if (selectedFruit == null) {
            // Adding a new fruit
            Fruit fruit = new Fruit(name, isAvailable, origin, currentStock, product);  // Include the product
            fruitManager.addFruit(fruit);  // Add to the database
        } else {
            // Updating an existing fruit
            fruitManager.updateFruit(selectedFruit, name, isAvailable, origin, currentStock, product);
            selectedFruit = null;
            addButton.setText(isEnglish ? "Add Fruit" : "Fruit Toevoegen");
        }

        resetFields();
        loadFruits();  // Refresh the fruit list
    }

    private void deleteSelectedFruit() {
        if (selectedFruit != null) {
            fruitManager.deleteFruit(selectedFruit);  // Call the delete method in FruitManager
            selectedFruit = null;
            resetFields();
            loadFruits();  // Refresh the list after deletion
            deleteButton.setVisible(false);  // Hide the delete button after deletion
        }
    }

    private void resetFields() {
        nameField.clear();
        countryComboBox.setValue(null);  // Clear the selected value in ComboBox
        productComboBox.setValue(null);  // Clear the selected product
        currentStockField.clear();
        selectedFruit = null;
        deleteButton.setVisible(false);  // Hide the delete button when no fruit is selected
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showLoginPage() {
        // Go back to login page
        Stage loginStage = new Stage();
        new LoginPage().start(loginStage);
    }

    private void openProductEditScreen() {
        Stage productStage = new Stage();
        ProductEditScreen productEditScreen = new ProductEditScreen(productManager);
        productEditScreen.start(productStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
