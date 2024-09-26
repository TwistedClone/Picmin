import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class AdminDashboard {

    private User currentUser;  // Store the entire User object, not just the role
    private UserDAO userDAO = new UserDAO();

    public AdminDashboard(User currentUser) {
        this.currentUser = currentUser;  // Accept the full User object
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Admin Dashboard");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        // Define columns for the user table
        List<ColumnDefinition<User>> columns = List.of(
                new ColumnDefinition<>("Username", User::getUsername),
                new ColumnDefinition<>("Role", user -> user.getRole().name())  // Display role as a string
        );

        // Load users into the table using the generic Table class
        List<User> users = userDAO.getAllUsers();
        Table<User> userTable = new Table<>(columns, users);
        userTable.setupTable(columns, users);  // Set up the table with users

        // Role change functionality
        Button changeRoleButton = new Button("Change Role");
        changeRoleButton.setOnAction(e -> {
            User selectedUser = userTable.getSelectedItem();  // Get the selected user
            if (selectedUser != null) {
                showRoleChangeDialog(selectedUser);
                userTable.setupTable(columns, userDAO.getAllUsers());  // Refresh table
            } else {
                showAlert(Alert.AlertType.ERROR, "No Selection", "Please select a user to change the role.");
            }
        });

        // Add TableView to the root and buttons
        root.getChildren().addAll(userTable.tableView, changeRoleButton);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showRoleChangeDialog(User user) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Change Role for " + user.getUsername());

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        ComboBox<User.Role> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll(User.Role.USER, User.Role.MEDEWERKER, User.Role.ADMIN);  // Allow Admin role
        roleComboBox.setValue(user.getRole());

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            User.Role newRole = roleComboBox.getValue();

            // Prevent the current admin from removing their own admin role
            if (user.getUsername().equals(currentUser.getUsername()) && newRole == User.Role.USER) {
                showAlert(Alert.AlertType.ERROR, "Error", "You cannot remove your own Admin role.");
            } else {
                userDAO.updateUserRole(user.getUsername(), newRole);
                dialogStage.close();
            }
        });

        vbox.getChildren().addAll(new Label("Select new role:"), roleComboBox, saveButton);

        Scene scene = new Scene(vbox, 200, 150);
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
