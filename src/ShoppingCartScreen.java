import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class ShoppingCartScreen {

    private final User currentUser;
    private final ShoppingCartManager shoppingCartManager;
    private final FruitTable fruitTable;  // Reference to the FruitTable to refresh it later

    public ShoppingCartScreen(User currentUser, FruitTable fruitTable) {
        this.currentUser = currentUser;
        this.shoppingCartManager = new ShoppingCartManager(currentUser);
        this.fruitTable = fruitTable;  // Get a reference to the FruitTable// Pass the current user
    }

    public void show() {
        Stage stage = new Stage();
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        TableView<ShoppingCart> cartTable = new TableView<>();

        // Column for Fruit Name
        TableColumn<ShoppingCart, String> fruitNameColumn = new TableColumn<>("Fruit Name");
        fruitNameColumn.setCellValueFactory(cartItem -> new SimpleStringProperty(cartItem.getValue().getFruit().getName()));

        // Column for Fruit Location
        TableColumn<ShoppingCart, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(cartItem -> new SimpleStringProperty(cartItem.getValue().getFruit().getLocation().getName()));

        // Column for Quantity
        TableColumn<ShoppingCart, String> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(cartItem -> new SimpleStringProperty(String.valueOf(cartItem.getValue().getQuantity())));

        // Column for Total Price
        TableColumn<ShoppingCart, String> totalPriceColumn = new TableColumn<>("Total Price");
        totalPriceColumn.setCellValueFactory(cartItem -> new SimpleStringProperty(String.valueOf(cartItem.getValue().getTotalPrice())));

        // Add columns to the table
        cartTable.getColumns().addAll(fruitNameColumn, locationColumn, quantityColumn, totalPriceColumn);

        // Populate the table with cart items for the current user
        cartTable.getItems().addAll(shoppingCartManager.getCartItems());

        // Calculate the total price and total quantity
        double totalPrice = shoppingCartManager.getCartItems().stream()
                .mapToDouble(ShoppingCart::getTotalPrice)
                .sum();

        int totalQuantity = shoppingCartManager.getCartItems().stream()
                .mapToInt(ShoppingCart::getQuantity)
                .sum();


        // Display the total price and quantity
        Label totalPriceLabel = new Label("Total Price: " + totalPrice);
        Label totalQuantityLabel = new Label("Total Quantity: " + totalQuantity);

        root.getChildren().addAll(cartTable, totalPriceLabel, totalQuantityLabel);

        // Add Remove Button for removing items from the cart
        Button removeButton = new Button("Remove from Cart");
        removeButton.setOnAction(e -> {
            ShoppingCart selectedItem = cartTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                // Restore stock to the fruit
                Fruit fruit = selectedItem.getFruit();
                fruit.setCurrentStock(fruit.getCurrentStock() + selectedItem.getQuantity());

                // Update the stock in the database
                FruitDAO fruitDAO = new FruitDAO();
                fruitDAO.updateFruit(fruit);

                // Remove the item from the shopping cart
                shoppingCartManager.removeFromCart(selectedItem);

                // Update the UI
                cartTable.getItems().remove(selectedItem);

                // Recalculate totals
                double updatedTotalPrice = shoppingCartManager.getCartItems().stream()
                        .mapToDouble(ShoppingCart::getTotalPrice)
                        .sum();
                int updatedTotalQuantity = shoppingCartManager.getCartItems().stream()
                        .mapToInt(ShoppingCart::getQuantity)
                        .sum();

                totalPriceLabel.setText("Total Price: " + updatedTotalPrice);
                totalQuantityLabel.setText("Total Quantity: " + updatedTotalQuantity);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "No item selected to remove!");
            }
        });

        root.getChildren().add(removeButton);

        // Add Place Order button
        Button placeOrderButton = new Button("Place Order");
        placeOrderButton.setOnAction(e -> {

            if (cartTable.getItems().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Shopping Cart is empty");
                alert.setContentText("You cannot place an order with an empty cart.");
                alert.showAndWait();
                return;
            }

            // Clear the cart
            shoppingCartManager.clearCart();

            // Calculate the pickup date (one week from now)
            LocalDate pickupDate = LocalDate.now().plusWeeks(1);

            // Get unique locations
            List<String> locations = cartTable.getItems().stream()
                    .map(cartItem -> cartItem.getFruit().getLocation().getName())  // Get the actual location name
                    .distinct()
                    .toList();

            // Display the confirmation message
            String locationMessage = String.join(", ", locations);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Order Placed");
            alert.setHeaderText(null);
            alert.setContentText("Your order is ready for pickup on " + pickupDate + " at locations: " + locationMessage);
            alert.showAndWait();

            // Update the cart table and totals after clearing
            cartTable.getItems().clear();
            totalPriceLabel.setText("Total Price: 0.0");
            totalQuantityLabel.setText("Total Quantity: 0");
        });

        root.getChildren().add(placeOrderButton);

        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(event -> {
            // Refresh the FruitTable when the ShoppingCartScreen is closed
            fruitTable.refreshTable();
        });
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
