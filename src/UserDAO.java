import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private static final String DB_URL = "jdbc:sqlite:your_database.db";

    // Constructor om de database te initialiseren en de gebruikers tabel te maken als deze nog niet bestaat
    public UserDAO() {
        createUsersTable();  // Roep de methode aan om de users-tabel te maken
    }

    // Methode om de users-tabel te maken
    private void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +  // Unieke ID voor elke gebruiker
                "username TEXT NOT NULL, " +  // Gebruikersnaam van de gebruiker
                "password TEXT NOT NULL, " +  // Wachtwoord van de gebruiker
                "role TEXT NOT NULL);";  // Rol van de gebruiker (bijv. Medewerker of Gebruiker)

        // Maak verbinding met de database en voer het SQL-commando uit om de tabel te maken
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.executeUpdate();  // Voer de SQL-update uit
            System.out.println("Users table created or already exists.");  // Tabel is gemaakt of bestaat al

        } catch (SQLException e) {
            System.out.println("Error creating users table: " + e.getMessage());  // Foutmelding als het maken van de tabel mislukt
        }
    }

    // Methode om een nieuwe gebruiker op te slaan in de database
    public void saveUser(User user) {
        String sql = "INSERT INTO users(username, password, role) VALUES(?, ?, ?)";  // SQL-commando om een nieuwe gebruiker in te voegen

        // Maak verbinding met de database en stel de waarden in voor de gebruikersinvoer
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());  // Zet de gebruikersnaam
            pstmt.setString(2, user.getPassword());  // Zet het wachtwoord
            pstmt.setString(3, user.getRole());  // Zet de gebruikersrol

            pstmt.executeUpdate();  // Voer de SQL-update uit om de gebruiker op te slaan
            System.out.println("User saved successfully!");  // Bevestigingsbericht dat de gebruiker is opgeslagen

        } catch (SQLException e) {
            System.out.println("Error saving user: " + e.getMessage());  // Foutmelding bij het opslaan van de gebruiker
        }
    }

    // Methode om een gebruiker te authentiseren door de gebruikersnaam en het wachtwoord te controleren
    public User authenticate(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";  // SQL-query om de gebruiker te zoeken op basis van gebruikersnaam en wachtwoord

        // Maak verbinding met de database en stel de gebruikersnaam en het wachtwoord in voor de query
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);  // Zet de gebruikersnaam
            pstmt.setString(2, password);  // Zet het wachtwoord
            ResultSet rs = pstmt.executeQuery();  // Voer de query uit en bewaar de resultaten

            if (rs.next()) {
                String role = rs.getString("role");  // Haal de rol van de gebruiker op
                return new User(username, password, role);  // Retourneer de geauthentiseerde gebruiker
            }

        } catch (SQLException e) {
            System.out.println("Error during authentication: " + e.getMessage());  // Foutmelding bij het authenticeren
        }
        return null;  // Gebruiker niet gevonden of authenticatie mislukt
    }

    // Methode om een gebruiker te vinden op basis van de gebruikersnaam
    public User findUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";  // SQL-query om een gebruiker te zoeken op basis van gebruikersnaam

        // Maak verbinding met de database en stel de gebruikersnaam in voor de query
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);  // Zet de gebruikersnaam
            ResultSet rs = pstmt.executeQuery();  // Voer de query uit en bewaar de resultaten

            if (rs.next()) {
                String password = rs.getString("password");  // Haal het wachtwoord van de gebruiker op
                String role = rs.getString("role");  // Haal de rol van de gebruiker op
                return new User(username, password, role);  // Retourneer de gevonden gebruiker
            }

        } catch (SQLException e) {
            System.out.println("Error finding user: " + e.getMessage());  // Foutmelding bij het zoeken van de gebruiker
        }
        return null;  // Gebruiker niet gevonden
    }
}
