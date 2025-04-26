import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Quiz extends Application {
    private static final int QUIZ_WIDTH_SIZE = 600;
    private static final int QUIZ_HEIGHT_SIZE = 800;

    public void start(Stage stage) throws Exception{
        Parent root = (Parent) FXMLLoader.load(getClass().getResource("quiz.fxml"));
        Scene scene = new Scene(root, QUIZ_WIDTH_SIZE, QUIZ_HEIGHT_SIZE);
        stage.setTitle("Quiz");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
        System.out.println();
    }
}
