import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginPage extends Application {

    private static UserDAO userDAO = new UserDAO();  // DAO to interact with the database

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login & Register");

        // Layout for the login form
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(10);
        grid.setVgap(10);

        // Login form components
        Label userLabel = new Label("Username:");
        TextField userTextField = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");

        // Add components to grid layout
        grid.add(userLabel, 0, 0);
        grid.add(userTextField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(loginButton, 1, 2);
        grid.add(registerButton, 1, 3);

        loginButton.setOnAction(e -> {
            String username = userTextField.getText();
            String password = passwordField.getText();

            // Authenticate user with the database
            User user = userDAO.authenticate(username, password);
            if (user != null) {
                // Close the login stage
                primaryStage.close();

                // Redirect based on the user's role and show the FruitTable
                Stage tableStage = new Stage();
                FruitDAO fruitDAO = new FruitDAO();  // Create a fruit DAO
                FruitTable fruitTable = new FruitTable(tableStage, fruitDAO.getFruits(), user);  // Pass the current user to the FruitTable
                fruitTable.show();  // Show the fruit table after successful login
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid login credentials.");
            }
        });

        // Register action
        registerButton.setOnAction(e -> {
            primaryStage.close();  // Close the login stage
            showRegistrationForm();  // Show the registration form
        });

        // Set the scene and show the login stage
        Scene scene = new Scene(grid, 300, 200);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Show registration form in a separate stage
    private void showRegistrationForm() {
        Stage registerStage = new Stage();
        registerStage.setTitle("Register");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(10);
        grid.setVgap(10);

        Label userLabel = new Label("Username:");
        TextField userTextField = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button registerButton = new Button("Register");

        grid.add(userLabel, 0, 0);
        grid.add(userTextField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(registerButton, 1, 3);

        // Register action
        registerButton.setOnAction(e -> {
            String username = userTextField.getText();
            String password = passwordField.getText();
            User.Role role = User.Role.USER;  // Default role is User

            if (userDAO.findUserByUsername(username) != null) {
                showAlert(Alert.AlertType.ERROR, "Registration Failed", "Username already exists.");
            } else {
                userDAO.saveUser(new User(username, password, role));
                showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "User registered successfully.");
                registerStage.close();
                start(new Stage());  // Go back to login page
            }
        });

        Scene scene = new Scene(grid, 300, 250);
        registerStage.setScene(scene);
        registerStage.show();
    }

    // Utility method to show alerts
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);  // Launch the JavaFX application
    }
}
