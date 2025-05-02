import java.util.List;

/**
 * Represents a quiz question with its possible answers and the correct answer.
 */
public class Question {
    private final String question; // The text of the question
    private final List<String> answers; // List of possible answers including the correct one
    private final String correctAnswer; // The correct answer to the question

    /**
     * Constructs a new Question object.
     *
     * @param question       The text of the question.
     * @param answers        A list of all possible answers (first is the correct answer in original input).
     * @param correctAnswer  The correct answer text.
     */
    public Question(String question, List<String> answers, String correctAnswer) {
        this.question = question;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }

    /**
     * Returns the question text.
     *
     * @return The question as a String.
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Returns the list of possible answers.
     *
     * @return A list of answer options.
     */
    public List<String> getAnswers() {
        return answers;
    }

    /**
     * Returns the correct answer.
     *
     * @return The correct answer as a String.
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
