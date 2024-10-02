import java.util.Arrays;
import java.util.List;

public class Country {

    private String name;

    public Country(String name) {
        this.name = name;
    }

    public String getCountryName() {
        return name;
    }

    @Override
    public String toString() {
        return name;  // or return city if you want to show the city instead
    }

    // Static method to get all countries
    public static List<Country> getAllCountries() {
        return Arrays.asList(
                new Country("United States"), new Country("Canada"), new Country("Mexico"),
                new Country("United Kingdom"), new Country("Germany"), new Country("France"),
                new Country("Netherlands"), new Country("China"), new Country("Japan"),
                new Country("India"), new Country("Brazil"), new Country("Australia"),
                new Country("New Zealand")
        );
    }
}

