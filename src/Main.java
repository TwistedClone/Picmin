public class Main {
    public static void main(String[] args) {
        // Initialize the database
        DatabaseManager.initializeDatabase();

        // Launch the LoginPage JavaFX application
        LoginPage.launch(LoginPage.class);
    }
}
