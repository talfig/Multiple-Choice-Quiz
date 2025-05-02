import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The Quiz class is a JavaFX application that loads a quiz UI
 * from an FXML file and displays it in a resizable window.
 */
public class Quiz extends Application {
    // Constants defining the dimensions of the quiz window
    private static final int QUIZ_WIDTH_SIZE = 600;
    private static final int QUIZ_HEIGHT_SIZE = 800;

    /**
     * The main entry point for the JavaFX application.
     * This method is automatically called after the application is launched.
     *
     * @param stage the primary stage for this application.
     * @throws Exception if loading the FXML file fails.
     */
    @Override
    public void start(Stage stage) throws Exception {
        // Load the quiz layout from the FXML file
        Parent root = FXMLLoader.load(getClass().getResource("quiz.fxml"));
        Scene scene = new Scene(root, QUIZ_WIDTH_SIZE, QUIZ_HEIGHT_SIZE);
        stage.setTitle("Quiz");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main method launches the JavaFX application.
     *
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        launch(args);
        System.out.println();
    }
}
