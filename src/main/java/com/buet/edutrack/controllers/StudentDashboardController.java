package com.buet.edutrack.controllers;

import com.buet.edutrack.utils.SceneManager;
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
        welcomeLabel.setText("Hello!");
    }

    @FXML
    private void handleLogout() {
        SceneManager.switchScene("/views/login.fxml");
    }

    @FXML
    private void handleAttendExam() {
        System.out.println("Attend Exam clicked");
        // TODO: Navigate to exam selection
    }

    @FXML
    private void handleViewResult() {
        System.out.println("View Results clicked");
        // TODO: Navigate to results
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