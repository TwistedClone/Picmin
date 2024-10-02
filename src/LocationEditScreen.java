import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LocationEditScreen extends Application {
    private LocationManager locationManager;
    private TextField nameField;
    private FruitTable fruitTable;
    private TextField cityField;
    private Button saveButton;
    private Location selectedLocation;

    public LocationEditScreen(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Location Edit/Create Screen");

        VBox inputPanel = new VBox(10);
        inputPanel.setPadding(new Insets(10));
        inputPanel.setStyle("-fx-background-color: #f4f4f4;");

        Label nameLabel = new Label("Location Name:");
        nameField = new TextField();

        Label cityLabel = new Label("City:");
        cityField = new TextField();

        saveButton = new Button("Save Location");
        saveButton.setOnAction(e -> saveLocation());

        inputPanel.getChildren().addAll(nameLabel, nameField, cityLabel, cityField, saveButton);

        Scene scene = new Scene(inputPanel, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public LocationEditScreen(LocationManager locationManager, FruitTable fruitTable) {
        this.locationManager = locationManager;
        this.fruitTable = fruitTable;
    }

    private void saveLocation() {
        String name = nameField.getText();
        String city = cityField.getText();

        if (selectedLocation == null) {
            // Creating a new location
            Location newLocation = new Location(0, name, city);  // ID 0 means it's new and will be auto-generated
            locationManager.addLocation(newLocation);
        } else {
            // Updating an existing location
            selectedLocation.setName(name);
            selectedLocation.setCity(city);
            locationManager.updateLocation(selectedLocation);
        }

        // Clear the fields after saving
        nameField.clear();
        cityField.clear();

        // Optionally, refresh any relevant UI components like ComboBoxes
    }

    public void editLocation(Location location) {
        selectedLocation = location;
        nameField.setText(location.getName());
        cityField.setText(location.getCity());
        saveButton.setText("Update Location");
    }
}
