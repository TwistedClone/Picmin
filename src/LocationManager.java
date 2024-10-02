import java.util.List;

public class LocationManager {
    private LocationDAO locationDAO;

    public LocationManager() {
        this.locationDAO = new LocationDAO();
    }

    // Add a new Location
    public void addLocation(Location location) {
        locationDAO.addLocation(location);
    }

    // Update an existing Location
    public void updateLocation(Location location) {
        locationDAO.updateLocation(location);
    }

    // Get all Locations
    public List<Location> getLocations() {
        return locationDAO.getAllLocations();
    }

    // Delete a Location
    public void deleteLocation(int locationId) {
        locationDAO.deleteLocation(locationId);
    }
}