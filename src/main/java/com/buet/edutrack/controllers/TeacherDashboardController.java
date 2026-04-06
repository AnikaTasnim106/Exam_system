package com.buet.edutrack.controllers;

import com.buet.edutrack.utils.SceneManager;
import com.buet.edutrack.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class TeacherDashboardController {

    @FXML private Label welcomeLabel;
    @FXML private Button logoutButton;
    @FXML private Button createExamBtn;
    @FXML private Button viewResultsBtn;
    @FXML private Button manageStudentsBtn;
    @FXML private Button forumBtn;
    @FXML private Button createExamCardBtn;
    @FXML private Button viewResultsCardBtn;
    @FXML private Button manageStudentsCardBtn;
    @FXML private Button questionBankCardBtn;

    @FXML
    public void initialize() {
        String username = SessionManager.getCurrentUsername();
        if (username != null) {
            welcomeLabel.setText("Hello, " + username + "!");
        } else {
            welcomeLabel.setText("Hello Teacher!");
        }
        String logoutOriginal = logoutButton.getStyle();
        logoutButton.setOnMouseEntered(e -> logoutButton.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-size: 15px; -fx-alignment: center-left; -fx-padding: 15 20; -fx-background-radius: 12; -fx-font-weight: bold;"));
        logoutButton.setOnMouseExited(e -> logoutButton.setStyle(logoutOriginal));

        setupSidebarHover(forumBtn);
        setupSidebarHover(createExamBtn);
        setupSidebarHover(viewResultsBtn);

        setupCardHover(createExamCardBtn, "#667eea");
        setupCardHover(viewResultsCardBtn, "#888888");
        setupCardHover(manageStudentsCardBtn, "#e6a817");
        setupCardHover(questionBankCardBtn, "#5c4db1");
    }

    @FXML
    private void handleLogout() {
        SessionManager.logout();
        SceneManager.switchScene("/views/login.fxml");
    }

    @FXML
    private void handleCreateExam() {
        SceneManager.switchScene("/views/create-exam.fxml");
    }

    @FXML
    private void handleViewResults() {
        SceneManager.switchScene("/views/manage-exams.fxml");
    }

    @FXML
    private void handleManageStudents() {
        SceneManager.switchScene("/views/manage-students.fxml");
    }

    @FXML
    private void handleQuestionBank() {
        SceneManager.switchScene("/views/question-bank.fxml");
    }

    @FXML
    private void handleForum() {
        SceneManager.switchScene("/views/forum-list.fxml");
    }

    private void setupSidebarHover(Button button) {
        String original = button.getStyle();
        button.setOnMouseEntered(e -> button.setStyle(original + "-fx-background-color: #0f3460; -fx-text-fill: white;"));
        button.setOnMouseExited(e -> button.setStyle(original));
    }

    private void setupCardHover(Button button, String hoverColor) {
        String original = button.getStyle();
        button.setOnMouseEntered(e -> button.setStyle(original + "-fx-background-color: " + hoverColor + "; -fx-text-fill: white;"));
        button.setOnMouseExited(e -> button.setStyle(original));
    }
}