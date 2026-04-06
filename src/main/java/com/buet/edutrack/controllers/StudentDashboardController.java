package com.buet.edutrack.controllers;

import com.buet.edutrack.utils.SceneManager;
import com.buet.edutrack.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class StudentDashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button logoutButton;

    @FXML
    private Button attendExamBtn;

    @FXML
    private Button viewResultBtn;

    @FXML
    private Button leaderboardBtn;

    @FXML private Button forumBtn;

    @FXML private Button attendExamCardBtn;
    @FXML private Button viewResultCardBtn;
    @FXML private Button leaderboardCardBtn;
    @FXML private Button practiceCardBtn;

    @FXML
    public void initialize() {
        String username = SessionManager.getCurrentUsername();
        if (username != null) {
            welcomeLabel.setText("Hello, " + username + "!");
        } else {
            welcomeLabel.setText("Hello!");
        }
        setupSidebarHover(forumBtn);
        setupSidebarHover(attendExamBtn);
        setupSidebarHover(viewResultBtn);
        String logoutOriginal = logoutButton.getStyle();
        logoutButton.setOnMouseEntered(e -> logoutButton.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-size: 15px; -fx-alignment: center-left; -fx-padding: 15 20; -fx-background-radius: 12; -fx-font-weight: bold;"));
        logoutButton.setOnMouseExited(e -> logoutButton.setStyle(logoutOriginal));

        setupCardHover(attendExamCardBtn, "#667eea");
        setupCardHover(viewResultCardBtn, "#888888");
        setupCardHover(leaderboardCardBtn, "#e6a817");
        setupCardHover(practiceCardBtn, "#5c4db1");
    }

    @FXML
    private void handleLogout() {
        SessionManager.logout();
        SceneManager.switchScene("/views/login.fxml");
    }

    @FXML
    private void handleAttendExam() {
        SceneManager.switchScene("/views/exam-list.fxml");
    }

    @FXML
    private void handleViewResult() {
        SceneManager.switchScene("/views/view-results.fxml");
    }

    @FXML
    private void handleLeaderboard() {
        SceneManager.switchScene("/views/leaderboard.fxml");
    }

    @FXML
    private void handlePractice() {
        SceneManager.switchScene("/views/practice.fxml");
    }

    @FXML
    private void handleForum(){
        SceneManager.switchScene("/views/forum-list.fxml");
    }

    private void setupSidebarHover(Button button) {
        String original = button.getStyle();
        button.setOnMouseEntered(e -> button.setStyle(original + "-fx-background-color: #0f3460; -fx-text-fill: white;"));
        button.setOnMouseExited(e -> button.setStyle(original));
    }

    private void setupCardHover(Button button, String hoverColor){
        String original = button.getStyle();
        button.setOnMouseEntered(e->button.setStyle(original + "-fx-background-color: " + hoverColor + "; -fx-text-fill: white;"));
        button.setOnMouseExited(e->button.setStyle(original));
    }
}