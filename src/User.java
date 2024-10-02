public class User {

    public enum Role {
        USER("User"),
        MEDEWERKER("Medewerker"),
        ADMIN("Admin");

        private final String roleName;

        Role(String roleName) {
            this.roleName = roleName;
        }

        public String getRoleName() {
            return roleName;
        }

        public static Role fromString(String role) {
            for (Role r : Role.values()) {
                if (r.getRoleName().equalsIgnoreCase(role)) {
                    return r;
                }
            }
            throw new IllegalArgumentException("No enum constant for role: " + role);
        }
    }

    private int id;  // Add id field
    private String username;
    private String password;
    private Role role;

    // Constructor with id
    public User(int id, String username, String password, Role role) {
        this.id = id;  // Initialize id
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Constructor without id (for user creation, for example)
    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getter for id
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public void setId(int id) {
        this.id = id;
    }
}
