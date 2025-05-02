import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.event.ActionEvent;
import javafx.scene.text.FontWeight;

import java.io.*;
import java.util.*;

/**
 * Controller class for handling the logic of a multiple-choice quiz application using JavaFX.
 */
public class QuizController {
    @FXML
    private VBox questionBox; // Container for displaying quiz questions dynamically.

    private List<Question> questions = new ArrayList<>(); // Holds the list of questions.
    private final Map<Question, ToggleGroup> questionToggleMap = new HashMap<>(); // Maps each question to its corresponding group of answer options.
    private final Map<Question, Label> questionStatusLabels = new HashMap<>(); // Maps each question to its status label (correct/incorrect).

    /**
     * Called automatically when the FXML file is loaded.
     * Loads questions from a file.
     */
    @FXML
    public void initialize() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the path to the questions file:");
        String filePath = scanner.nextLine();
        questions = loadQuestionsFromFile(filePath);
        Collections.shuffle(questions); // Randomize questions

        for (Question q : questions) {
            VBox questionItem = new VBox(5);

            // Display question text
            Label questionLabel = new Label(q.getQuestion());
            questionLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
            questionItem.getChildren().add(questionLabel);

            // Shuffle and display answer options
            List<String> shuffledAnswers = new ArrayList<>(q.getAnswers());
            Collections.shuffle(shuffledAnswers);

            ToggleGroup group = new ToggleGroup();
            questionToggleMap.put(q, group);

            for (String answer : shuffledAnswers) {
                RadioButton rb = new RadioButton(answer);
                rb.setToggleGroup(group);
                questionItem.getChildren().add(rb);
            }

            // Add label for answer status (e.g., correct, incorrect)
            Label statusLabel = new Label();
            statusLabel.setFont(Font.font("System", 12));
            questionItem.getChildren().add(statusLabel);
            questionStatusLabels.put(q, statusLabel);

            questionBox.getChildren().add(questionItem);
        }
    }

    /**
     * Handles the submission of the quiz.
     * Evaluates selected answers, updates status labels, shows total score, and resets the quiz.
     * @param event the action event triggered by clicking the "Submit" button.
     */
    @FXML
    private void handleSubmit(ActionEvent event) {
        int score = 0;

        for (Question q : questions) {
            ToggleGroup group = questionToggleMap.get(q);
            Label statusLabel = questionStatusLabels.get(q);

            if (group.getSelectedToggle() != null) {
                RadioButton selected = (RadioButton) group.getSelectedToggle();
                String selectedText = selected.getText();
                if (q.getCorrectAnswer().equals(selectedText)) {
                    score++;
                    statusLabel.setText("✔ Correct");
                    statusLabel.setTextFill(Color.GREEN);
                } else {
                    statusLabel.setText("✖ Incorrect");
                    statusLabel.setTextFill(Color.RED);
                }
            } else {
                statusLabel.setText("✖ No Answer Selected");
                statusLabel.setTextFill(Color.RED);
            }
        }

        // Display the score in a dialog box
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Quiz Result");
        alert.setHeaderText("Your Score:");
        alert.setContentText(score + " out of " + questions.size());
        alert.showAndWait();

        // Reset quiz after displaying result
        handleReset(null);
    }

    /**
     * Resets the quiz to its initial state by clearing the current UI
     * and regenerating the question and answer components.
     * @param event the action event triggered by clicking the "Reset" button or after submission.
     */
    @FXML
    private void handleReset(ActionEvent event) {
        questionBox.getChildren().clear();
        questionToggleMap.clear();
        questionStatusLabels.clear();

        Collections.shuffle(questions); // Shuffle the questions again

        for (Question q : questions) {
            VBox questionItem = new VBox(5);

            Label questionLabel = new Label(q.getQuestion());
            questionLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
            questionItem.getChildren().add(questionLabel);

            List<String> shuffledAnswers = new ArrayList<>(q.getAnswers());
            Collections.shuffle(shuffledAnswers); // Shuffle the answers too

            ToggleGroup group = new ToggleGroup();
            questionToggleMap.put(q, group);

            for (String answer : shuffledAnswers) {
                RadioButton rb = new RadioButton(answer);
                rb.setToggleGroup(group);
                questionItem.getChildren().add(rb);
            }

            Label statusLabel = new Label();
            statusLabel.setFont(Font.font("System", 12));
            questionItem.getChildren().add(statusLabel);
            questionStatusLabels.put(q, statusLabel);

            questionBox.getChildren().add(questionItem);
        }
    }

    /**
     * Loads questions from a specified text file.
     * The format of the file must be:
     * Question (line 1)
     * Correct answer (line 2)
     * Incorrect answer 1 (line 3)
     * Incorrect answer 2 (line 4)
     * Incorrect answer 3 (line 5)
     * (Repeat for each question block)
     *
     * @param filename the path to the question file
     * @return a list of parsed Question objects
     */
    private List<Question> loadQuestionsFromFile(String filename) {
        List<Question> loadedQuestions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    throw new IllegalArgumentException("File format error at line " + lineNumber + ": Missing question");
                }
                List<String> answers = new ArrayList<>();
                String correctAnswer = reader.readLine();
                lineNumber++;
                if (correctAnswer == null) {
                    throw new IllegalArgumentException("File format error at line " + lineNumber + ": Missing answer 1 for question: " + line);
                }
                answers.add(correctAnswer);

                for (int i = 0; i < 3; i++) {
                    String wrongAnswer = reader.readLine();
                    lineNumber++;
                    if (wrongAnswer == null) {
                        throw new IllegalArgumentException("File format error at line " + lineNumber + ": Missing answer " + (i + 2) + " for question: " + line);
                    }
                    answers.add(wrongAnswer);
                }

                loadedQuestions.add(new Question(line, answers, correctAnswer));
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return loadedQuestions;
    }
}
