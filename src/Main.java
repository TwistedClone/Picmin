public class Main {
    public static void main(String[] args) {
        DatabaseManager.initializeDatabase();

        LoginPage.launch(LoginPage.class);
    }
}
