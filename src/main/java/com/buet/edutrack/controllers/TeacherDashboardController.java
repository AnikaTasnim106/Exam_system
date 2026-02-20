package com.buet.edutrack.controllers;

import com.buet.edutrack.utils.SceneManager;
import com.buet.edutrack.utils.SessionManager;
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
        String username = SessionManager.getCurrentUsername();
        if (username != null) {
            welcomeLabel.setText("Hello, " + username + "!");
        } else {
            welcomeLabel.setText("Hello Teacher!");
        }
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
        System.out.println("Manage Students clicked");
        // TODO: Navigate to student management page
    }

    @FXML
    private void handleQuestionBank() {
        SceneManager.switchScene("/views/question-bank.fxml");
    }

    @FXML
    private void handleForum(){
        SceneManager.switchScene("/views/forum-list.fxml");
    }
}