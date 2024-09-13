import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Table {

    // GUI components and user management
    private static FruitManager fruitManager;  // Manages fruits in the database
    private static JPanel fruitPanel;  // Panel for displaying fruits
    private static JTextField nameField;  // Text field for fruit name
    private static JTextField originField;  // Text field for country of origin
    private static JTextField currentStockField;  // Text field for current stock
    private static JButton addButton;  // Button to add or update fruit
    private static JScrollPane scrollPane;  // Scroll bar to browse the fruit panel

    // Search and language settings
    private static JTextField searchField;  // Search field for fruit
    private static JButton searchButton;    // Search button
    private static JButton logoutButton;    // Logout button
    private static JToggleButton languageToggle; // Language toggle button

    // For cart functionality (for users)
    private static JPanel cartPanel; // Panel for cart
    private static JButton addToCartButton; // Button to add fruits to cart

    // Variables to track selected fruit and row
    private static Fruit selectedFruit = null; // Tracks selected fruit for editing
    private static JPanel selectedPanel = null; // Tracks the selected row for highlighting

    // Language and user role settings
    private static boolean isEnglish = true; // Current language (default: English)
    private static boolean isMedewerker;  // Indicates whether the current user is a Medewerker

    // Method for Medewerker with full access
    public static void showTableForMedewerker() {
        setupFrame(true);  // Enable adding/updating fruits
    }

    // Method for users with limited access
    public static void showTableForUser() {
        setupFrame(false); // Disable adding/updating fruits
    }

    // Common setup for both roles
    private static void setupFrame(boolean medewerker) {
        isMedewerker = medewerker;  // Store isMedewerker as a class-level variable
        fruitManager = new FruitManager();  // Initialize FruitManager for database access

        // Create the main window with the correct title based on the role and language
        JFrame frame = new JFrame(isMedewerker ? (isEnglish ? "Medewerker - Fruit Table" : "Medewerker - Fruittafel") : (isEnglish ? "User - Fruit Table" : "Gebruiker - Fruittafel"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 700);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());  // Input panel with a grid layout
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.WEST;

        // Logout button at the top left
        logoutButton = new JButton(isEnglish ? "Logout" : "Uitloggen");
        c.gridx = 0;
        c.gridy = 0;
        inputPanel.add(logoutButton, c);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();  // Close the current window
                LoginPage.main(null);  // Go back to the login screen
            }
        });

        // Input fields (visible only to Medewerkers)
        if (isMedewerker) {
            JLabel nameLabel = new JLabel(isEnglish ? "Fruit Name:" : "Fruit Naam:");
            nameField = new JTextField(20);

            JLabel originLabel = new JLabel(isEnglish ? "Country of Origin:" : "Land van herkomst:");
            originField = new JTextField(20);

            JLabel currentStockLabel = new JLabel(isEnglish ? "Current Stock:" : "Huidige voorraad:");
            currentStockField = new JTextField(20);

            addButton = new JButton(isEnglish ? "Add Fruit" : "Fruit Toevoegen");

            // Add input fields and button to the input panel
            c.gridx = 0;
            c.gridy = 1;
            inputPanel.add(nameLabel, c);
            c.gridx = 1;
            inputPanel.add(nameField, c);
            c.gridx = 0;
            c.gridy = 2;
            inputPanel.add(originLabel, c);
            c.gridx = 1;
            inputPanel.add(originField, c);
            c.gridx = 0;
            c.gridy = 3;
            inputPanel.add(currentStockLabel, c);
            c.gridx = 1;
            inputPanel.add(currentStockField, c);
            c.gridx = 1;
            c.gridy = 4;
            inputPanel.add(addButton, c);
        }

        // Panel for displaying fruits
        fruitPanel = new JPanel();
        fruitPanel.setLayout(new BoxLayout(fruitPanel, BoxLayout.Y_AXIS));
        addTableHeader();  // Add table header

        scrollPane = new JScrollPane(fruitPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        // Load existing fruits from the database and add them to the panel
        List<Fruit> fruits = fruitManager.getFruits();  // Get all fruits from the database
        for (Fruit fruit : fruits) {
            addFruitToPanel(fruit);  // Add each fruit to the panel
        }

        // Add/update fruit functionality for Medewerkers
        if (isMedewerker) {
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Get input values
                    String name = nameField.getText();
                    String origin = originField.getText();
                    int currentStock;

                    // Check if the stock value is valid
                    try {
                        currentStock = Integer.parseInt(currentStockField.getText());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, isEnglish ? "Please enter a valid stock number." : "Voer een geldig voorraadnummer in.", isEnglish ? "Invalid Input" : "Ongeldige invoer", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Determine if the fruit is available based on stock
                    boolean isAvailable = currentStock > 0;

                    // Add new fruit or update existing fruit
                    if (selectedFruit == null) {
                        Fruit fruit = new Fruit(name, isAvailable, origin, currentStock);
                        fruitManager.addFruit(fruit);  // Add fruit to database
                        addFruitToPanel(fruit);
                    } else {
                        fruitManager.updateFruit(selectedFruit, name, isAvailable, origin, currentStock);
                        updateFruitPanel();  // Refresh the fruit display
                        selectedFruit = null;
                        addButton.setText(isEnglish ? "Add Fruit" : "Fruit Toevoegen");
                    }

                    resetFields();  // Reset input fields
                }
            });
        } else {
            // Cart functionality for users
            cartPanel = new JPanel();
            addToCartButton = new JButton(isEnglish ? "Add to Cart" : "Voeg toe aan winkelwagen");
            c.gridx = 1;
            c.gridy = 6;
            cartPanel.add(addToCartButton);
            inputPanel.add(cartPanel, c);
        }

        // Search field and button at the bottom of the table
        searchField = new JTextField(20);
        searchButton = new JButton(isEnglish ? "Search" : "Zoeken");

        // Language toggle button
        languageToggle = new JToggleButton(isEnglish ? "Switch to Dutch" : "Wissel naar Engels");
        languageToggle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isEnglish = !isEnglish;  // Toggle between English and Dutch
                frame.dispose();  // Close the current window
                if (isMedewerker) {
                    showTableForMedewerker();  // Reload with the selected language
                } else {
                    showTableForUser();
                }
            }
        });

        // Panel for search and language toggle buttons
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.add(languageToggle);
        bottomPanel.add(new JLabel(isEnglish ? "Search:" : "Zoeken:"));
        bottomPanel.add(searchField);
        bottomPanel.add(searchButton);

        // Search button functionality
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchTerm = searchField.getText().trim().toLowerCase();
                searchFruits(searchTerm);  // Search for fruits
            }
        });

        // Add components to the main window
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);  // Search and language toggle buttons at the bottom

        frame.setVisible(true);  // Show the window
    }

    // Method to add the table header
    private static void addTableHeader() {
        JPanel headerPanel = new JPanel(new GridLayout(1, isMedewerker ? 5 : 4)); // 5 columns for medewerker, 4 for user
        headerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel nameHeader = new JLabel(isEnglish ? "Name" : "Naam", SwingConstants.CENTER);
        JLabel availableHeader = new JLabel(isEnglish ? "Available" : "Beschikbaar", SwingConstants.CENTER);
        JLabel originHeader = new JLabel(isEnglish ? "Origin" : "Herkomst", SwingConstants.CENTER);
        JLabel currentStockHeader = new JLabel(isEnglish ? "Stock" : "Voorraad", SwingConstants.CENTER);

        headerPanel.add(nameHeader);
        headerPanel.add(availableHeader);
        headerPanel.add(originHeader);
        headerPanel.add(currentStockHeader);

        // Add "Actions" column only for Medewerkers
        if (isMedewerker) {
            JLabel actionHeader = new JLabel(isEnglish ? "Actions" : "Acties", SwingConstants.CENTER);
            headerPanel.add(actionHeader);
        }

        headerPanel.setPreferredSize(new Dimension(600, 30)); // Set a fixed height for the headers
        headerPanel.setMaximumSize(new Dimension(600, 30));

        fruitPanel.add(headerPanel);  // Add the header panel to the fruit panel
    }

    // Method to reset input fields
    private static void resetFields() {
        nameField.setText("");
        originField.setText("");
        currentStockField.setText("");
    }

    // Method to add fruit to the fruit panel
    private static void addFruitToPanel(Fruit fruit) {
        JPanel fruitEntryPanel = new JPanel(new GridLayout(1, isMedewerker ? 5 : 4));  // 5 columns for medewerker, 4 for user
        fruitEntryPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JLabel nameLabel = new JLabel(fruit.getName(), SwingConstants.CENTER);
        JLabel availableLabel = new JLabel(fruit.isAvailable() ? (isEnglish ? "Yes" : "Ja") : (isEnglish ? "No" : "Nee"), SwingConstants.CENTER);
        JLabel originLabel = new JLabel(fruit.getOrigin(), SwingConstants.CENTER);
        JLabel stockLabel = new JLabel(String.valueOf(fruit.getCurrentStock()), SwingConstants.CENTER);

        // Add the labels to the fruit panel
        fruitEntryPanel.add(nameLabel);
        fruitEntryPanel.add(availableLabel);
        fruitEntryPanel.add(originLabel);
        fruitEntryPanel.add(stockLabel);

        // Add "Delete" button for Medewerkers
        if (isMedewerker) {
            JButton deleteButton = new JButton(isEnglish ? "Delete" : "Verwijderen");
            deleteButton.setPreferredSize(new Dimension(80, 20));  // Set a fixed size for the delete button

            // Delete fruit functionality
            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fruitManager.deleteFruit(fruit);  // Remove fruit from the database
                    updateFruitPanel();  // Refresh the fruit display
                }
            });

            fruitEntryPanel.add(deleteButton);  // Add the delete button to the fruit panel
        }

        fruitEntryPanel.setPreferredSize(new Dimension(600, 30)); // Set a fixed size for each row
        fruitEntryPanel.setMaximumSize(new Dimension(600, 30));

        // Click functionality to select row for editing
        fruitEntryPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                highlightRow(fruitEntryPanel);  // Highlight the selected row
                selectedFruit = fruit;  // Track selected fruit
                nameField.setText(fruit.getName());
                originField.setText(fruit.getOrigin());
                currentStockField.setText(String.valueOf(fruit.getCurrentStock()));
                addButton.setText(isEnglish ? "Update Fruit" : "Fruit Bijwerken");
            }
        });

        fruitPanel.add(fruitEntryPanel);  // Add the fruit panel to the GUI
        fruitPanel.revalidate();
        fruitPanel.repaint();
    }

    // Method to search for fruits based on name or origin
    private static void searchFruits(String searchTerm) {
        fruitPanel.removeAll();  // Remove all existing rows
        addTableHeader();  // Add the table header again
        for (Fruit fruit : fruitManager.getFruits()) {
            // Check if the name or origin matches the search term
            if (fruit.getName().toLowerCase().contains(searchTerm) ||
                    fruit.getOrigin().toLowerCase().contains(searchTerm)) {
                addFruitToPanel(fruit);  // Add fruit to the panel
            }
        }
        fruitPanel.revalidate();
        fruitPanel.repaint();
    }

    // Method to highlight the selected row
    private static void highlightRow(JPanel panel) {
        if (selectedPanel != null) {
            selectedPanel.setBackground(null);  // Reset background of previous selection
        }
        panel.setBackground(Color.LIGHT_GRAY);  // Highlight the current selected row
        selectedPanel = panel;
    }

    // Method to update the fruit panel after editing or deleting
    private static void updateFruitPanel() {
        fruitPanel.removeAll();  // Remove all fruit data
        addTableHeader();  // Add the table header again
        for (Fruit fruit : fruitManager.getFruits()) {
            addFruitToPanel(fruit);  // Add each fruit back to the panel
        }
        fruitPanel.revalidate();
        fruitPanel.repaint();
    }
}
