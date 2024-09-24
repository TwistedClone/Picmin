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

        // Method to map strings to enum values
        public static Role fromString(String role) {
            for (Role r : Role.values()) {
                if (r.getRoleName().equalsIgnoreCase(role)) {
                    return r;
                }
            }
            throw new IllegalArgumentException("No enum constant for role: " + role);
        }
    }

    private String username;
    private String password;
    private Role role;

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
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
}
