import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FruitDAO {
    private static final String DB_URL = "jdbc:sqlite:your_database.db";

    // Constructor om de fruitentabel te maken als deze nog niet bestaat
    public FruitDAO() {
        createFruitsTable();  // Roep de methode aan om de fruits tabel te maken
    }

    // Methode om de fruits tabel te maken
    private void createFruitsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS fruits (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +  // Uniek ID voor elke fruitinvoer
                "name TEXT NOT NULL, " +  // Naam van het fruit
                "available INTEGER NOT NULL, " +  // Beschikbaarheid, opgeslagen als INTEGER (0 = false, 1 = true)
                "origin TEXT NOT NULL, " +  // Land van herkomst van het fruit
                "current_stock INTEGER NOT NULL);";  // Huidige voorraad van het fruit

        // Maak verbinding met de database en voer het SQL-commando uit om de tabel te maken
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.executeUpdate();  // Voer de SQL-update uit
            System.out.println("Fruits table created or already exists.");  // Tabel is gemaakt of bestaat al

        } catch (SQLException e) {
            System.out.println("Error creating fruits table: " + e.getMessage());  // Foutmelding als het maken van de tabel mislukt
        }
    }

    // Methode om een nieuw fruit op te slaan in de database
    public void saveFruit(Fruit fruit) {
        String sql = "INSERT INTO fruits(name, available, origin, current_stock) VALUES(?, ?, ?, ?)";  // SQL-commando om gegevens in te voegen

        // Maak verbinding met de database en stel de waarden in voor de fruitinvoer
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fruit.getName());  // Zet de naam van het fruit
            pstmt.setInt(2, fruit.isAvailable() ? 1 : 0);  // Zet beschikbaarheid om naar 1 (waar) of 0 (niet waar)
            pstmt.setString(3, fruit.getOrigin());  // Zet de herkomst van het fruit
            pstmt.setInt(4, fruit.getCurrentStock());  // Zet de huidige voorraad

            pstmt.executeUpdate();  // Voer de SQL-update uit om het fruit op te slaan
            System.out.println("Fruit saved successfully!");  // Bevestigingsbericht dat het fruit is opgeslagen

        } catch (SQLException e) {
            System.out.println("Error saving fruit: " + e.getMessage());  // Foutmelding bij het opslaan van het fruit
        }
    }

    // Methode om een bestaand fruit in de database bij te werken
    public void updateFruit(Fruit fruit) {
        String sql = "UPDATE fruits SET name = ?, available = ?, origin = ?, current_stock = ? WHERE id = ?";  // SQL-commando om fruitgegevens bij te werken

        // Maak verbinding met de database en stel de nieuwe waarden in
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fruit.getName());  // Werk de fruitnaam bij
            pstmt.setInt(2, fruit.isAvailable() ? 1 : 0);  // Werk de beschikbaarheid bij (1 of 0)
            pstmt.setString(3, fruit.getOrigin());  // Werk de herkomst bij
            pstmt.setInt(4, fruit.getCurrentStock());  // Werk de voorraad bij
            pstmt.setInt(5, fruit.getId());  // Gebruik het unieke ID om het juiste fruit te vinden

            pstmt.executeUpdate();  // Voer de SQL-update uit om de wijzigingen op te slaan
            System.out.println("Fruit updated successfully!");  // Bevestigingsbericht dat het fruit is bijgewerkt

        } catch (SQLException e) {
            System.out.println("Error updating fruit: " + e.getMessage());  // Foutmelding bij het bijwerken van het fruit
        }
    }

    // Methode om alle fruitgegevens uit de database op te halen
    public List<Fruit> getFruits() {
        List<Fruit> fruits = new ArrayList<>();  // Lijst om alle fruitobjecten op te slaan
        String sql = "SELECT * FROM fruits";  // SQL-commando om alle fruitgegevens op te halen

        // Maak verbinding met de database en voer de query uit
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {  // Voer de query uit en sla het resultaat op

            // Doorloop alle rijen van het resultaat
            while (rs.next()) {
                int id = rs.getInt("id");  // Haal het fruit-ID op
                String name = rs.getString("name");  // Haal de fruitnaam op
                boolean available = rs.getInt("available") == 1;  // Zet 1 of 0 om naar een boolean waarde (beschikbaar of niet)
                String origin = rs.getString("origin");  // Haal de herkomst op
                int currentStock = rs.getInt("current_stock");  // Haal de voorraad op

                fruits.add(new Fruit(id, name, available, origin, currentStock));  // Voeg het fruit toe aan de lijst
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving fruits: " + e.getMessage());  // Foutmelding bij het ophalen van fruitgegevens
        }

        return fruits;  // Retourneer de lijst met fruitobjecten
    }

    // Methode om een fruit uit de database te verwijderen
    public void deleteFruit(int id) {
        String sql = "DELETE FROM fruits WHERE id = ?";  // SQL-commando om een fruitinvoer te verwijderen op basis van het ID

        // Maak verbinding met de database en verwijder het fruit met het opgegeven ID
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);  // Zet het fruit-ID in de SQL-query
            pstmt.executeUpdate();  // Voer de SQL-update uit om het fruit te verwijderen
            System.out.println("Fruit deleted successfully!");  // Bevestigingsbericht dat het fruit is verwijderd

        } catch (SQLException e) {
            System.out.println("Error deleting fruit: " + e.getMessage());  // Foutmelding bij het verwijderen van fruit
        }
    }
}
