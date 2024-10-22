import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public abstract class BaseView {

    protected Stage stage;
    protected VBox root;  // Base layout for all views

    public BaseView(Stage stage) {
        this.stage = stage;
        root = new VBox(10);  // Initialize the base layout with spacing
    }
    // Method to set the scene for the stage, which will include adding the stylesheet
    protected void setScene(Scene scene) {
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
    }

    // Abstract method for showing the view, to be implemented by subclasses
    public abstract void show();
}
