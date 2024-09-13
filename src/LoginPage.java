import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage {
    private static UserDAO userDAO = new UserDAO();  // DAO to interact with the database

    public static void main(String[] args) {
        JFrame loginFrame = new JFrame("Login & Register");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(300, 200);
        loginFrame.setLayout(new GridLayout(4, 2));

        // Login form
        JLabel userLabel = new JLabel("Username:");
        JTextField userTextField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginFrame.add(userLabel);
        loginFrame.add(userTextField);
        loginFrame.add(passwordLabel);
        loginFrame.add(passwordField);
        loginFrame.add(loginButton);
        loginFrame.add(registerButton);

        // Login action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userTextField.getText();
                String password = new String(passwordField.getPassword());

                // Authenticate the user using the database
                User user = userDAO.authenticate(username, password);
                if (user != null) {
                    // Redirect based on role
                    loginFrame.dispose(); // Close the login window
                    if (user.getRole().equals("Medewerker")) {
                        Table.showTableForMedewerker();  // Full access
                    } else {
                        Table.showTableForUser();  // Limited access
                    }
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Invalid credentials", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Registration action
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginFrame.dispose();  // Close login window
                showRegistrationForm();
            }
        });

        loginFrame.setVisible(true);
    }

    // Show registration form
    private static void showRegistrationForm() {
        JFrame registerFrame = new JFrame("Register");
        registerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        registerFrame.setSize(300, 250);
        registerFrame.setLayout(new GridLayout(5, 2));

        // Registration form
        JLabel userLabel = new JLabel("Username:");
        JTextField userTextField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);

        JLabel roleLabel = new JLabel("Role:");
        String[] roles = {"Medewerker", "User"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);

        JButton registerButton = new JButton("Register");

        registerFrame.add(userLabel);
        registerFrame.add(userTextField);
        registerFrame.add(passwordLabel);
        registerFrame.add(passwordField);
        registerFrame.add(roleLabel);
        registerFrame.add(roleComboBox);
        registerFrame.add(new JLabel());  // Spacer
        registerFrame.add(registerButton);

        // Register action
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userTextField.getText();
                String password = new String(passwordField.getPassword());
                String role = (String) roleComboBox.getSelectedItem();

                // Check if the username already exists in the database
                if (userDAO.findUserByUsername(username) != null) {
                    JOptionPane.showMessageDialog(registerFrame, "Username already exists", "Registration Failed", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Add new user to the database
                    userDAO.saveUser(new User(username, password, role));
                    JOptionPane.showMessageDialog(registerFrame, "Registration successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                    registerFrame.dispose();  // Close registration window
                    main(null);  // Return to login
                }
            }
        });

        registerFrame.setVisible(true);
    }
}
