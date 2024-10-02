public class Location {
    public int id;
    public String name;
    public String city;

    public Location(int id, String name, String city ){
        this.name = name;
        this.city = city;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city){
        this.city = city;
    }

    @Override
    public String toString() {
        return name + " " + city;  // or return city if you want to show the city instead
    }
}
