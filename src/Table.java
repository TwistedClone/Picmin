import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Table {

    private static List<Fruit> fruits = new ArrayList<>();
    private static JPanel fruitPanel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Fruit Input Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        JLabel nameLabel = new JLabel("Fruit Name:");
        JTextField nameField = new JTextField(20);

        JLabel availableLabel = new JLabel("Available:");
        JCheckBox availableCheckBox = new JCheckBox();

        JLabel originLabel = new JLabel("Country of Origin:");
        JTextField originField = new JTextField(20);

        JButton addButton = new JButton("Add Fruit");

        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(availableLabel);
        inputPanel.add(availableCheckBox);
        inputPanel.add(originLabel);
        inputPanel.add(originField);
        inputPanel.add(addButton);

        fruitPanel = new JPanel();
        fruitPanel.setLayout(new BoxLayout(fruitPanel, BoxLayout.Y_AXIS));
        addTableHeader();

        JScrollPane scrollPane = new JScrollPane(fruitPanel);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String name = nameField.getText();
                boolean isAvailable = availableCheckBox.isSelected();
                String origin = originField.getText();

                Fruit fruit = new Fruit(name, isAvailable, origin);
                fruits.add(fruit);

                addFruitToPanel(fruit);

                nameField.setText("");
                availableCheckBox.setSelected(false);
                originField.setText("");
            }
        });

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private static void addTableHeader() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel nameHeader = new JLabel("Name");
        JLabel availableHeader = new JLabel("Available");
        JLabel originHeader = new JLabel("Origin");
        JLabel actionHeader = new JLabel("Action");

        nameHeader.setPreferredSize(new Dimension(80, 20));
        availableHeader.setPreferredSize(new Dimension(80, 20));
        originHeader.setPreferredSize(new Dimension(80, 20));
        actionHeader.setPreferredSize(new Dimension(80, 20));

        headerPanel.add(nameHeader);
        headerPanel.add(availableHeader);
        headerPanel.add(originHeader);
        headerPanel.add(actionHeader);

        fruitPanel.add(headerPanel);
    }


    private static void addFruitToPanel(Fruit fruit) {
        JPanel fruitEntryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel fruitLabel = new JLabel(fruit.displayFruit());
        JButton deleteButton = new JButton("Delete");
        deleteButton.setPreferredSize(new Dimension(80, 20));

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fruits.remove(fruit);
                fruitPanel.remove(fruitEntryPanel);
                fruitPanel.revalidate();
                fruitPanel.repaint();
            }
        });

        fruitEntryPanel.add(fruitLabel);
        fruitEntryPanel.add(deleteButton);
        fruitPanel.add(fruitEntryPanel);

        fruitPanel.revalidate();
        fruitPanel.repaint();
    }
}

