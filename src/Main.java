public class Main {
    public static void main(String[] args) {

        // Initialize the database before starting the application
        DatabaseManager.initializeDatabase();  // Create the database and table if not exists

        // Start the rest of your application, e.g., show the login screen
        LoginPage.main(null);
    }
}
