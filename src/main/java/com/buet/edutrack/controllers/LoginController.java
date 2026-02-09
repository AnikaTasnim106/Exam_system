package com.buet.edutrack.controllers;
import com.buet.edutrack.models.User;
import com.buet.edutrack.services.UserService;
import com.buet.edutrack.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
        String username = usernameField.getText();
        String password = passwordField.getText();
        String selectedRole = roleComboBox.getValue();

        errorLabel.setVisible(false);

        if (username.isEmpty() || password.isEmpty() || selectedRole == null) {
            showError("Please fill in all fields!");
            return;
        }

        // Authenticate using UserService
        User user = UserService.authenticateUser(username, password);

        if (user == null) {
            showError("Invalid username or password!");
            return;
        }

        // Check if role matches
        if (!user.getRole().equals(selectedRole)) {
            showError("Please select the correct role for your account!");
            return;
        }

        // Navigate to appropriate dashboard
        if (user.getRole().equals("Student")) {
            SceneManager.switchScene("/views/student-dashboard.fxml");
        } else if (user.getRole().equals("Teacher")) {
            SceneManager.switchScene("/views/teacher-dashboard.fxml");
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