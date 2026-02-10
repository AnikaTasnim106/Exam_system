package com.buet.edutrack.controllers;

import com.buet.edutrack.models.Exam;
import com.buet.edutrack.services.ExamService;
import com.buet.edutrack.services.ResultService;
import com.buet.edutrack.utils.SceneManager;
import com.buet.edutrack.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class ExamListController {

    @FXML private ComboBox<String> subjectFilterCombo;
    @FXML private Label examCountLabel;
    @FXML private GridPane examsGrid;
    @FXML private VBox emptyStateBox;

    @FXML
    public void initialize() {
        setupSubjectFilter();
        loadExams();
    }

    private void setupSubjectFilter() {
        subjectFilterCombo.getItems().add("All Subjects");
        subjectFilterCombo.getItems().addAll(
                "Mathematics", "Physics", "Chemistry", "Biology",
                "Computer Science", "English", "History", "Geography"
        );
        subjectFilterCombo.setValue("All Subjects");
    }

    private void loadExams() {
        List<Exam> allExams = ExamService.getAllExams();
        displayExams(allExams);
    }

    @FXML
    private void handleFilter() {
        String selected = subjectFilterCombo.getValue();

        List<Exam> exams;
        if ("All Subjects".equals(selected)) {
            exams = ExamService.getAllExams();
        } else {
            exams = ExamService.getExamsBySubject(selected);
        }

        displayExams(exams);
    }

    private void displayExams(List<Exam> exams) {
        examsGrid.getChildren().clear();

        if (exams.isEmpty()) {
            examsGrid.setVisible(false);
            examsGrid.setManaged(false);
            emptyStateBox.setVisible(true);
            emptyStateBox.setManaged(true);
            examCountLabel.setText("0 exams available");
            return;
        }

        examsGrid.setVisible(true);
        examsGrid.setManaged(true);
        emptyStateBox.setVisible(false);
        emptyStateBox.setManaged(false);

        int row = 0;
        int col = 0;

        for (Exam exam : exams) {
            VBox examCard = createExamCard(exam);
            examsGrid.add(examCard, col, row);

            col++;
            if (col > 1) { // 2 columns
                col = 0;
                row++;
            }
        }

        examCountLabel.setText(exams.size() + " exam" + (exams.size() != 1 ? "s" : "") + " available");
    }

    private VBox createExamCard(Exam exam) {
        VBox card = new VBox(15);
        card.setAlignment(Pos.TOP_LEFT);
        card.setStyle("-fx-background-color: linear-gradient(to bottom right, #667eea 0%, #764ba2 100%);" +
                "-fx-background-radius: 15;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0, 0, 5);");
        card.setPadding(new Insets(25));
        card.setPrefWidth(500);
        card.setPrefHeight(220);

        // Exam title
        Label titleLabel = new Label(exam.getTitle());
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
        titleLabel.setWrapText(true);

        // Subject badge
        Label subjectLabel = new Label(exam.getSubject());
        subjectLabel.setStyle("-fx-background-color: rgba(255,255,255,0.3);" +
                "-fx-text-fill: white;" +
                "-fx-padding: 5 15;" +
                "-fx-background-radius: 15;" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;");

        // Info row
        HBox infoBox = new HBox(20);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        Label questionsLabel = new Label("📝 " + exam.getQuestionIds().size() + " questions");
        questionsLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.9); -fx-font-size: 14px;");

        Label durationLabel = new Label("⏱️ " + exam.getDuration() + " min");
        durationLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.9); -fx-font-size: 14px;");

        infoBox.getChildren().addAll(questionsLabel, durationLabel);

        // Description
        Label descLabel = new Label(exam.getDescription().isEmpty() ? "No description" : exam.getDescription());
        descLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.8); -fx-font-size: 13px;");
        descLabel.setWrapText(true);
        descLabel.setMaxHeight(40);

        // Spacer
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Button
        String currentUsername = SessionManager.getCurrentUsername();
        if (currentUsername == null) currentUsername = "student";

        boolean alreadyTaken = ResultService.hasStudentTakenExam(currentUsername, exam.getId());

        Button actionBtn;
        if (alreadyTaken) {
            actionBtn = new Button("✓ Completed");
            actionBtn.setStyle("-fx-background-color: #00C851;" +
                    "-fx-text-fill: white;" +
                    "-fx-padding: 12 30;" +
                    "-fx-background-radius: 25;" +
                    "-fx-font-weight: bold;" +
                    "-fx-font-size: 14px;");
            actionBtn.setDisable(true);
        } else {
            actionBtn = new Button("Start Exam →");
            actionBtn.setStyle("-fx-background-color: white;" +
                    "-fx-text-fill: #667eea;" +
                    "-fx-padding: 12 30;" +
                    "-fx-background-radius: 25;" +
                    "-fx-font-weight: bold;" +
                    "-fx-font-size: 14px;" +
                    "-fx-cursor: hand;");
            actionBtn.setOnAction(e -> handleStartExam(exam));

            actionBtn.setOnMouseEntered(e ->
                    actionBtn.setStyle("-fx-background-color: #f0f0f0;" +
                            "-fx-text-fill: #667eea;" +
                            "-fx-padding: 12 30;" +
                            "-fx-background-radius: 25;" +
                            "-fx-font-weight: bold;" +
                            "-fx-font-size: 14px;" +
                            "-fx-cursor: hand;")
            );
            actionBtn.setOnMouseExited(e ->
                    actionBtn.setStyle("-fx-background-color: white;" +
                            "-fx-text-fill: #667eea;" +
                            "-fx-padding: 12 30;" +
                            "-fx-background-radius: 25;" +
                            "-fx-font-weight: bold;" +
                            "-fx-font-size: 14px;" +
                            "-fx-cursor: hand;")
            );
        }

        card.getChildren().addAll(titleLabel, subjectLabel, infoBox, descLabel, spacer, actionBtn);

        return card;
    }

    private void handleStartExam(Exam exam) {
        // Confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Start Exam");
        alert.setHeaderText(exam.getTitle());
        alert.setContentText("You are about to start this exam.\n\n" +
                "Duration: " + exam.getDuration() + " minutes\n" +
                "Questions: " + exam.getQuestionIds().size() + "\n\n" +
                "Are you ready?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                TakeExamController.setCurrentExam(exam);
                SceneManager.switchScene("/views/take-exam.fxml");
            }
        });
    }

    @FXML
    private void handleBack() {
        SceneManager.switchScene("/views/student-dashboard.fxml");
    }
}