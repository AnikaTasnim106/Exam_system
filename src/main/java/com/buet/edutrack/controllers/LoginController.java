package com.buet.edutrack.controllers;

import com.buet.edutrack.models.User;
import com.buet.edutrack.services.UserService;
import com.buet.edutrack.utils.SceneManager;
import com.buet.edutrack.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.buet.edutrack.NetworkClient;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private Label errorLabel;
    @FXML
    private Button loginButton;

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll("Student", "Teacher");
        roleComboBox.setValue("Student");
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill in all fields!");
            errorLabel.setVisible(true);
            return;
        }

        String response = NetworkClient.sendLoginRequest(username, password);

        if (response != null && response.startsWith("SUCCESS:")) {
            String role = response.split(":")[1];
            User user = UserService.getUserByUsername(username);
            SessionManager.setCurrentUser(user);
            if (role.equals("Teacher")) {
                SceneManager.switchScene("/views/teacher-dashboard.fxml");
            } else {
                SceneManager.switchScene("/views/student-dashboard.fxml");
            }
        } else {
            String reason = response != null ? response.split(":")[1] : "Connection failed";
            errorLabel.setText(reason);
            errorLabel.setVisible(true);
        }
    }

    @FXML
    private void handleGoToSignup() {
        SceneManager.switchScene("/views/signup.fxml");
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}