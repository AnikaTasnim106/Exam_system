package com.buet.edutrack.controllers;

import com.buet.edutrack.models.Exam;
import com.buet.edutrack.models.Question;
import com.buet.edutrack.models.Result;
import com.buet.edutrack.services.ExamService;
import com.buet.edutrack.services.QuestionService;
import com.buet.edutrack.services.ResultService;
import com.buet.edutrack.utils.SceneManager;
import com.buet.edutrack.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.scene.layout.FlowPane;

public class PracticeController {

    @FXML private Label titleLabel;
    @FXML private FlowPane examsContainer;
    @FXML private Label weakConceptsLabel;
    @FXML private Button backButton;

    @FXML
    public void initialize(){
        loadPastExams();
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: #6c63ff; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-border-color: #6c63ff; -fx-border-width: 1; -fx-border-radius: 8; -fx-padding: 10 20; -fx-font-size: 14px;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: #2d1b69; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-border-color: #6c63ff; -fx-border-width: 1; -fx-border-radius: 8; -fx-padding: 10 20; -fx-font-size: 14px;"));
    }

    private void loadPastExams(){
        examsContainer.getChildren().clear();
        String username = SessionManager.getCurrentUsername();
        List<Result> Results = ResultService.getAllResults();
        for(Result result: Results){
            if(result.getStudentUsername().equals(username)){
                Exam exam = ExamService.getExamById(result.getExamId());
                if(exam != null){
                    examsContainer.getChildren().add(createExamCard(result, exam));
                }
            }

        }
    }

    private String getSubjectEmoji(String subject) {
        switch(subject) {
            case "Physics": return "⚛️  " + subject;
            case "Biology": return "🧬  " + subject;
            case "Chemistry": return "🧪  " + subject;
            case "Mathematics": return "📐  " + subject;
            case "Computer Science": return "💻  " + subject;
            case "English": return "📖  " + subject;
            case "History": return "🏛️  " + subject;
            case "Geography": return "🌍  " + subject;
            default: return "📚  " + subject;
        }
    }
    private VBox createExamCard(Result result, Exam exam) {
        VBox card = new VBox();
        card.setSpacing(12);
        card.setPrefWidth(260);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 4);");

        // Subject badge
        Label subjectLabel = new Label(exam.getSubject());
        subjectLabel.setStyle("-fx-background-color: #e8f4fd; -fx-text-fill: #2196F3; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 4 10; -fx-border-color: #2196F3; -fx-border-radius: 6; -fx-border-width: 1;");

        // Title
        Label titleLabel = new Label(exam.getTitle());
        titleLabel.setStyle("-fx-text-fill: #1a1a2e; -fx-font-size: 16px; -fx-font-weight: bold;");
        titleLabel.setWrapText(true);

        // Score circle color
        double percentage = result.getPercentage();
        String circleColor;
        String statusText;
        String statusColor;
        if (percentage == 0) {
            circleColor = "#ff4444";
            statusText = "All Wrong";
            statusColor = "#ff4444";
        } else if (percentage == 100) {
            circleColor = "#00cc66";
            statusText = "Perfect score!";
            statusColor = "#00cc66";
        } else {
            circleColor = "#2196F3";
            statusText = "Needs practice";
            statusColor = "#2196F3";
        }

        // Score circle using a Label styled as circle
        Label scoreCircle = new Label(String.format("%.0f%%", percentage));
        scoreCircle.setPrefWidth(100);
        scoreCircle.setPrefHeight(100);
        scoreCircle.setStyle(
                "-fx-background-color: white; " +
                        "-fx-border-color: " + circleColor + "; " +
                        "-fx-border-width: 6; " +
                        "-fx-border-radius: 50; " +
                        "-fx-background-radius: 50; " +
                        "-fx-alignment: center; " +
                        "-fx-font-size: 18px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: " + circleColor + ";"
        );

        javafx.scene.layout.StackPane circleContainer = new javafx.scene.layout.StackPane(scoreCircle);
        circleContainer.setAlignment(javafx.geometry.Pos.CENTER);

        Label statusLabel = new Label(statusText);
        statusLabel.setStyle("-fx-text-fill: " + statusColor + "; -fx-font-size: 13px; -fx-font-weight: bold;");
        statusLabel.setAlignment(javafx.geometry.Pos.CENTER);
        statusLabel.setMaxWidth(Double.MAX_VALUE);

        // Practice button
        Button practiceButton = new Button("▶  Start Practice");
        practiceButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-pref-width: 220;");
        practiceButton.setOnAction(e -> handlePracticeExam(result));
        practiceButton.setOnMouseEntered(e -> practiceButton.setStyle("-fx-background-color: #1976D2; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-pref-width: 220;"));
        practiceButton.setOnMouseExited(e -> practiceButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-pref-width: 220;"));

        card.getChildren().addAll(subjectLabel, titleLabel, circleContainer, statusLabel, practiceButton);
        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0, 0, 6);"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 4);"));
        return card;
    }

    @FXML
    private void handleBack(){
        SceneManager.switchScene("/views/student-dashboard.fxml");
    }

    private void handlePracticeExam(Result result){
        PracticeSessionController.setCurrentResult(result);
        SceneManager.switchScene("/views/practice-session.fxml");
    }
}
