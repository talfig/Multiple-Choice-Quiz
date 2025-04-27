import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.event.ActionEvent;
import javafx.scene.text.FontWeight;

import java.io.*;
import java.util.*;

public class QuizController {
    @FXML
    private VBox questionBox;

    private List<Question> questions = new ArrayList<>();
    private final Map<Question, ToggleGroup> questionToggleMap = new HashMap<>();
    private final Map<Question, Label> questionStatusLabels = new HashMap<>();

    @FXML
    public void initialize() {
        questions = loadQuestionsFromFile("src/questions.txt");
        Collections.shuffle(questions); // Randomize questions

        for (Question q : questions) {
            VBox questionItem = new VBox(5);

            Label questionLabel = new Label(q.getQuestion());
            questionLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
            questionItem.getChildren().add(questionLabel);

            // Shuffle the answers while ensuring the correct answer is preserved
            List<String> shuffledAnswers = new ArrayList<>(q.getAnswers());
            Collections.shuffle(shuffledAnswers);

            ToggleGroup group = new ToggleGroup();
            questionToggleMap.put(q, group);

            for (String answer : shuffledAnswers) {
                RadioButton rb = new RadioButton(answer);
                rb.setToggleGroup(group);
                questionItem.getChildren().add(rb);
            }

            // Add status label
            Label statusLabel = new Label();
            statusLabel.setFont(Font.font("System", 12));
            questionItem.getChildren().add(statusLabel);
            questionStatusLabels.put(q, statusLabel);

            questionBox.getChildren().add(questionItem);
        }
    }

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

        // After pressing OK, reset the quiz
        handleReset(null);
    }

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

            // Add status label
            Label statusLabel = new Label();
            statusLabel.setFont(Font.font("System", 12));
            questionItem.getChildren().add(statusLabel);
            questionStatusLabels.put(q, statusLabel);

            questionBox.getChildren().add(questionItem);
        }
    }

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
            e.printStackTrace();
        }
        return loadedQuestions;
    }
}
