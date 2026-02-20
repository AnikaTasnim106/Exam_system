package com.buet.edutrack.controllers;

import com.buet.edutrack.models.Exam;
import com.buet.edutrack.models.Question;
import com.buet.edutrack.models.Result;
import com.buet.edutrack.services.ExamService;
import com.buet.edutrack.services.QuestionService;
import com.buet.edutrack.services.ResultService;
import com.buet.edutrack.utils.SceneManager;
import com.buet.edutrack.utils.SessionManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.util.*;

public class TakeExamController {

    @FXML
    private Label examTitleLabel;
    @FXML
    private Label timerLabel;
    @FXML
    private Label progressLabel;
    @FXML
    private GridPane questionNavGrid;
    @FXML
    private Label questionNumberLabel;
    @FXML
    private Label questionSubjectLabel;
    @FXML
    private Label questionDifficultyLabel;
    @FXML
    private Label questionTextLabel;
    @FXML
    private VBox optionsBox;
    @FXML
    private Button prevButton;
    @FXML
    private Button nextButton;

    private static Exam currentExam;
    private List<Question> questions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private Map<String, String> studentAnswers = new HashMap<>();
    private Timeline timer;
    private int remainingSeconds;
    private ToggleGroup optionsGroup;
    private int startTimeSeconds;

    public static void setCurrentExam(Exam exam) {
        currentExam = exam;
    }

    @FXML
    public void initialize() {
        if (currentExam == null) {
            showError("No exam selected!");
            SceneManager.switchScene("/views/exam-list.fxml");
            return;
        }

        loadExam();
        setupTimer();
        setupQuestionNavigation();
        displayQuestion(0);
    }

    private void loadExam() {
        examTitleLabel.setText(currentExam.getTitle());

        // Load questions
        for (String questionId : currentExam.getQuestionIds()) {
            Question q = QuestionService.getAllQuestions().stream()
                    .filter(question -> question.getId().equals(questionId))
                    .findFirst()
                    .orElse(null);
            if (q != null) {
                questions.add(q);
            }
        }

        if (questions.isEmpty()) {
            showError("No questions found for this exam!");
            SceneManager.switchScene("/views/exam-list.fxml");
        }
    }

    private void setupTimer() {
        remainingSeconds = currentExam.getDuration() * 60;
        startTimeSeconds = remainingSeconds;
        updateTimerDisplay();

        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            remainingSeconds--;
            updateTimerDisplay();

            if (remainingSeconds <= 0) {
                timer.stop();
                autoSubmitExam();
            } else if (remainingSeconds <= 60) {
                timerLabel.setStyle("-fx-font-size: 28px; -fx-text-fill: #ff0000; -fx-font-weight: bold;");
            } else if (remainingSeconds <= 300) {
                timerLabel.setStyle("-fx-font-size: 28px; -fx-text-fill: #ffa500; -fx-font-weight: bold;");
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void updateTimerDisplay() {
        int minutes = remainingSeconds / 60;
        int seconds = remainingSeconds % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void setupQuestionNavigation() {
        questionNavGrid.getChildren().clear();

        int row = 0;
        int col = 0;

        for (int i = 0; i < questions.size(); i++) {
            final int index = i;
            Button navBtn = new Button(String.valueOf(i + 1));
            navBtn.setPrefSize(40, 40);
            navBtn.setStyle("-fx-background-color: #363648; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand;");

            navBtn.setOnAction(e -> displayQuestion(index));

            questionNavGrid.add(navBtn, col, row);

            col++;
            if (col > 3) {
                col = 0;
                row++;
            }
        }

        updateProgress();
    }

    private void displayQuestion(int index) {
        if (index < 0 || index >= questions.size()) return;

        currentQuestionIndex = index;
        Question question = questions.get(index);

        // Update header
        questionNumberLabel.setText("Question " + (index + 1) + " of " + questions.size());
        questionSubjectLabel.setText(question.getSubject());

        String difficulty = question.getDifficulty();
        questionDifficultyLabel.setText(difficulty);
        switch (difficulty) {
            case "Easy":
                questionDifficultyLabel.setStyle("-fx-text-fill: #00ff00; -fx-font-size: 13px; -fx-font-weight: bold;");
                break;
            case "Medium":
                questionDifficultyLabel.setStyle("-fx-text-fill: #ffa500; -fx-font-size: 13px; -fx-font-weight: bold;");
                break;
            case "Hard":
                questionDifficultyLabel.setStyle("-fx-text-fill: #ff0000; -fx-font-size: 13px; -fx-font-weight: bold;");
                break;
        }

        // Update question text
        questionTextLabel.setText(question.getQuestionText());

        // Create options
        optionsBox.getChildren().clear();
        optionsGroup = new ToggleGroup();

        String[] options = {
                "A) " + question.getOptionA(),
                "B) " + question.getOptionB(),
                "C) " + question.getOptionC(),
                "D) " + question.getOptionD()
        };

        String[] optionValues = {"A", "B", "C", "D"};

        for (int i = 0; i < options.length; i++) {
            RadioButton option = new RadioButton(options[i]);
            option.setToggleGroup(optionsGroup);
            option.setUserData(optionValues[i]);
            option.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
            option.setPadding(new Insets(10));

            // Set previously selected answer
            if (studentAnswers.containsKey(question.getId()) &&
                    studentAnswers.get(question.getId()).equals(optionValues[i])) {
                option.setSelected(true);
            }

            // Save answer when selected
            option.setOnAction(e -> {
                studentAnswers.put(question.getId(), (String) option.getUserData());
                updateQuestionNavButton(currentQuestionIndex, true);
                updateProgress();
            });

            optionsBox.getChildren().add(option);
        }

        // Update navigation buttons
        prevButton.setDisable(index == 0);
        nextButton.setText(index == questions.size() - 1 ? "Finish" : "Next →");

        // Update navigation grid
        updateQuestionNavButton(index, studentAnswers.containsKey(question.getId()));
    }

    private void updateQuestionNavButton(int index, boolean answered) {
        Button btn = (Button) questionNavGrid.getChildren().get(index);
        if (answered) {
            btn.setStyle("-fx-background-color: #6c63ff; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand;");
        } else {
            btn.setStyle("-fx-background-color: #363648; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand;");
        }
    }

    private void updateProgress() {
        int answered = studentAnswers.size();
        int total = questions.size();
        progressLabel.setText("Answered: " + answered + " / " + total);
    }

    @FXML
    private void handlePrevious() {
        if (currentQuestionIndex > 0) {
            displayQuestion(currentQuestionIndex - 1);
        }
    }

    @FXML
    private void handleNext() {
        if (currentQuestionIndex < questions.size() - 1) {
            displayQuestion(currentQuestionIndex + 1);
        } else {
            handleSubmitExam();
        }
    }

    @FXML
    private void handleSubmitExam() {
        // Check if all questions answered
        int unanswered = questions.size() - studentAnswers.size();

        String message;
        if (unanswered > 0) {
            message = "You have " + unanswered + " unanswered question(s).\n\n" +
                    "Are you sure you want to submit?";
        } else {
            message = "You have answered all questions.\n\n" +
                    "Submit your exam now?";
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Submit Exam");
        alert.setHeaderText("Confirm Submission");
        alert.setContentText(message);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                submitExam();
            }
        });
    }

    private void autoSubmitExam() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Time's Up!");
            alert.setHeaderText("Exam Time Expired");
            alert.setContentText("Your exam will be submitted automatically.");
            alert.showAndWait();

            submitExam();
        });
    }

    private void submitExam() {
        timer.stop();
        int correct = 0;
        for (Question q : questions) {
            String studentAnswer = studentAnswers.getOrDefault(q.getId(), "");
            if (studentAnswer.equals(q.getCorrectAnswer())) {
                correct++;
            }
        }
        String currentUsername = SessionManager.getCurrentUsername();
        if (currentUsername == null) currentUsername = "student";
        Result result = new Result(currentExam.getId(), currentUsername);
        result.calculateScore(questions.size(), correct);
        result.setStudentAnswers(studentAnswers);
        result.setTimeTaken(startTimeSeconds - remainingSeconds);
        ResultService.addResult(result);
        showResultDialog(result);
        SceneManager.switchScene("/views/student-dashboard.fxml");
    }

    private void showResultDialog(Result result) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exam Completed!");
        alert.setHeaderText("Your Results");

        String content = String.format(
                "Score: %d / %d\n" +
                        "Percentage: %.2f%%\n" +
                        "Grade: %s\n\n" +
                        "Correct: %d\n" +
                        "Wrong: %d\n" +
                        "Time Taken: %d min %d sec",
                result.getCorrectAnswers(),
                result.getTotalQuestions(),
                result.getPercentage(),
                result.getGrade(),
                result.getCorrectAnswers(),
                result.getWrongAnswers(),
                result.getTimeTaken() / 60,
                result.getTimeTaken() % 60
        );

        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}