import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class LocationDashboard {

    private LocationDAO locationDAO = new LocationDAO();  // Assuming you have a DAO for Location

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Location Dashboard");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        // Define columns for the location table
        List<ColumnDefinition<Location>> columns = List.of(
                new ColumnDefinition<>("Name", Location::getName),
                new ColumnDefinition<>("City", Location::getCity)
        );

        // Load categories into the table using the generic Table class
        List<Location> locations = locationDAO.getAllLocations();  // Retrieve categories from the database
        Table<Location> locationTable = new Table<>(columns, locations);
        locationTable.setupTable(columns, locations);  // Set up the table with categories

        // Edit Location functionality
        Button editLocationButton = new Button("Edit Location");
        editLocationButton.setOnAction(e -> {
            Location selectedLocation = locationTable.getSelectedItem();  // Get the selected location
            if (selectedLocation != null) {
                showEditLocationDialog(selectedLocation);
                locationTable.setupTable(columns, locationDAO.getAllLocations());  // Refresh table
            } else {
                showAlert(Alert.AlertType.ERROR, "No Selection", "Please select a location to edit.");
            }
        });

        Button deleteLocationButton = new Button("Delete Location");
        deleteLocationButton.setOnAction(e -> {
            Location selectedLocation = locationTable.getSelectedItem();
            if (selectedLocation != null) {
                locationDAO.deleteLocation(selectedLocation.getId());
                locationTable.setupTable(columns, locationDAO.getAllLocations());  // Refresh table
            } else {
                showAlert(Alert.AlertType.ERROR, "No Selection", "Please select a location to edit.");
            }
        });

        // Add TableView to the root and buttons
        root.getChildren().addAll(locationTable.tableView, editLocationButton, deleteLocationButton);

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showEditLocationDialog(Location location) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Edit Location: " + location.getName());

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        TextField nameField = new TextField(location.getName());
        TextArea descriptionField = new TextArea(location.getCity());

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            String newName = nameField.getText();
            String newCity = descriptionField.getText();

            location.setName(newName);
            location.setCity(newCity);

            locationDAO.updateLocation(location);  // Update location in the database
            dialogStage.close();
        });

        vbox.getChildren().addAll(new Label("Location Name:"), nameField,
                new Label("City:"), descriptionField, saveButton);

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
