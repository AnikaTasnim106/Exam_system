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

    @FXML
    public void initialize() {
        String username = SessionManager.getCurrentUsername();
        if (username != null) {
            welcomeLabel.setText("Hello, " + username + "!");
        } else {
            welcomeLabel.setText("Hello!");
        }
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
        System.out.println("Leaderboard clicked");
        // TODO: Navigate to leaderboard
    }

    @FXML
    private void handlePractice() {
        System.out.println("Practice clicked");
        // TODO: Navigate to practice mode
    }
}