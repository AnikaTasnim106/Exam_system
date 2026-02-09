package com.buet.edutrack.controllers;

import com.buet.edutrack.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class TeacherDashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button logoutButton;

    @FXML
    private Button createExamBtn;

    @FXML
    private Button viewResultsBtn;

    @FXML
    private Button manageStudentsBtn;

    @FXML
    public void initialize() {
        welcomeLabel.setText("Hello Teacher!");
    }

    @FXML
    private void handleLogout() {
        SceneManager.switchScene("/views/login.fxml");
    }

    @FXML
    private void handleCreateExam() {
        System.out.println("Create Exam clicked");
        // TODO: Navigate to exam creation page
    }

    @FXML
    private void handleViewResults() {
        System.out.println("View Results clicked");
        // TODO: Navigate to results viewing page
    }

    @FXML
    private void handleManageStudents() {
        System.out.println("Manage Students clicked");
        // TODO: Navigate to student management page
    }

    @FXML
    private void handleQuestionBank() {
        SceneManager.switchScene("/views/question-bank.fxml");
    }
}