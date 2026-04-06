package com.buet.edutrack.controllers;

import com.buet.edutrack.models.Question;
import com.buet.edutrack.services.QuestionService;
import com.buet.edutrack.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AddQuestionController {

    @FXML
    private ComboBox<String> subjectCombo;
    @FXML
    private ComboBox<String> difficultyCombo;
    @FXML
    private TextArea questionTextArea;
    @FXML
    private TextField optionAField;
    @FXML
    private TextField optionBField;
    @FXML
    private TextField optionCField;
    @FXML
    private TextField optionDField;
    @FXML
    private ComboBox<String> correctAnswerCombo;
    @FXML
    private TextArea explanationArea;
    @FXML
    private Label errorLabel;

    @FXML private Button backButton;
    @FXML private Button saveBtn;
    @FXML private Button clearBtn;

    @FXML
    public void initialize() {
        subjectCombo.getItems().addAll(
                "Mathematics", "Physics", "Chemistry", "Biology",
                "Computer Science", "English", "History", "Geography"
        );
        difficultyCombo.getItems().addAll("Easy", "Medium", "Hard");
        correctAnswerCombo.getItems().addAll("A", "B", "C", "D");
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: #6c63ff; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 8; -fx-font-weight: bold; -fx-border-color: #6c63ff; -fx-border-width: 1; -fx-border-radius: 8;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: #2d1b69; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 8; -fx-font-weight: bold; -fx-border-color: #6c63ff; -fx-border-width: 1; -fx-border-radius: 8;"));

        saveBtn.setOnMouseEntered(e -> saveBtn.setStyle("-fx-background-color: #5848ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 8; -fx-font-weight: bold; -fx-cursor: hand;"));
        saveBtn.setOnMouseExited(e -> saveBtn.setStyle("-fx-background-color: #6c63ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 8; -fx-font-weight: bold; -fx-cursor: hand;"));

        clearBtn.setOnMouseEntered(e -> clearBtn.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 8; -fx-font-weight: bold; -fx-cursor: hand;"));
        clearBtn.setOnMouseExited(e -> clearBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 8; -fx-font-weight: bold; -fx-cursor: hand;"));
    }

    @FXML
    private void handleSave() {
        errorLabel.setVisible(false);
        if (subjectCombo.getValue() == null) {
            showError("Please select a subject!");
            return;
        }
        if (difficultyCombo.getValue() == null) {
            showError("Please select difficulty level!");
            return;
        }
        String questionText = questionTextArea.getText().trim();
        if (questionText.isEmpty()) {
            showError("Please enter the question text!");
            return;
        }
        String optionA = optionAField.getText().trim();
        String optionB = optionBField.getText().trim();
        String optionC = optionCField.getText().trim();
        String optionD = optionDField.getText().trim();
        if (optionA.isEmpty() || optionB.isEmpty() || optionC.isEmpty() || optionD.isEmpty()) {
            showError("Please fill in all four options!");
            return;
        }
        if (correctAnswerCombo.getValue() == null) {
            showError("Please select the correct answer!");
            return;
        }
        String explanation = explanationArea.getText().trim();
        if (explanation.isEmpty()) {
            showError("Please provide an explanation!");
            return;
        }
        Question question = new Question(
                subjectCombo.getValue(),
                questionText,
                optionA,
                optionB,
                optionC,
                optionD,
                correctAnswerCombo.getValue(),
                explanation,
                difficultyCombo.getValue()
        );
        boolean saved = QuestionService.addQuestion(question);
        if (saved) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Question added successfully!");
            alert.showAndWait();
            handleClear();
        } else {
            showError("Failed to save question. Please try again.");
        }
    }

    @FXML
    private void handleClear() {
        subjectCombo.setValue(null);
        difficultyCombo.setValue(null);
        questionTextArea.clear();
        optionAField.clear();
        optionBField.clear();
        optionCField.clear();
        optionDField.clear();
        correctAnswerCombo.setValue(null);
        explanationArea.clear();
        errorLabel.setVisible(false);
    }

    @FXML
    private void handleBack() {
        SceneManager.switchScene("/views/question-bank.fxml");
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}