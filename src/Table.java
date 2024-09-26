import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;
import java.util.function.Function;

public class Table<T> extends Application {

    public VBox dataPanel;
    public TableView<T> tableView;  // Use JavaFX's TableView to handle selection

    // To launch the table, we need columns and data to be passed dynamically
    public List<ColumnDefinition<T>> columns;
    public List<T> items;

    public Table(List<ColumnDefinition<T>> columns, List<T> items) {
        this.columns = columns;
        this.items = items;
        this.tableView = new TableView<>();  // Initialize the TableView
    }

    public List<ColumnDefinition<T>> getColumns() {
        return columns;
    }

    @Override
    public void start(Stage primaryStage) {
        setupStage(primaryStage, columns, items);
    }

    public void setupStage(Stage primaryStage, List<ColumnDefinition<T>> columns, List<T> items) {
        primaryStage.setTitle("Generic Table");

        // Create the root layout
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        // Create the table using the TableView and load data into it
        setupTable(columns, items);

        // Add TableView to the root panel
        root.getChildren().clear();
        root.getChildren().add(tableView);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void setupTable(List<ColumnDefinition<T>> columns, List<T> items) {
        // Clear any existing columns before setting up new ones
        tableView.getColumns().clear();

        // Add columns dynamically
        for (ColumnDefinition<T> column : columns) {
            TableColumn<T, String> tableColumn = new TableColumn<>(column.getHeader());
            tableColumn.setCellValueFactory(cellData -> {
                T item = cellData.getValue();
                return new SimpleStringProperty(column.getValueFunction().apply(item));
            });
            tableColumn.setPrefWidth(100);
            tableView.getColumns().add(tableColumn);
        }

        // Add data to the table
        tableView.getItems().setAll(items);
    }

    // Expose the selected item from the TableView
    public T getSelectedItem() {
        return tableView.getSelectionModel().getSelectedItem();
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}
