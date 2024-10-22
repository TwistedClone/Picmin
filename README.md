***Picit***

Picit is een applicatie die gebruikers in staat stelt om fruit te beheren en te kopen. De applicatie biedt twee rollen: "Medewerker" en "Gebruiker". Medewerkers kunnen fruit toevoegen, bewerken en verwijderen, terwijl gebruikers fruit kunnen bekijken en toevoegen aan hun winkelwagen.
Functionaliteiten
1. Rollen

    Medewerker: Heeft volledige toegang om fruit toe te voegen, bij te werken en te verwijderen. Medewerkers kunnen ook alle gegevens van het fruit zien, inclusief de beschikbaarheid.
    Gebruiker: Heeft beperkte toegang en kan alleen fruit bekijken en toevoegen aan de winkelwagen. Gebruikers kunnen geen fruitgegevens wijzigen.

2. Fruitbeheer

Medewerkers kunnen fruit beheren door het invoeren van:

    Fruitnaam
    Land van herkomst
    Huidige voorraad

De beschikbaarheid van het fruit wordt automatisch berekend op basis van de voorraad. Als de voorraad groter is dan 0, wordt het fruit als beschikbaar gemarkeerd.
3. Taalondersteuning

De applicatie biedt de mogelijkheid om de taal te wisselen tussen Engels en Nederlands. Dit kan eenvoudig worden gedaan via een taalwisselknop in de interface.
4. Zoekfunctionaliteit

Gebruikers kunnen fruit zoeken op naam of land van herkomst. De zoekresultaten worden dynamisch bijgewerkt in de interface.
5. Winkelwagen (voor gebruikers)

Gebruikers kunnen fruit toevoegen aan hun winkelwagen. De beschikbaarheid van het fruit wordt weergegeven, zodat gebruikers alleen beschikbare items kunnen toevoegen.
Installatie

    Clone de repository:

    bash

    git clone https://github.com/jouw-gebruikersnaam/Picit.git

    Open het project in je IDE: Zorg ervoor dat je een Java-ontwikkelomgeving hebt geïnstalleerd (bijv. IntelliJ IDEA).

    Voeg de SQLite JDBC Driver toe: De applicatie maakt gebruik van een SQLite-database. Zorg ervoor dat de SQLite JDBC-driver is toegevoegd aan het project.

    Start de applicatie: Compileer en voer de applicatie uit vanuit je IDE.

Vereisten

    Java: Java JDK 11 of hoger
    SQLite: SQLite voor databasebeheer
    JDBC Driver: JDBC Driver voor SQLite om databaseverbindingen te ondersteunen

Gebruik

    Inloggen:
        Bij het opstarten van de applicatie wordt gevraagd om in te loggen met een gebruikersnaam en wachtwoord.
        Afhankelijk van de rol van de gebruiker (Medewerker of Gebruiker) wordt de juiste interface weergegeven.

    Voor Medewerkers:
        Fruit toevoegen: Vul de fruitnaam, het land van herkomst en de voorraad in. De beschikbaarheid wordt automatisch berekend.
        Fruit bewerken: Klik op een fruit in de lijst om de gegevens te bewerken. Werk de velden bij en klik op "Fruit Bijwerken".
        Fruit verwijderen: Medewerkers kunnen fruit uit de database verwijderen.

    Voor Gebruikers:
        Fruit bekijken: Gebruikers kunnen een lijst van beschikbaar fruit bekijken.
        Winkelwagen: Voeg fruit toe aan de winkelwagen door te klikken op "Voeg toe aan winkelwagen".

Database

De applicatie gebruikt SQLite om fruit- en gebruikersgegevens op te slaan. De database bevat de volgende tabellen:

    Users: Bevat gebruikersgegevens (gebruikersnaam, wachtwoord, rol).
    Fruits: Bevat fruitgegevens (naam, herkomst, voorraad, beschikbaarheid).
    Locations: Bevat locatiegegevens (naam, stad)
    Categories: Bevat categoriegegevens(naam, beschrijving)
